package com.example.telemedicine.Api;

import com.example.telemedicine.Models.DoctorModel;
import com.example.telemedicine.Models.ResponseMessage;
import com.example.telemedicine.Models.UserAuth;
import com.example.telemedicine.Models.UserConsultRequest;
import com.example.telemedicine.Models.UserConsult;
import com.example.telemedicine.Models.UserProfile;
import com.example.telemedicine.Models.UserRegister;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CoreApi {

    @POST("api/Register/UserReg")
    Call<String> userRegister(@Body UserRegister userRegister);

    @POST("api/Login/UserAuth")
    Call<ResponseMessage> userAuth(@Body UserAuth userAuth);

    @POST("api/Doctor/AddConsultation")
    Call<UserConsult> addConsult(@Header("token") String token, @Body UserConsultRequest userConsult);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @GET("api/Profile/GetProfile")
    Call<UserProfile> getProfile(@Header("token") String token);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @GET("api/Doctor/GetDoctor/{id}")
    Call<DoctorModel> getDoctor(@Path("id") int doctorId, @Header("token") String token);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @GET("api/Doctor/GetDoctorList")
    Call<List<DoctorModel>> getDoctors(@Header("token") String token);
}
