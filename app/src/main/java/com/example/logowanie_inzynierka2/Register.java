package com.example.logowanie_inzynierka2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.logowanie_inzynierka2.Model.RegisterViewModel;
import com.example.logowanie_inzynierka2.Model.ResponseViewModel;
import com.example.logowanie_inzynierka2.Model.tblUser;
import com.example.logowanie_inzynierka2.Remote.IMyAPI;
import com.example.logowanie_inzynierka2.Remote.RetrofitClient;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Register extends AppCompatActivity {




    EditText edt_user, edt_password, edt_phone, edt_name, edt_account;
    Button btn_login;


    IMyAPI  iMyAPI = RetrofitClient.getInstance().create(IMyAPI.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE); //resize when keyboard on

        ////
        edt_user=(EditText) findViewById(R.id.edt_user_name);
        edt_password=(EditText) findViewById(R.id.edt_types_of_repair);
        btn_login=(Button) findViewById(R.id.bt_search);
        edt_phone=(EditText) findViewById(R.id.edt_telephone);
        edt_name=(EditText) findViewById(R.id.edt_name_and_surname);
        edt_account=(EditText) findViewById(R.id.edt_bank_account);
        /////






btn_login.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        final AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(Register.this)
                .build();

        dialog.show();

      // RegisterViewModel user=new RegisterViewModel(edt_user.getText().toString(),
        //        edt_password.getText().toString(),  edt_password.getText().toString(),"51515","ggege", "156516"); //Add separate password confirmation


        register();

        dialog.dismiss();




    }});}

    private void register(){
        RegisterViewModel registerViewModel = new RegisterViewModel(edt_user.getText().toString(),
                edt_password.getText().toString(),  edt_password.getText().toString(), edt_phone.getText().toString(),edt_name.getText().toString(), edt_account.getText().toString());
       Call<ResponseViewModel> call= iMyAPI.registerUser(registerViewModel);
       call.enqueue(new Callback<ResponseViewModel>() {
           @RequiresApi(api = Build.VERSION_CODES.O)
           @Override
           public void onResponse(Call<ResponseViewModel> call, Response<ResponseViewModel> response)
           {

               if (response.isSuccessful())
               {
                   Toast.makeText(Register.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();
               }
               else
               {
                   try {
                       String body = response.errorBody().string(); //Raw json
                       Toast.makeText(Register.this,body, Toast.LENGTH_LONG).show();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }

               }
           }

           @Override
           public void onFailure(Call<ResponseViewModel> call, Throwable t)
           {
               Toast.makeText(Register.this, t.toString(), Toast.LENGTH_SHORT).show();
           }
       });

    }

/*
    String token2= "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJFbWFpbCI6InRlc3QzN0ByYWRlay5uZXQiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6ImY3NDJmNzczLWY5YmUtNGRjYi04N2NkLWY4OWQxYWZlOWZlMiIsImV4cCI6MTYxMjU2MjYzN30.TaY0I6pN_EYWicyODkQElWZJl93bqDDrz184sQx1DTE";
    private void getSecret(){
        Call<ResponseBody> call = iMyAPI.getSecret(token2);

    call.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {


                try {


                    Toast.makeText(Register.this, response.body().string(), Toast.LENGTH_SHORT).show();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(Register.this, "obsluz to", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Toast.makeText(Register.this, t.toString(), Toast.LENGTH_SHORT).show();
        }
    });
    }*/

    @Override
    protected void onStop() {
       super.onStop();
    }




}