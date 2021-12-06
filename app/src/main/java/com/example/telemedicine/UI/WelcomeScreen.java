package com.example.telemedicine.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.telemedicine.Models.StorageToken;
import com.example.telemedicine.R;
import com.example.telemedicine.Utils.DateUtil;
import com.example.telemedicine.Utils.Storage;
import com.google.android.material.button.MaterialButton;

import java.util.Date;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // If date is expired then clear token and login again
        checkIfNotExpiredToken();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        MaterialButton signUpBtn = findViewById(R.id.welcome_sign_up);
        MaterialButton loginBtn = findViewById(R.id.welcome_login);

        signUpBtn.setOnClickListener(view -> goSignUp());
        loginBtn.setOnClickListener(view -> goLogin());
    }

    private void checkIfNotExpiredToken() {
        Storage storage = new Storage(this);
        if (storage.validateToken()) {
            authMain();
        }
    }

    private void goSignUp() {
        final Intent mainIntent = new Intent(WelcomeScreen.this, SignUpActivity.class);
        startActivity(mainIntent);
    }

    private void goLogin() {
        final Intent mainIntent = new Intent(WelcomeScreen.this, LoginActivity.class);
        startActivity(mainIntent);
    }

    private void authMain() {
        final Intent intent = new Intent(WelcomeScreen.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        finish();
    }
}
