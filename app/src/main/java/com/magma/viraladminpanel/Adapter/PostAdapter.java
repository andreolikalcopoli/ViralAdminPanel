package com.magma.viraladminpanel.Adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.magma.viraladminpanel.Comment;
import com.magma.viraladminpanel.Popup.PopupPostReports;
import com.magma.viraladminpanel.Popup.PopupRemovePost;
import com.magma.viraladminpanel.Post;
import com.magma.viraladminpanel.R;
import com.magma.viraladminpanel.ReportedPost;
import com.magma.viraladminpanel.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Context mContext;
    private Activity mActivity;
    private List<ReportedPost> mReportedPosts;

    private SimpleExoPlayer simpleExoPlayer;

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

        Map<String, String> reports = reportedPost.getReports();
        String str = reports.size() + " reports";

        holder.post_reports.setText(str);

        holder.post_reports.setOnClickListener(view -> {
            PopupPostReports popupPostReports = new PopupPostReports();
            popupPostReports.showPopup(mActivity, reports, reportedPost.getPostId());
        });

        holder.post_remove.setOnClickListener(view -> {
            PopupRemovePost popupRemovePost = new PopupRemovePost();
            popupRemovePost.showPopup(mActivity, reportedPost.getPostId());
        });

        Post post = reportedPost.getPost();

        User user = post.getPostUser();

        holder.user_name.setText(user.getFirstName() + " " + user.getLastName());

        if(!user.getProfilePhoto().equals("default")) {
            Glide.with(mContext)
                    .load(user.getProfilePhoto())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(holder.user_photo);
        }
        else { holder.user_photo.setImageResource(R.drawable.default_profile_photo); }

        if(post.getLocation() != null) holder.user_location.setText(post.getLocation());
        if(post.getDescription() != null) holder.post_description.setText(post.getDescription());

        holder.post_description.setOnClickListener(view -> {
            if(!post.getDescription().isEmpty()) {
                holder.post_description.setVisibility(View.GONE);
                holder.linearLayoutHide.setVisibility(View.VISIBLE);
                holder.coordinatorLayoutFullDescription.setVisibility(View.VISIBLE);
                holder.post_full_description.setText(post.getDescription());
            }
        });

        holder.buttonHide.setOnClickListener(view -> {
            holder.post_description.setVisibility(View.VISIBLE);
            holder.linearLayoutHide.setVisibility(View.GONE);
            holder.coordinatorLayoutFullDescription.setVisibility(View.GONE);
        });

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

        if(post.getOptionComments()) {
            holder.linearLayoutComment.setVisibility(View.VISIBLE);

            List<Comment> mComments = new ArrayList<>();

            DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference("comments");

            commentRef.child(post.getId())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mComments.clear();

                            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Comment comment = dataSnapshot.getValue(Comment.class);
                                mComments.add(comment);
                            }

                            holder.textViewComment.setText(String.valueOf(mComments.size()));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });
        } else { holder.linearLayoutComment.setVisibility(View.GONE); }

        if(post.getOptionShare()) holder.linearLayoutShare.setVisibility(View.VISIBLE);
        else holder.linearLayoutShare.setVisibility(View.INVISIBLE);

        if(post.getType().equals(Post.POST_TYPE_IMAGE)) {
            holder.post_image.setVisibility(View.VISIBLE);
            holder.linearLayoutText.setVisibility(View.GONE);
            holder.linearLayoutVideo.setVisibility(View.GONE);

            if(post.getPost() != null) { Glide.with(mContext).load(post.getPost()).into(holder.post_image); }
        } else if(post.getType().equals(Post.POST_TYPE_TEXT)) {
            holder.post_image.setVisibility(View.GONE);
            holder.linearLayoutText.setVisibility(View.VISIBLE);
            holder.linearLayoutVideo.setVisibility(View.GONE);

            if(post.getPost() != null) { holder.post_text.setText(post.getPost()); }
        } else if(post.getType().equals(Post.POST_TYPE_VIDEO)) {
            holder.post_image.setVisibility(View.GONE);
            holder.linearLayoutText.setVisibility(View.GONE);
            holder.linearLayoutVideo.setVisibility(View.VISIBLE);

            if(post.getPost() != null) {
                try {
                    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

                    simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

                    Uri uri = Uri.parse(post.getPost());

                    DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");

                    ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

                    MediaSource mediaSource = new ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, null, null);

                    holder.post_video.setPlayer(simpleExoPlayer);

                    simpleExoPlayer.prepare(mediaSource);

                    simpleExoPlayer.addListener(new Player.EventListener() {
                        @Override
                        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) { }
                        @Override
                        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) { }
                        @Override
                        public void onLoadingChanged(boolean isLoading) { }
                        @Override
                        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                            if(playbackState == Player.STATE_BUFFERING) {
                                holder.post_video_progress.setVisibility(View.VISIBLE);
                                holder.post_video.setUseController(false);
                            } else if(playbackState == Player.STATE_READY) {
                                holder.post_video_progress.setVisibility(View.GONE);
                                holder.post_video.setUseController(true);
                            }
                        }
                        @Override
                        public void onRepeatModeChanged(int repeatMode) { }
                        @Override
                        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) { }
                        @Override
                        public void onPlayerError(ExoPlaybackException error) { }
                        @Override
                        public void onPositionDiscontinuity(int reason) { }
                        @Override
                        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) { }
                        @Override
                        public void onSeekProcessed() { }
                    });

                    simpleExoPlayer.setPlayWhenReady(true);
                }

                catch (Exception e){ }
            }
        }

    }

    @Override
    public int getItemCount() { return mReportedPosts.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView user_photo;
        public TextView user_name;
        public TextView user_location;

        public ImageView post_image;

        public TextView textViewLike;
        public TextView textViewComment;

        public TextView post_description;

        public LinearLayout linearLayoutText;
        public TextView post_text;

        public LinearLayout linearLayoutVideo;
        public SimpleExoPlayerView post_video;
        public ProgressBar post_video_progress;

        public LinearLayout linearLayoutComment;

        public LinearLayout linearLayoutShare;

        public LinearLayout linearLayoutHide;
        public Button buttonHide;

        public CoordinatorLayout coordinatorLayoutFullDescription;
        public TextView post_full_description;

        public TextView post_reports;
        public ImageButton post_remove;

        public ViewHolder(View itemView) {
            super(itemView);

            user_photo = (CircleImageView) itemView.findViewById(R.id.post_user_photo);
            user_name = (TextView) itemView.findViewById(R.id.post_user_name);
            user_location = (TextView) itemView.findViewById(R.id.post_user_location);

            post_image = (ImageView) itemView.findViewById(R.id.post_image);

            textViewLike = (TextView) itemView.findViewById(R.id.textViewLike);
            textViewComment = (TextView) itemView.findViewById(R.id.textViewComment);

            post_description = (TextView) itemView.findViewById(R.id.post_description);

            linearLayoutText = (LinearLayout) itemView.findViewById(R.id.linearLayoutPostText);
            post_text = (TextView) itemView.findViewById(R.id.post_text);

            linearLayoutVideo = (LinearLayout) itemView.findViewById(R.id.linearLayoutPostVideo);
            post_video = (SimpleExoPlayerView) itemView.findViewById(R.id.post_video);
            post_video_progress = (ProgressBar) itemView.findViewById(R.id.post_video_progress);

            linearLayoutComment = (LinearLayout) itemView.findViewById(R.id.post_comment);

            linearLayoutShare = (LinearLayout) itemView.findViewById(R.id.lin_share);

            linearLayoutHide = (LinearLayout) itemView.findViewById(R.id.linearLayoutPostHide);
            buttonHide = (Button) itemView.findViewById(R.id.buttonHidePost);

            coordinatorLayoutFullDescription = (CoordinatorLayout) itemView.findViewById(R.id.coordinatorLayout_post_description);
            post_full_description = (TextView) itemView.findViewById(R.id.post_full_description);

            post_reports = (TextView) itemView.findViewById(R.id.post_reports);

            post_remove = (ImageButton) itemView.findViewById(R.id.post_remove);
        }
    }
}
