package com.example.telemedicine.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.telemedicine.Api.RetrofitCall;
import com.example.telemedicine.Models.DoctorModel;
import com.example.telemedicine.Models.StorageToken;
import com.example.telemedicine.R;
import com.example.telemedicine.Utils.ImageUtil;
import com.example.telemedicine.Utils.Storage;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorDetailsFragment extends Fragment {

    private int docId;
    private DoctorModel doctor;

    private CircleImageView photo;
    private TextView fullName;
    private TextView rating;
    private TextView specs;
    private TextView about;
    private TextView address;

    public DoctorDetailsFragment(int id) {
        docId = id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_details, null);

        photo = view.findViewById(R.id.doc_photo);
        fullName = view.findViewById(R.id.doc_name);
        rating = view.findViewById(R.id.doc_rating);
        specs = view.findViewById(R.id.doc_specs);
        about = view.findViewById(R.id.doc_about);
        address = view.findViewById(R.id.doc_address);

        getDoctorDetails(view.getContext());

        return view;
    }

    private void getDoctorDetails(final Context context) {
        try {
            String token = getStorageToken(context);

            RetrofitCall retrofitCall = RetrofitCall.getInstance();
            retrofitCall.getDoctor(docId, token).enqueue(new Callback<DoctorModel>() {
                @Override
                public void onResponse(Call<DoctorModel> call, Response<DoctorModel> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(context, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    doctor = response.body();

                    Bitmap decoded = ImageUtil.convert(doctor.getPhoto());
                    photo.setImageBitmap(decoded);
                    fullName.setText(doctor.getFullName());
                    rating.setText(String.valueOf(doctor.getStars()));
                    specs.setText(doctor.getSpecs());
                    about.setText(doctor.getAbout());
                    address.setText(doctor.getAddress());
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
