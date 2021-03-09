package com.example.logowanie_inzynierka2.Remote;

import com.example.logowanie_inzynierka2.Model.ApartmentViewModel;
import com.example.logowanie_inzynierka2.Model.LoginViewModel;
import com.example.logowanie_inzynierka2.Model.RegisterViewModel;
import com.example.logowanie_inzynierka2.Model.ResponseApartmentViewModel;
import com.example.logowanie_inzynierka2.Model.ResponseViewModel;
import com.example.logowanie_inzynierka2.Model.tblUser;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IMyAPI {

    //Authorization
    @POST("api/auth/register")
    Call<ResponseViewModel> registerUser(@Body RegisterViewModel registerViewModel);

    @POST("api/auth/login")
    Call<ResponseViewModel> loginUser(@Body LoginViewModel loginViewModel);

    @GET("api/auth/secret")
    Call<ResponseBody> getSecret(@Header("Authorization") String authToken);


    //Apartments
    @Multipart
    @POST("api/Apartments/saveapartment")
    Call<ResponseViewModel> AddApartment(@Header("Authorization") String authToken, @Part MultipartBody.Part file, @Part("Apartments")ApartmentViewModel apartmentViewModel);

    @GET("api/Apartments/getapartment")
    Call<List<ResponseApartmentViewModel>> GetApartments(@Header("Authorization") String authToken);


}
