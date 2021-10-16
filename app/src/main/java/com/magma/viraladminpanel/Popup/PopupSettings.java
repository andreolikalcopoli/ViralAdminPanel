package com.magma.viraladminpanel.Popup;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.magma.viraladminpanel.Adapter.AdminAdapter;
import com.magma.viraladminpanel.Admin;

import com.magma.viraladminpanel.R;

import java.util.ArrayList;
import java.util.List;

public class PopupSettings {

    private Activity mActivity;

    private List<Admin> mAdmins;
    private RecyclerView recyclerView;
    private AdminAdapter adminAdapter;

    private FirebaseUser firebaseUser;

    public void showPopup(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.settings_popup);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mActivity = activity;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Button buttonClose = (Button) dialog.findViewById(R.id.buttonCloseSettingsPopup);
        buttonClose.setOnClickListener(view -> dialog.dismiss());

        TextView textViewAddAdmin = (TextView) dialog.findViewById(R.id.textViewAddAdminSettingsPopUp);
        textViewAddAdmin.setOnClickListener(view -> addAdmin(dialog));

        recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerViewSettingsPopup);
        recyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext()));

        mAdmins = new ArrayList<>();

        getAdmins(dialog);

        dialog.show();
    }

    private void getAdmins(Dialog dialog) {
        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference("admins");

        adminRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mAdmins.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Admin admin = dataSnapshot.getValue(Admin.class);
                    if(!admin.getAdminId().equals(firebaseUser.getUid())) mAdmins.add(admin);
                }

                adminAdapter = new AdminAdapter(dialog.getContext(), mAdmins);
                recyclerView.setAdapter(adminAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void addAdmin(Dialog dialog) {
        dialog.dismiss();

        PopupAddAdmin popupAddAdmin = new PopupAddAdmin();
        popupAddAdmin.showPopup(mActivity);
    }

}
