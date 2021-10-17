package com.magma.viraladminpanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.magma.viraladminpanel.Adapter.PeopleAdapter;
import com.magma.viraladminpanel.Adapter.PostAdapter;
import com.magma.viraladminpanel.Popup.PopupAddAdmin;
import com.magma.viraladminpanel.Popup.PopupBlockedAdmin;
import com.magma.viraladminpanel.Popup.PopupSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;

    private ImageView imageViewPeople;
    private ImageView imageViewPost;

    private List<People> mPeoples;
    private PeopleAdapter peopleAdapter;
    private RecyclerView recyclerViewPeople;

    private List<ReportedPost> mReportedPosts;
    private PostAdapter postAdapter;
    private RecyclerView recyclerViewPosts;

    private Activity mActivity;

    private Boolean loadedPeoples;
    private Boolean loadedPosts;

    private ImageButton addAdmin;
    private ImageButton settings;

    private EditText editTextSearch;

    private FirebaseUser firebaseUser;

    private Map<String, User> mUsers;
    private Map<String, Post> mPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mActivity = this;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null) {
            checkBlocked();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void checkBlocked() {
        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference("admins");

        adminRef.child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Admin admin = snapshot.getValue(Admin.class);

                        if(admin != null) {
                            init(admin);
                        } else {
                            PopupBlockedAdmin popupBlockedAdmin = new PopupBlockedAdmin();
                            popupBlockedAdmin.showPopup(mActivity);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void init(Admin admin) {
        loadedPeoples = false;
        loadedPosts = false;

        addAdmin = (ImageButton) findViewById(R.id.imageButtonAddAdmin);
        settings = (ImageButton) findViewById(R.id.imageButtonSettings);

        if(admin.getAdminLevel().equals("head admin")) {
            addAdmin.setVisibility(View.VISIBLE);
            settings.setVisibility(View.VISIBLE);
        } else if(admin.getAdminLevel().equals("admin")) {
            addAdmin.setVisibility(View.GONE);
            settings.setVisibility(View.GONE);
        }

        addAdmin.setOnClickListener(view -> {
            PopupAddAdmin popupAddAdmin = new PopupAddAdmin();
            popupAddAdmin.showPopup(mActivity);
        });

        settings.setOnClickListener(view -> {
            PopupSettings popupSettings = new PopupSettings();
            popupSettings.showPopup(mActivity);
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        imageViewPeople = (ImageView) findViewById(R.id.imgPeople);
        imageViewPost = (ImageView) findViewById(R.id.imgPosts);

        imageViewPeople.setOnClickListener(view -> onPeople());
        imageViewPost.setOnClickListener(view -> onPost());

        recyclerViewPeople = (RecyclerView) findViewById(R.id.recPeople);
        recyclerViewPeople.setNestedScrollingEnabled(false);
        recyclerViewPeople.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewPosts = (RecyclerView) findViewById(R.id.recPosts);
        recyclerViewPosts.setNestedScrollingEnabled(false);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));

        editTextSearch = (EditText) findViewById(R.id.editTextSearch);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                final String s = editTextSearch.getText().toString();
                searchUsers(s.toLowerCase());
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });

        mUsers = new HashMap<>();
        mPosts = new HashMap<>();

        mPeoples = new ArrayList<>();
        mReportedPosts = new ArrayList<>();

        onPeople();

        getUsers();
    }

    private void searchUsers(String s) {
        Query query = FirebaseDatabase.getInstance().getReference("users").orderByChild("search")
                .startAt(s)
                .endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    mUsers.put(user.getUserId(), user);
                }

                getPeoples();
                getPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }

    private void getUsers() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(editTextSearch.getText().toString().isEmpty()) {
                    mUsers.clear();

                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
                        mUsers.put(user.getUserId(), user);
                    }

                    getPeoples();
                    getPosts();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void getPosts() {
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("posts");

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mPosts.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    if(mUsers.containsKey(post.getUser())) {
                        post.setPostUser(mUsers.get(post.getUser()));
                        mPosts.put(post.getId(), post);
                    }
                }

                getReportedPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void getPeoples() {
        DatabaseReference peopleRef = FirebaseDatabase.getInstance().getReference("admin_panel").child("users");

        peopleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mPeoples.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    People people = dataSnapshot.getValue(People.class);
                    people.setUserId(dataSnapshot.getKey());

                    if(mUsers.containsKey(people.getUserId())) {
                        people.setUser(mUsers.get(people.getUserId()));
                        mPeoples.add(people);
                    }
                }

                loadedPeoples = true;

                if(imageViewPeople.isActivated()) progressBar.setVisibility(View.GONE);

                Collections.sort(mPeoples);

                peopleAdapter = new PeopleAdapter(getApplicationContext(), mActivity, mPeoples);
                recyclerViewPeople.setAdapter(peopleAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void getReportedPosts() {
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("admin_panel").child("posts");

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mReportedPosts.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ReportedPost reportedPost = dataSnapshot.getValue(ReportedPost.class);
                    reportedPost.setPostId(dataSnapshot.getKey());

                    if(mPosts.containsKey(reportedPost.getPostId())) {
                        reportedPost.setPost(mPosts.get(reportedPost.getPostId()));
                        mReportedPosts.add(reportedPost);
                    }
                }

                loadedPosts = true;

                if(imageViewPost.isActivated()) progressBar.setVisibility(View.GONE);

                Collections.sort(mReportedPosts);

                postAdapter = new PostAdapter(getApplicationContext(), mActivity, mReportedPosts);
                recyclerViewPosts.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
    
    private void onPeople() {
        imageViewPeople.setActivated(true);
        imageViewPost.setActivated(false);
        recyclerViewPeople.setVisibility(View.VISIBLE);
        recyclerViewPosts.setVisibility(View.GONE);

        if(!loadedPeoples) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }

    private void onPost() {
        imageViewPost.setActivated(true);
        imageViewPeople.setActivated(false);
        recyclerViewPosts.setVisibility(View.VISIBLE);
        recyclerViewPeople.setVisibility(View.GONE);

        if(!loadedPosts) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }
}