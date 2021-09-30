package com.magma.viraladminpanel.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.magma.viraladminpanel.LoginActivity;

public class Utils {
    private Context context;

    public Utils(Context context) {
        this.context = context;
    }

    public void displayMessage(String message) {
        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
    }

    public void intent(Class c, Bundle bundle) {
        Intent intent = new Intent(context, c);
        if (bundle != null)
            intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }
}
