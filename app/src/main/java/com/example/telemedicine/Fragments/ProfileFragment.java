package com.example.telemedicine.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.telemedicine.Api.RetrofitCall;
import com.example.telemedicine.Models.StorageToken;
import com.example.telemedicine.Models.UserProfile;
import com.example.telemedicine.R;
import com.example.telemedicine.Utils.DateUtil;
import com.example.telemedicine.Utils.ImageUtil;
import com.example.telemedicine.Utils.MessageTracker;
import com.example.telemedicine.Utils.Storage;
import com.google.android.material.button.MaterialButton;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private CircleImageView photo;
    private TextView fullName;
    private TextView username;
    private TextView email;
    private TextView phone;
    private TextView address;
    private TextView birthday;

    private FragmentHandler fragmentHandler;

    public ProfileFragment(FragmentHandler fragmentHandler) {
        this.fragmentHandler = fragmentHandler;
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        photo = view.findViewById(R.id.profile_photo);
        fullName = view.findViewById(R.id.profile_name);
        username = view.findViewById(R.id.profile_username);
        email = view.findViewById(R.id.profile_email);
        phone = view.findViewById(R.id.profile_phone);
        address = view.findViewById(R.id.profile_address);
        birthday = view.findViewById(R.id.profile_birthday);

        getUserData(view.getContext());

        MaterialButton logoutBtn = view.findViewById(R.id.profile_logout);
        logoutBtn.setOnClickListener(v -> {
            try {
                Storage storage = new Storage(v.getContext());
                storage.deleteToken();
                Toast.makeText(v.getContext(), MessageTracker.LOGOUT_SUCCESS, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Handler().postDelayed(() -> fragmentHandler.getMainActivity().logoutIntent(), 1000);
        });

        return view;
    }

    private void getUserData(Context context) {
        try {
            String token = getStorageToken(context);

            RetrofitCall retrofitCall = RetrofitCall.getInstance();
            retrofitCall.getProfile(token).enqueue(new Callback<UserProfile>() {
                @Override
                public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(context, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    UserProfile user = response.body();
                    photo.setImageBitmap(ImageUtil.convert(user.getBase64Photo()));
                    fullName.setText(user.getFullName());
                    username.setText(user.getUsername());
                    email.setText(user.getEmail());
                    phone.setText(user.getPhone());
                    address.setText(user.getAddress());

                    birthday.setText(DateUtil.getSimpleDateFormat(user.getBirthday()));
                }

                @Override
                public void onFailure(Call<UserProfile> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getStorageToken(Context context) throws Exception {
        Storage storage = new Storage(context);
        StorageToken storageToken = storage.getUserStorage().getStorageToken();
        return storageToken.getToken();
    }
}
