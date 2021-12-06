package com.example.telemedicine.Api;

import com.example.telemedicine.Models.DoctorModel;
import com.example.telemedicine.Models.ResponseMessage;
import com.example.telemedicine.Models.UserAuth;
import com.example.telemedicine.Models.UserConsultRequest;
import com.example.telemedicine.Models.UserConsult;
import com.example.telemedicine.Models.UserProfile;
import com.example.telemedicine.Models.UserRegister;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCall {

    public static final String API_BASE_URL = "http://81.180.72.17/";

    private static RetrofitCall retrofitInstance;
    private static Retrofit retrofit;

    public RetrofitCall() {
    }

    public static RetrofitCall getInstance() {
        if (retrofitInstance == null) {
            retrofitInstance = new RetrofitCall();
        }
        return retrofitInstance;
    }

    private Retrofit getRetrofit() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .readTimeout(15, TimeUnit.SECONDS)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(false)
                    .addInterceptor(interceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
        }

        return retrofit;
    }

    public Call<String> userRegister(UserRegister register) {
        CoreApi call = getRetrofit().create(CoreApi.class);

        return call.userRegister(register);
    }

    public Call<ResponseMessage> userAuth(UserAuth auth) {
        CoreApi call = getRetrofit().create(CoreApi.class);

        return call.userAuth(auth);
    }

    public Call<UserConsult> addConsult(String token, UserConsultRequest consult) {
        CoreApi call = getRetrofit().create(CoreApi.class);

        return call.addConsult(token, consult);
    }

    public Call<UserProfile> getProfile(String token) {
        CoreApi call = getRetrofit().create(CoreApi.class);

        return call.getProfile(token);
    }

    public Call<List<DoctorModel>> getDoctors(String token) {
        CoreApi call = getRetrofit().create(CoreApi.class);

        return call.getDoctors(token);
    }

    public Call<DoctorModel> getDoctor(int id, String token) {
        CoreApi call = getRetrofit().create(CoreApi.class);

        return call.getDoctor(id, token);
    }
}
