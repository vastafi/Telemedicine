package com.example.telemedicine.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedicine.Fragments.FragmentHandler;
import com.example.telemedicine.Models.DoctorModel;
import com.example.telemedicine.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private List<DoctorModel> mDoctorList;
    private FragmentHandler fh;

    public RecyclerViewAdapter(FragmentHandler fragmentHandler) {
        mDoctorList = new ArrayList<>();
        this.fh = fragmentHandler;
    }

    public void setList(List<DoctorModel> feedList) {
        mDoctorList = feedList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_doctor_item, parent, false);

        return new RecyclerViewHolder(v, fh);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.setItem(mDoctorList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDoctorList.size();
    }
}
