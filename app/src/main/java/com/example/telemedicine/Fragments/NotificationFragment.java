package com.example.telemedicine.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.telemedicine.Api.RetrofitCall;
import com.example.telemedicine.Models.DoctorModel;
import com.example.telemedicine.Models.StorageToken;
import com.example.telemedicine.Models.UserConsult;
import com.example.telemedicine.R;
import com.example.telemedicine.Utils.ImageUtil;
import com.example.telemedicine.Utils.MessageTracker;
import com.example.telemedicine.Utils.Storage;
import com.google.android.material.button.MaterialButton;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment {

    private TextView noNotificationText;
    private ScrollView notificationView;

    private TextView nameText;
    private TextView diseaseText;
    private TextView addressText;
    private TextView descriptionText;
    private CircleImageView docPhotoView;
    private TextView docNameText;
    private TextView docSpecsText;
    private TextView docRatingText;

    private UserConsult userConsult;
    private boolean newNotification = false;

    private FragmentHandler fragmentHandler;

    public NotificationFragment(FragmentHandler fragmentHandler) {
        this.fragmentHandler = fragmentHandler;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, null);

        noNotificationText = view.findViewById(R.id.notification_no_notification);
        notificationView = view.findViewById(R.id.notification_view);

        nameText = view.findViewById(R.id.notification_name);
        diseaseText = view.findViewById(R.id.notification_disease);
        addressText = view.findViewById(R.id.notification_address);
        descriptionText = view.findViewById(R.id.notification_description);
        docPhotoView = view.findViewById(R.id.notification_doc_photo);
        docNameText = view.findViewById(R.id.notification_doc_name);
        docSpecsText = view.findViewById(R.id.notification_doc_specs);
        docRatingText = view.findViewById(R.id.notification_doc_rating);

        MaterialButton confirmBtn = view.findViewById(R.id.notification_confirm);
        MaterialButton cancelBtn = view.findViewById(R.id.notification_cancel);

        setConsultIfAny(view.getContext());

        confirmBtn.setOnClickListener(v -> confirmConsultation(view.getContext()));
        cancelBtn.setOnClickListener(v -> cancelConsultation(view.getContext()));

        return view;
    }

    private void confirmConsultation(Context context) {
        if (newNotification) {
            try {
                Storage storage = new Storage(context);
                storage.updateConsultConfirmation(userConsult);
                Toast.makeText(context, MessageTracker.CONSULT_CONFIRM, Toast.LENGTH_SHORT).show();
                fragmentHandler.getMainActivity().openHomeFragment();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void cancelConsultation(Context context) {
        if (newNotification) {
            try {
                Storage storage = new Storage(context);
                storage.removeConsult(userConsult.getConsId());
                Toast.makeText(context, MessageTracker.CONSULT_CANCEL, Toast.LENGTH_SHORT).show();
                fragmentHandler.getMainActivity().openHomeFragment();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setConsultIfAny(Context context) {
        try {
            Storage storage = new Storage(context);
            userConsult = storage.getLastUnconfirmedConsult();
            if (userConsult == null) {
                noNotificationText.setVisibility(View.VISIBLE);
                notificationView.setVisibility(View.GONE);
                newNotification = false;
                return;
            }

            RetrofitCall retrofitCall = RetrofitCall.getInstance();
            retrofitCall.getDoctor(userConsult.getDocId(), getStorageToken(context)).enqueue(new Callback<DoctorModel>() {
                @Override
                public void onResponse(Call<DoctorModel> call, Response<DoctorModel> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(context, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    DoctorModel doctor = response.body();
                    if (doctor == null) {
                        return;
                    }

                    notificationView.setVisibility(View.GONE);
                    notificationView.setVisibility(View.VISIBLE);

                    nameText.setText(userConsult.getName());
                    diseaseText.setText(userConsult.getDisease());
                    addressText.setText(userConsult.getAddress());
                    descriptionText.setText(userConsult.getDescription());

                    docPhotoView.setImageBitmap(ImageUtil.convert(doctor.getPhoto()));
                    docNameText.setText(doctor.getFullName());
                    docSpecsText.setText(doctor.getSpecs());
                    docRatingText.setText(String.valueOf(doctor.getStars()));
                    newNotification = true;
                    fragmentHandler.getMainActivity().setNotificationBadge(false);
                }

                @Override
                public void onFailure(Call<DoctorModel> call, Throwable t) {
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
