package com.example.telemedicine.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.telemedicine.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() -> {
            final Intent mainIntent = new Intent(SplashScreen.this, WelcomeScreen.class);
            startActivity(mainIntent);
            finish();
        }, 3000);
    }
}
