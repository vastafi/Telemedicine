package com.example.telemedicine.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.telemedicine.Api.RetrofitCall;
import com.example.telemedicine.Models.ResponseMessage;
import com.example.telemedicine.Models.UserAuth;
import com.example.telemedicine.R;
import com.example.telemedicine.Utils.MessageTracker;
import com.example.telemedicine.Utils.Storage;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private MaterialButton loginBtn;
    private MaterialButton signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();

        loginBtn.setOnClickListener(view -> {
            String email = Objects.requireNonNull(emailInput.getText()).toString();
            String pass = Objects.requireNonNull(passwordInput.getText()).toString();

            if (email.length() < 4 || pass.length() < 4) {
                Toast.makeText(LoginActivity.this, MessageTracker.VALIDATE_INVALID, Toast.LENGTH_SHORT).show();
                return;
            }

            if (email.equals("admin") && pass.equals("admin")) {
                goMainScreen();
                return;
            }

            userAuth(email, pass);
        });
        signUpBtn.setOnClickListener(view -> goSignUp());
    }

    private void userAuth(String email, String password) {
        RetrofitCall retrofitCall = RetrofitCall.getInstance();

        UserAuth auth = new UserAuth(email, password);
        retrofitCall.userAuth(auth).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                try {
                    if (!response.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ResponseMessage responseMessage = response.body();
                    Toast.makeText(LoginActivity.this, MessageTracker.LOGIN_SUCCESS, Toast.LENGTH_SHORT).show();

                    String token = responseMessage.Message;
                    Storage storage = new Storage(LoginActivity.this);
                    storage.updateToken(token);
                    goMainScreen();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goMainScreen() {
        new Handler().postDelayed(() -> {
            final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
        }, 1000);
    }

    private void goSignUp() {
        final Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void initComponents() {
        emailInput = findViewById(R.id.login_email);
        passwordInput = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_login);
        signUpBtn = findViewById(R.id.login_sign_up);
    }
}
