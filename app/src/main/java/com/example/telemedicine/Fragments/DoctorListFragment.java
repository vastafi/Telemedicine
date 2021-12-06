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
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedicine.Api.RetrofitCall;
import com.example.telemedicine.Models.DoctorModel;
import com.example.telemedicine.Models.StorageToken;
import com.example.telemedicine.R;
import com.example.telemedicine.Utils.RecyclerViewAdapter;
import com.example.telemedicine.Utils.Storage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerAdapter;
    private List<DoctorModel> doctorList;

    private FragmentHandler fragmentHandler;

    public DoctorListFragment(FragmentHandler fragmentHandler) {
        this.fragmentHandler = fragmentHandler;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_doctor_list, null);

        mRecyclerAdapter = new RecyclerViewAdapter(fragmentHandler);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        // Add some values
        getDoctors(view.getContext());

        return view;
    }

    private void getDoctors(final Context context) {
        try {
            String token = getStorageToken(context);

            RetrofitCall retrofitCall = RetrofitCall.getInstance();
            retrofitCall.getDoctors(token).enqueue(new Callback<List<DoctorModel>>() {
                @Override
                public void onResponse(Call<List<DoctorModel>> call, Response<List<DoctorModel>> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(context, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    doctorList = response.body();
                    mRecyclerAdapter.setList(doctorList);
                    mRecyclerView.setAdapter(mRecyclerAdapter);
                }

                @Override
                public void onFailure(Call<List<DoctorModel>> call, Throwable t) {
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
