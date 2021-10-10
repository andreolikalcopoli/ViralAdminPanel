package com.magma.viraladminpanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        prefSingleton.saveString("id", firebaseAuth.getUid());
                        utils.intent(MainActivity.class, null);
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
        return !etEmail.getText().toString().trim().equals("") && !etPassword.getText().toString().trim().equals("");
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