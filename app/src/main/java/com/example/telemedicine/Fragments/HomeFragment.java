package com.example.telemedicine.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.telemedicine.Api.RetrofitCall;
import com.example.telemedicine.Models.StorageToken;
import com.example.telemedicine.Models.UserConsult;
import com.example.telemedicine.Models.UserConsultRequest;
import com.example.telemedicine.R;
import com.example.telemedicine.Utils.MessageTracker;
import com.example.telemedicine.Utils.Storage;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private TextInputEditText nameInput;
    private TextInputEditText diseaseInput;
    private TextInputEditText addressInput;
    private TextInputEditText descriptionInput;

    private FragmentHandler fragmentHandler;

    public HomeFragment(FragmentHandler fragmentHandler) {
        this.fragmentHandler = fragmentHandler;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);

        nameInput = view.findViewById(R.id.consult_name);
        diseaseInput = view.findViewById(R.id.consult_disease);
        addressInput = view.findViewById(R.id.consult_address);
        descriptionInput = view.findViewById(R.id.consult_description);

        MaterialButton requestBtn = view.findViewById(R.id.consult_request);
        requestBtn.setOnClickListener(v -> requestConsultation(v.getContext()));

        return view;
    }

    private void requestConsultation(final Context context) {
        String name = Objects.requireNonNull(nameInput.getText()).toString();
        String disease = Objects.requireNonNull(diseaseInput.getText()).toString();
        String address = Objects.requireNonNull(addressInput.getText()).toString();
        String description = Objects.requireNonNull(descriptionInput.getText()).toString();

        if (name.length() < 5 || disease.length() < 3 || address.length() < 5) {
            Toast.makeText(getActivity(), MessageTracker.VALIDATE_INVALID, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            RetrofitCall retrofitCall = RetrofitCall.getInstance();
            String token = getStorageToken(context);
            UserConsultRequest request = new UserConsultRequest(name, disease, address, description);

            retrofitCall.addConsult(token, request).enqueue(new Callback<UserConsult>() {
                @Override
                public void onResponse(Call<UserConsult> call, Response<UserConsult> response) {
                    try {
                        if (!response.isSuccessful()) {
                            Toast.makeText(context, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        UserConsult userConsult = response.body();
                        Storage storage = new Storage(context);
                        storage.addConsult(userConsult);

                        Toast.makeText(context, MessageTracker.REQUEST_SUCCESS, Toast.LENGTH_SHORT).show();
                        fragmentHandler.loadHomeFragment();
                        fragmentHandler.getMainActivity().setNotificationBadge(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<UserConsult> call, Throwable t) {
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
