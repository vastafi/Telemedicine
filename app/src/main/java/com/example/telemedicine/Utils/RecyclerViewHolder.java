package com.example.telemedicine.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedicine.Fragments.FragmentHandler;
import com.example.telemedicine.Models.DoctorModel;
import com.example.telemedicine.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public DoctorModel model;

    private LinearLayout layout;
    private CircleImageView photo;
    private TextView fullName;
    private TextView rating;
    private TextView specs;
    private TextView address;

    private FragmentHandler fragmentHandler;

    public RecyclerViewHolder(@NonNull View itemView, FragmentHandler fragmentHandler) {
        super(itemView);

        layout = itemView.findViewById(R.id.doctor_item);
        layout.setOnClickListener(this);

        photo = itemView.findViewById(R.id.doctor_photo);
        fullName = itemView.findViewById(R.id.doctor_name);
        rating = itemView.findViewById(R.id.doctor_rating);
        specs = itemView.findViewById(R.id.doctor_specs);
        address = itemView.findViewById(R.id.doctor_address);
        this.fragmentHandler = fragmentHandler;
    }

    public void setItem(@NonNull DoctorModel model) {
        fullName.setText(model.getFullName());
        rating.setText(String.valueOf(model.getStars()));
        specs.setText(model.getSpecs());
        address.setText(model.getAddress());

        byte[] decodedString = Base64.decode(model.getPhoto(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        photo.setImageBitmap(decodedByte);

        this.model = model;
    }

    @Override
    public void onClick(View view) {
        fragmentHandler.getMainActivity().setToolbarTitle("Doctor Details");
        fragmentHandler.loadDocDetailsFragment(model.getDocId());
    }
}
