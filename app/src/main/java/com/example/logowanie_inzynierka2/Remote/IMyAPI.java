package com.example.logowanie_inzynierka2.Remote;

import com.example.logowanie_inzynierka2.Model.ApartmentViewModel;
import com.example.logowanie_inzynierka2.Model.LoginViewModel;
import com.example.logowanie_inzynierka2.Model.RegisterViewModel;
import com.example.logowanie_inzynierka2.Model.ResponseApartmentViewModel;
import com.example.logowanie_inzynierka2.Model.ResponseViewModel;
import com.example.logowanie_inzynierka2.Model.RoomViewModel;
import com.example.logowanie_inzynierka2.Model.tblUser;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

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
    @POST("api/Apartments")
    Call<ResponseViewModel> AddApartment(@Header("Authorization") String authToken, @Part MultipartBody.Part file, @Part("Apartments")ApartmentViewModel apartmentViewModel);

    @GET("api/Apartments")
    Call<List<ResponseApartmentViewModel>> GetApartments(@Header("Authorization") String authToken);

    @DELETE("api/Apartments/{id}")
    Call<ResponseViewModel> DeleteApartment(@Header("Authorization") String authToken, @Path("id") String id);

    @Multipart
    @PUT("api/Apartments/{id}")
    Call<ResponseViewModel> UpdateApartment(@Header("Authorization") String authToken, @Part MultipartBody.Part file, @Part("Apartments")ApartmentViewModel apartmentViewModel, @Path("id") String id);

    @GET("api/Apartments/{id}")
    Call<ResponseApartmentViewModel> GetOneApartment(@Header("Authorization") String authToken, @Path("id") String id );

    //Rooms
    @Multipart
    @POST("api/Apartments/{id}/rooms")
    Call<ResponseViewModel> AddRoom(@Header("Authorization") String authToken, @Part MultipartBody.Part file, @Part("Rooms") RoomViewModel roomViewModel, @Path("id") String id);

    @GET("api/Apartments/{id}/rooms/{roomId}")
    Call<RoomViewModel> GetOneRoom(@Header("Authorization") String authToken,  @Path("id") String id, @Path("roomId") String roomId);

    @GET("api/Apartments/{id}/rooms")
    Call<List<RoomViewModel>> GetRooms(@Header("Authorization") String authToken, @Path("id") String id);

    @Multipart
    @PUT("api/Apartments/{id}/rooms/{roomId}")
    Call<ResponseViewModel> EditRoom(@Header("Authorization") String authToken, @Part MultipartBody.Part file, @Part("Rooms") RoomViewModel roomViewModel, @Path("id") String id,@Path("roomId") String roomId);

    @DELETE("api/Apartments/{id}/rooms/{roomId}")
    Call<ResponseViewModel> DeleteRoom(@Header("Authorization") String authToken, @Path("id") String id, @Path("roomId") String roomId);

}
