package com.magma.viraladminpanel.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.magma.viraladminpanel.Post;
import com.magma.viraladminpanel.R;
import com.magma.viraladminpanel.ReportedPost;
import com.magma.viraladminpanel.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Context mContext;
    private Activity mActivity;
    private List<ReportedPost> mReportedPosts;

    public PostAdapter(Context mContext, Activity mActivity, List<ReportedPost> mReportedPosts) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mReportedPosts = mReportedPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReportedPost reportedPost = mReportedPosts.get(position);

        setPost(reportedPost.getPostId(), holder);

    }

    private void setPost(String postId, ViewHolder holder) {
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("posts");

        postRef.child(postId).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Post post = task.getResult().getValue(Post.class);

                        setUserInfo(post.getUser(), holder);

                        DatabaseReference likeRef = FirebaseDatabase.getInstance().getReference("likes");

                        likeRef.child(post.getId())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        List<String> likes = new ArrayList<>();

                                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            final String like = dataSnapshot.getValue(String.class);
                                            likes.add(like);
                                        }

                                        holder.textViewLike.setText(String.valueOf(likes.size()));
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) { }
                                });


                    }
                });
    }

    private void setUserInfo(String userId, ViewHolder holder) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
        userRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                holder.user_name.setText(user.getFirstName() + " " + user.getLastName());

                if(!user.getProfilePhoto().equals("default")) { Glide.with(mContext).load(user.getProfilePhoto()).into(holder.user_photo); }
                else { holder.user_photo.setImageResource(R.drawable.default_profile_photo); }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }


    @Override
    public int getItemCount() { return mReportedPosts.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView user_photo;
        public TextView user_name;
        public TextView user_location;

        // public ImageButton options;

        public ImageView post_image;
        public Button buttonLike;
        public TextView textViewLike;
        public Button buttonComment;
        public TextView textViewComment;
        public TextView post_description;
        public Button buttonShare;
        public Button buttonSend;
        public LinearLayout linearLayoutText;
        public TextView post_text;
        public LinearLayout linearLayoutVideo;
        public SimpleExoPlayerView post_video;
        public LinearLayout linearLayoutComment;

        public LinearLayout linearLayoutShare;

        // public CoordinatorLayout coordinatorLayoutOptions;
        // public TextView post_unfollow;
        // public TextView post_report_user;
        // public TextView post_report;

        public ProgressBar post_video_progress;

        // --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---

        public LinearLayout linearLayoutHide;
        public Button buttonHide;

        // --- ---- -- -- - -- - - - -- - - - - - - - - -- - - - - -- - - - -- - - --  --

        public CoordinatorLayout coordinatorLayoutFullDescription;
        public TextView post_full_description;

        // Comment -- --- POST COMMENT

        public ConstraintLayout layoutPostComments;
        public TextView comments_number;
        public RecyclerView recyclerViewComments;

        public EditText editTextComments;
        public TextView textViewComments;

        public ViewHolder(View itemView) {
            super(itemView);

            user_photo = (CircleImageView) itemView.findViewById(R.id.post_user_photo);
            user_name = (TextView) itemView.findViewById(R.id.post_user_name);
            user_location = (TextView) itemView.findViewById(R.id.post_user_location);

            // options = (ImageButton) itemView.findViewById(R.id.post_options);

            post_image = (ImageView) itemView.findViewById(R.id.post_image);
            buttonLike = (Button) itemView.findViewById(R.id.buttonLike);
            textViewLike = (TextView) itemView.findViewById(R.id.textViewLike);
            buttonComment = (Button) itemView.findViewById(R.id.buttonComment);
            textViewComment = (TextView) itemView.findViewById(R.id.textViewComment);
            post_description = (TextView) itemView.findViewById(R.id.post_description);
            buttonShare = (Button) itemView.findViewById(R.id.buttonShare);
            buttonSend = (Button) itemView.findViewById(R.id.buttonSend);
            linearLayoutText = (LinearLayout) itemView.findViewById(R.id.linearLayoutPostText);
            post_text = (TextView) itemView.findViewById(R.id.post_text);
            linearLayoutVideo = (LinearLayout) itemView.findViewById(R.id.linearLayoutPostVideo);
            post_video = (SimpleExoPlayerView) itemView.findViewById(R.id.post_video);
            linearLayoutComment = (LinearLayout) itemView.findViewById(R.id.post_comment);

            linearLayoutShare = (LinearLayout) itemView.findViewById(R.id.lin_share);

            /*
            coordinatorLayoutOptions = (CoordinatorLayout) itemView.findViewById(R.id.coordinatorLayout_post_settings);
            post_unfollow = (TextView) itemView.findViewById(R.id.post_unfollow);
            post_report_user = (TextView) itemView.findViewById(R.id.post_report_user);
            post_report = (TextView) itemView.findViewById(R.id.post_report);

             */

            post_video_progress = (ProgressBar) itemView.findViewById(R.id.post_video_progress);

            // --- --- --- --- --- --- --- --- --- ---

            linearLayoutHide = (LinearLayout) itemView.findViewById(R.id.linearLayoutPostHide);
            buttonHide = (Button) itemView.findViewById(R.id.buttonHidePost);

            // --- -- - - - -- - - -- - - - - - -- - - - - - - - -- - - - - - -

            coordinatorLayoutFullDescription = (CoordinatorLayout) itemView.findViewById(R.id.coordinatorLayout_post_description);
            post_full_description = (TextView) itemView.findViewById(R.id.post_full_description);

            layoutPostComments = (ConstraintLayout) itemView.findViewById(R.id.consPostComments);
            comments_number = (TextView) itemView.findViewById(R.id.commentsNumber);
            recyclerViewComments = (RecyclerView) itemView.findViewById(R.id.recComments);

            editTextComments = (EditText) itemView.findViewById(R.id.editTextComments);
            textViewComments = (TextView) itemView.findViewById(R.id.textViewComments);
        }
    }
}
