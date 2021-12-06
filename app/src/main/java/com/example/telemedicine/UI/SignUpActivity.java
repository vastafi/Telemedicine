package com.example.telemedicine.UI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.telemedicine.Api.RetrofitCall;
import com.example.telemedicine.Models.UserRegister;
import com.example.telemedicine.R;
import com.example.telemedicine.Utils.ImageUtil;
import com.example.telemedicine.Utils.MessageTracker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private static final int GET_FROM_GALLERY = 101;

    private TextInputEditText fullNameInput;
    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput;
    private TextInputEditText birthdayInput;
    private TextInputEditText emailInput;
    private TextInputEditText phoneInput;
    private TextInputEditText addressInput;

    private CircleImageView userImage;
    private String base64Photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ImageButton backBtn = findViewById(R.id.sign_up_back);
        MaterialButton continueBtn = findViewById(R.id.sign_up_continue);
        fullNameInput = findViewById(R.id.sign_up_fullname);
        usernameInput = findViewById(R.id.sign_up_username);
        passwordInput = findViewById(R.id.sign_up_password);
        birthdayInput = findViewById(R.id.sign_up_birthday);
        emailInput = findViewById(R.id.sign_up_email);
        phoneInput = findViewById(R.id.sign_up_phone);
        addressInput = findViewById(R.id.sign_up_address);

        userImage = findViewById(R.id.sign_up_photo);
        userImage.setOnClickListener(view -> startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY));

        backBtn.setOnClickListener(view -> goBack());
        continueBtn.setOnClickListener(view -> userRegister());
    }

    public void userRegister() {
        String fullName = Objects.requireNonNull(fullNameInput.getText()).toString();
        String username = Objects.requireNonNull(usernameInput.getText()).toString();
        String password = Objects.requireNonNull(passwordInput.getText()).toString();
        String birthday = Objects.requireNonNull(birthdayInput.getText()).toString();
        String email = Objects.requireNonNull(emailInput.getText()).toString();
        String phone = Objects.requireNonNull(phoneInput.getText()).toString();
        String address = Objects.requireNonNull(addressInput.getText()).toString();

        if (fullName.length() < 8 ||
                username.length() < 5 ||
                password.length() < 5 ||
                birthday.length() < 5 ||
                email.length() < 5 ||
                phone.length() < 5 ||
                address.length() < 5 ||
                base64Photo.length() < 5) {

            Toast.makeText(this, MessageTracker.VALIDATE_INVALID, Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitCall retrofitCall = RetrofitCall.getInstance();
        UserRegister userRegister = new UserRegister(fullName, username, password, birthday, email, phone, address, base64Photo);

        retrofitCall.userRegister(userRegister).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(SignUpActivity.this, MessageTracker.REGISTER_SUCCESS, Toast.LENGTH_SHORT).show();
                goToLogin();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToLogin() {
        final Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void goBack() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                base64Photo = ImageUtil.convert(bitmap);
                userImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
