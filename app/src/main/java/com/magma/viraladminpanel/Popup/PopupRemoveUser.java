package com.magma.viraladminpanel.Popup;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.magma.viraladminpanel.BlockedUser;
import com.magma.viraladminpanel.R;
import com.magma.viraladminpanel.User;

public class PopupRemoveUser {

    public void showPopup(Activity activity, User user) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.remove_user_popup);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView userName = (TextView) dialog.findViewById(R.id.userNameRemoveUserPopup);

        userName.setText(user.getFirstName() + " " + user.getLastName());

        Button buttonBack = (Button) dialog.findViewById(R.id.buttonBackRemoveUserPopup);
        Button buttonRemove = (Button) dialog.findViewById(R.id.buttonRemoveUserPopUp);

        buttonBack.setOnClickListener(view -> dialog.dismiss());
        buttonRemove.setOnClickListener(view -> removeUser(dialog, user.getUserId()));

        dialog.show();
    }

    private void removeUser(Dialog dialog, String userId) {
        BlockedUser blockedUser = new BlockedUser();
        blockedUser.setUserId(userId);

        ProgressDialog progressDialog = new ProgressDialog(dialog.getContext());
        progressDialog.setMessage("Removing user...");
        progressDialog.show();

        DatabaseReference removeRef = FirebaseDatabase.getInstance().getReference();

        removeRef.child("blocked_users").child(userId).setValue(blockedUser)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    dialog.dismiss();
                    if(task.isSuccessful()) Toast.makeText(dialog.getContext(), "Successfully removed user", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(dialog.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
