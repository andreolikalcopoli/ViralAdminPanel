package com.magma.viraladminpanel.Popup;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.Button;

import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.magma.viraladminpanel.R;

public class PopupRemovePost {

    public void showPopup(Activity activity, String postId) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.remove_post_popup);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button buttonBack = (Button) dialog.findViewById(R.id.buttonBackRemovePostPopup);
        Button buttonRemove = (Button) dialog.findViewById(R.id.buttonRemovePostPopUp);

        buttonBack.setOnClickListener(view -> dialog.dismiss());
        buttonRemove.setOnClickListener(view -> removePost(dialog, postId));

        dialog.show();
    }

    private void removePost(Dialog dialog, String postId) {
        ProgressDialog progressDialog = new ProgressDialog(dialog.getContext());
        progressDialog.setMessage("Removing post in progress...");
        progressDialog.show();

        DatabaseReference removeRef = FirebaseDatabase.getInstance().getReference();

        removeRef.child("likes").child(postId).removeValue();
        removeRef.child("comments").child(postId).removeValue();
        removeRef.child("admin_panel").child("posts").child(postId).removeValue();

        removeRef.child("posts").child(postId).removeValue()
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    dialog.dismiss();

                    if(task.isSuccessful()) Toast.makeText(dialog.getContext(), "Successfully deleted post", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(dialog.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
