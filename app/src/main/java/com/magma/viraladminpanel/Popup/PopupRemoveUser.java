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
        buttonRemove.setOnClickListener(view -> removeUser(dialog));

        dialog.show();
    }

    private void removeUser(Dialog dialog) {
        dialog.dismiss();
    }

}
