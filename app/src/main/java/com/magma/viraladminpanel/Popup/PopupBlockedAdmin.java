package com.magma.viraladminpanel.Popup;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.Button;

import com.magma.viraladminpanel.R;

public class PopupBlockedAdmin {

    public void showPopup(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.blocked_admin_popup);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button buttonExit = (Button) dialog.findViewById(R.id.buttonExitBlockedAdminPopup);
        buttonExit.setOnClickListener(view -> {
            dialog.dismiss();
            activity.finishAffinity();
        });

        dialog.show();
    }
}
