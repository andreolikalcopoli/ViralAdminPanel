package com.magma.viraladminpanel.Popup;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.magma.viraladminpanel.BlockedUser;
import com.magma.viraladminpanel.DateTime;
import com.magma.viraladminpanel.R;
import com.magma.viraladminpanel.ReportType;
import com.magma.viraladminpanel.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PopupBlockUser {

    private final String[] duration = {"for a month"};
    private final String[] reason = {ReportType.HATE_SPEECH, ReportType.PORNOGRAPHY, ReportType.SPAM, ReportType.VIOLENCE_OR_DANGER, ReportType.FALSE_INFORMATION};

    public void showPopup(Activity activity, User user) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.block_user_popup);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView userName = (TextView) dialog.findViewById(R.id.userNameBlockUserPopup);
        userName.setText(user.getFirstName() + " " + user.getLastName());

        Button buttonBack = (Button) dialog.findViewById(R.id.buttonBackBlockUserPopup);
        Button buttonBlock = (Button) dialog.findViewById(R.id.buttonBlockUserPopUp);

        Spinner spinnerDuration = (Spinner) dialog.findViewById(R.id.spinnerBlockUserDurationPopup);
        Spinner spinnerReason = (Spinner) dialog.findViewById(R.id.spinnerBlockUserReasonPopup);

        ArrayAdapter arrayAdapterDuration = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, duration);
        arrayAdapterDuration.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDuration.setAdapter(arrayAdapterDuration);

        ArrayAdapter arrayAdapterReason = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, reason);
        arrayAdapterReason.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReason.setAdapter(arrayAdapterReason);

        buttonBack.setOnClickListener(view -> dialog.dismiss());

        buttonBlock.setOnClickListener(view -> {
            String blockDuration = spinnerDuration.getSelectedItem().toString();
            String reasonForBlocking = spinnerReason.getSelectedItem().toString();

            DateTime durationTo = getDurationTo(blockDuration);

            blockUser(dialog, user.getUserId(), durationTo, reasonForBlocking);
        });

        dialog.show();
    }

    private DateTime getDurationTo(String blockDuration) {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormatD = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat dateFormatT = new SimpleDateFormat("HH:mm");

        String date = dateFormatD.format(calendar.getTime());
        String time = dateFormatT.format(calendar.getTime());

        DateTime currentTime = new DateTime(date, time);

        return currentTime;
    }

    private void blockUser(Dialog dialog, String userId, DateTime durationTo, String reasonForBlocking) {
        BlockedUser blockedUser = new BlockedUser(userId, durationTo, reasonForBlocking);

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
