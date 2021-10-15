package com.magma.viraladminpanel.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.magma.viraladminpanel.People;
import com.magma.viraladminpanel.Popup.PopupBlockUser;
import com.magma.viraladminpanel.Popup.PopupRemoveUser;
import com.magma.viraladminpanel.R;
import com.magma.viraladminpanel.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {
    private Context mContext;
    private Activity mActivity;
    private List<People> mPeoples;

    public PeopleAdapter(Context mContext, Activity mActivity, List<People> mPeoples) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mPeoples = mPeoples;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.people_item, parent, false);
        return new PeopleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        People people = mPeoples.get(position);

        holder.user_blocked.setText(String.valueOf(people.getBlocked().size()));
        holder.user_blocked_by.setText(String.valueOf(people.getBlockedBy().size()));
        holder.user_post_reports.setText(String.valueOf(people.getPostReports().size()));
        holder.user_profile_reports.setText(String.valueOf(people.getProfileReports().size()));

        User user = people.getUser();

        holder.user_name.setText(user.getFirstName() + " " + user.getLastName());

        if(!user.getProfilePhoto().equals("default")) {
            Glide.with(mContext)
                    .load(user.getProfilePhoto())
                    .into(holder.user_photo);
        } else { holder.user_photo.setImageResource(R.drawable.default_profile_photo); }

        holder.people_remove.setOnClickListener(view -> {
            PopupRemoveUser popupRemoveUser = new PopupRemoveUser();
            popupRemoveUser.showPopup(mActivity, user);
        });

        holder.people_block.setOnClickListener(view -> {
            PopupBlockUser popupBlockUser = new PopupBlockUser();
            popupBlockUser.showPopup(mActivity, user);
        });
    }

    @Override
    public int getItemCount() { return mPeoples.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView user_name;
        public CircleImageView user_photo;
        public ImageButton people_block;
        public ImageButton people_remove;
        public TextView user_blocked;
        public TextView user_blocked_by;
        public TextView user_post_reports;
        public TextView user_profile_reports;

        public ViewHolder(View itemView) {
            super(itemView);

            user_name = (TextView) itemView.findViewById(R.id.user_name);
            user_photo = (CircleImageView) itemView.findViewById(R.id.user_photo);
            people_block = (ImageButton) itemView.findViewById(R.id.people_block);
            people_remove = (ImageButton) itemView.findViewById(R.id.people_remove);
            user_blocked = (TextView) itemView.findViewById(R.id.user_blocked);
            user_blocked_by = (TextView) itemView.findViewById(R.id.user_blocked_by);
            user_post_reports = (TextView) itemView.findViewById(R.id.user_post_reports);
            user_profile_reports = (TextView)  itemView.findViewById(R.id.user_profile_reports);
        }
    }
}
