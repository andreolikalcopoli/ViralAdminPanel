package com.magma.viraladminpanel.Popup;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Patterns;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.magma.viraladminpanel.Admin;
import com.magma.viraladminpanel.R;

import java.util.Random;

public class PopupAddAdmin {

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private Activity mActivity;

    public void showPopup(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_admin_popup);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mActivity = activity;

        firebaseAuth = FirebaseAuth.getInstance();

        Button buttonBack = (Button) dialog.findViewById(R.id.buttonBackAddAdminPopup);
        Button buttonInvite = (Button) dialog.findViewById(R.id.buttonInviteAddAdminPopUp);

        EditText editTextNewAdminEmail = (EditText) dialog.findViewById(R.id.editTextEmailAddressAddAdmin);

        Button buttonSelectedAdmin = (Button) dialog.findViewById(R.id.buttonSelectedAdmin);
        Button buttonSelectedHeadAdmin = (Button) dialog.findViewById(R.id.buttonSelectedHeadAdmin);

        Drawable blueBackground = activity.getDrawable(R.drawable.blue_button_background);
        Drawable withoutBackground = activity.getDrawable(R.drawable.color_transparent);

        buttonSelectedAdmin.setOnClickListener(view -> {
            buttonSelectedAdmin.setBackground(blueBackground);
            buttonSelectedHeadAdmin.setBackground(withoutBackground);

            buttonSelectedAdmin.setTextColor(Color.parseColor("#FFFFFF"));
            buttonSelectedHeadAdmin.setTextColor(Color.parseColor("#189AD3"));
        });

        buttonSelectedHeadAdmin.setOnClickListener(view -> {
            buttonSelectedHeadAdmin.setBackground(blueBackground);
            buttonSelectedAdmin.setBackground(withoutBackground);

            buttonSelectedHeadAdmin.setTextColor(Color.parseColor("#FFFFFF"));
            buttonSelectedAdmin.setTextColor(Color.parseColor("#189AD3"));
        });

        progressDialog = new ProgressDialog(dialog.getContext());
        progressDialog.setMessage("Inviting in progress, Please wait...");
        progressDialog.setCancelable(false);

        buttonBack.setOnClickListener(view -> dialog.dismiss());
        buttonInvite.setOnClickListener(view -> {
            final String email = editTextNewAdminEmail.getText().toString();
            if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if(buttonSelectedAdmin.getBackground() == blueBackground) inviteAdmin(dialog, email, "admin");
                else inviteAdmin(dialog, email, "head admin");
            }
            else editTextNewAdminEmail.setError("Enter valid Email address!");
        });

        dialog.show();
    }

    private void inviteAdmin(Dialog dialog, final String email, final String adminLevel) {
        progressDialog.show();

        final String password = randomPassword();

        addAdminToDatabase(dialog, email, password, adminLevel);
    }

    private String randomPassword() {
        final int min = 100000;
        final int max = 999999;
        final int password = new Random().nextInt((max - min) + 1) + min;

        return String.valueOf(password);
    }

    private void addAdminToDatabase(Dialog dialog, final String email, final String password, final String adminLevel) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) setAdmin(dialog, task.getResult().getUser(), adminLevel, email, password);
                    else {
                        progressDialog.dismiss();
                        dialog.dismiss();
                        Toast.makeText(dialog.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setAdmin(Dialog dialog, final FirebaseUser firebaseUser, final String adminLevel, final String email, final String password) {
        Admin admin = new Admin(firebaseUser.getUid(), firebaseUser.getEmail(), adminLevel);

        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference("admins");

        adminRef.child(firebaseUser.getUid()).setValue(admin)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    dialog.dismiss();
                    if(task.isSuccessful()) sendInvitation(email, password);
                    else Toast.makeText(dialog.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void sendInvitation(final String email, final String password) {
        String TO = email;
        String SUBJECT = "Admin Invite";
        String TEXT = "App link" + "\n\n" + "username: " + email + "\n" + "password: " + password;

        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { TO });
        intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
        intent.putExtra(Intent.EXTRA_TEXT, TEXT);

        intent.setType("message/rfc822");

        mActivity.startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    }

}
