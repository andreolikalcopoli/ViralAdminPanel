package com.magma.viraladminpanel.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.magma.viraladminpanel.Admin;

import com.magma.viraladminpanel.R;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder> {
    private Context mContext;
    private List<Admin> mAdmins;

    public AdminAdapter(Context mContext, List<Admin> mAdmins) {
        this.mContext = mContext;
        this.mAdmins = mAdmins;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.admin_item, parent, false);
        return new AdminAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Admin admin = mAdmins.get(position);

        holder.admin_email.setText(admin.getAdminEmail());
        holder.button_remove.setOnClickListener(view -> removeAdmin(admin.getAdminId()));
    }


    private void removeAdmin(String adminId) {
        ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Removing in progress...");
        progressDialog.show();

        DatabaseReference removeRef = FirebaseDatabase.getInstance().getReference("admins");
        removeRef.child(adminId).removeValue().addOnCompleteListener(task -> progressDialog.dismiss());
    }

    @Override
    public int getItemCount() { return mAdmins.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView admin_email;
        public ImageButton button_remove;

        public ViewHolder(View itemView) {
            super(itemView);

            admin_email = (TextView) itemView.findViewById(R.id.admin_email);
            button_remove = (ImageButton) itemView.findViewById(R.id.admin_remove);
        }
    }
}
