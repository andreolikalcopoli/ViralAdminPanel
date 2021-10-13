package com.magma.viraladminpanel.Popup;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magma.viraladminpanel.R;
import com.magma.viraladminpanel.ReportType;

import java.util.Map;

public class PopupPostReports {

    private Activity mActivity;

    public void showPopup(Activity activity, Map<String, String> reports, String postId) {
        mActivity = activity;

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.post_reports_popup);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button buttonBack = (Button) dialog.findViewById(R.id.buttonBackPostReportsPopup);
        Button buttonRemove = (Button) dialog.findViewById(R.id.buttonRemovePostReportsPopUp);

        buttonBack.setOnClickListener(view -> dialog.dismiss());
        buttonRemove.setOnClickListener(view -> removePost(dialog, postId));

        LinearLayout linearLayoutHateSpeech = (LinearLayout) dialog.findViewById(R.id.post_reports_hate_speech);
        LinearLayout linearLayoutPornography = (LinearLayout) dialog.findViewById(R.id.post_reports_pornography);
        LinearLayout linearLayoutSpam = (LinearLayout) dialog.findViewById(R.id.post_reports_spam);
        LinearLayout linearLayoutViolenceOrDanger = (LinearLayout) dialog.findViewById(R.id.post_reports_violence_or_danger);
        LinearLayout linearLayoutFalseInformation = (LinearLayout) dialog.findViewById(R.id.post_reports_false_information);

        TextView textViewHateSpeech = (TextView) dialog.findViewById(R.id.post_reports_hate_speech_number);
        TextView textViewPornography = (TextView) dialog.findViewById(R.id.post_reports_pornography_number);
        TextView textViewSpam = (TextView) dialog.findViewById(R.id.post_reports_spam_number);
        TextView textViewViolenceOrDanger = (TextView) dialog.findViewById(R.id.post_reports_violence_or_danger_number);
        TextView textViewFalseInformation = (TextView) dialog.findViewById(R.id.post_reports_false_information_number);

        int br_hate_speech = 0;
        int br_pornography = 0;
        int br_spam = 0;
        int br_violence_or_danger = 0;
        int br_false_information = 0;

        for(String report : reports.values()) {
            switch (report) {
                case ReportType.HATE_SPEECH:
                    br_hate_speech++;
                    break;
                case ReportType.PORNOGRAPHY:
                    br_pornography++;
                    break;
                case ReportType.SPAM:
                    br_spam++;
                    break;
                case ReportType.VIOLENCE_OR_DANGER:
                    br_violence_or_danger++;
                    break;
                case ReportType.FALSE_INFORMATION:
                    br_false_information++;
                    break;
            }
        }

        if(br_hate_speech > 0) {
            linearLayoutHateSpeech.setVisibility(View.VISIBLE);
            textViewHateSpeech.setText(String.valueOf(br_hate_speech));
        }

        if(br_pornography > 0) {
            linearLayoutPornography.setVisibility(View.VISIBLE);
            textViewPornography.setText(String.valueOf(br_pornography));
        }

        if(br_spam > 0) {
            linearLayoutSpam.setVisibility(View.VISIBLE);
            textViewSpam.setText(String.valueOf(br_spam));
        }

        if(br_violence_or_danger > 0) {
            linearLayoutViolenceOrDanger.setVisibility(View.VISIBLE);
            textViewViolenceOrDanger.setText(String.valueOf(br_violence_or_danger));
        }

        if(br_false_information > 0) {
            linearLayoutFalseInformation.setVisibility(View.VISIBLE);
            textViewFalseInformation.setText(String.valueOf(br_false_information));
        }

        dialog.show();
    }

    private void removePost(Dialog dialog, String postId) {
        dialog.dismiss();

        PopupRemovePost popupRemovePost = new PopupRemovePost();
        popupRemovePost.showPopup(mActivity, postId);
    }

}
