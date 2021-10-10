package com.magma.viraladminpanel.Popup;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.magma.viraladminpanel.R;
import com.magma.viraladminpanel.User;

public class PopupRemovePost {

    public void showPopup(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.remove_post_popup);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button buttonBack = (Button) dialog.findViewById(R.id.buttonBackRemovePostPopup);
        Button buttonRemove = (Button) dialog.findViewById(R.id.buttonRemovePostPopUp);

        buttonBack.setOnClickListener(view -> dialog.dismiss());
        buttonRemove.setOnClickListener(view -> removePost(dialog));

        dialog.show();
    }

    private void removePost(Dialog dialog) {

    }

}
