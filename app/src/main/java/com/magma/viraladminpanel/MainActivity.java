package com.magma.viraladminpanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;
import com.magma.viraladminpanel.Adapter.PeopleAdapter;
import com.magma.viraladminpanel.Adapter.PostAdapter;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadedPeoples = false;
        loadedPosts = false;

        mActivity = this;

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        imageViewPeople = (ImageView) findViewById(R.id.imgPeople);
        imageViewPost = (ImageView) findViewById(R.id.imgPosts);
        
        imageViewPeople.setOnClickListener(view -> onPeople());
        imageViewPost.setOnClickListener(view -> onPost());

        recyclerViewPeople = (RecyclerView) findViewById(R.id.recPeople);
        recyclerViewPeople.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewPosts = (RecyclerView) findViewById(R.id.recPosts);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));

        mPeoples = new ArrayList<>();
        mReportedPosts = new ArrayList<>();

        onPeople();

        getPeoples();
        getPosts();
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
                    mPeoples.add(people);
                }

                loadedPeoples = true;

                if(imageViewPeople.isActivated()) progressBar.setVisibility(View.GONE);

                peopleAdapter = new PeopleAdapter(getApplicationContext(), mActivity, mPeoples);
                recyclerViewPeople.setAdapter(peopleAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void getPosts() {
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("admin_panel").child("posts");

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mReportedPosts.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ReportedPost reportedPost = dataSnapshot.getValue(ReportedPost.class);
                    reportedPost.setPostId(dataSnapshot.getKey());
                    mReportedPosts.add(reportedPost);
                }

                loadedPosts = true;

                if(imageViewPost.isActivated()) progressBar.setVisibility(View.GONE);

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