package com.magma.viraladminpanel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.magma.viraladminpanel.Utils.PrefSingleton;
import com.magma.viraladminpanel.Utils.Utils;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnContinue;

    private FirebaseAuth firebaseAuth;
    private Utils utils;
    private PrefSingleton prefSingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void listeners() {
        btnContinue.setOnClickListener(v -> {
            if (checkFields()) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        prefSingleton.saveString("id", firebaseAuth.getUid());
                        utils.intent(MainActivity.class, null);
                        finish();
                    } else {
                        utils.displayMessage("Authentication failed.");
                        etEmail.setText("");
                        etPassword.setText("");
                    }
                });
            } else utils.displayMessage("Please fill in all the fields");

        });
    }

    private boolean checkFields() {
        return !etEmail.getText().toString().trim().isEmpty() && !etPassword.getText().toString().isEmpty();
    }

    private void init() {
        etEmail = (EditText) findViewById(R.id.etEmailLogin);
        etPassword = (EditText) findViewById(R.id.etPassLogin);
        btnContinue = (Button) findViewById(R.id.btContinueLogin);

        firebaseAuth = FirebaseAuth.getInstance();
        utils = new Utils(this);
        PrefSingleton.getInstance().Initialize(this);
        prefSingleton = PrefSingleton.getInstance();

        listeners();
    }
}