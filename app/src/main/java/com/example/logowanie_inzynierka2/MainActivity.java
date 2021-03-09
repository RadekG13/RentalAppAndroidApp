package com.example.logowanie_inzynierka2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.logowanie_inzynierka2.Model.LoginViewModel;
import com.example.logowanie_inzynierka2.Model.ResponseViewModel;
import com.example.logowanie_inzynierka2.Model.tblUser;
import com.example.logowanie_inzynierka2.Remote.IMyAPI;
import com.example.logowanie_inzynierka2.Remote.RetrofitClient;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {


    IMyAPI iMyAPI;
    EditText edt_user, edt_password;
    Button btn_login;
    TextView txt_register, txt_Help;
    RadioGroup radioGroup;
    RadioButton radioButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //// Resize when keyboard is shown
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ////
        edt_user=(EditText) findViewById(R.id.edt_user_name);
        edt_password=(EditText) findViewById(R.id.edt_types_of_repair);
        btn_login=(Button) findViewById(R.id.bt_search);
        txt_register=(TextView) findViewById(R.id.tv_register);
        txt_Help=(TextView) findViewById(R.id.tV_Help);

        /////
        iMyAPI = RetrofitClient.getInstance().create(IMyAPI.class);
        ///

        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });
        //// Pomocnik - usunąć później!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        txt_Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuView();
            }
        });



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new SpotsDialog.Builder()
                        .setContext(MainActivity.this)
                        .build();
                dialog.show();




                login();

                dialog.dismiss();

            }
        });

    }


    private void login() {
        LoginViewModel user=new LoginViewModel(edt_user.getText().toString(),
                edt_password.getText().toString());
        Call<ResponseViewModel> call= iMyAPI.loginUser(user);
        call.enqueue(new Callback<ResponseViewModel>() {
            @Override
            public void onResponse(Call<ResponseViewModel> call, Response<ResponseViewModel> response) {
    String token;
                if (response.isSuccessful())
                {
                    Toast.makeText(MainActivity.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    token = response.body().getMessage();
                    SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                    preferences.edit().putString("TOKEN",token).apply();

                    openMenuView();
                }
                else
                {
                    try {
                        String body = response.errorBody().string(); //Raw json
                        Toast.makeText(MainActivity.this,body, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }



            }

            @Override
            public void onFailure(Call<ResponseViewModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    public void openMenuView(){
        radioGroup = findViewById(R.id.rg_1);
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);


        if(radioButton.getText().toString().equals("Zarządca")){
        Intent intent = new Intent(this, MenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
            finish();}
        if(radioButton.getText().toString().equals("Najemca")){
           Intent intent = new Intent(this, MenuTenantActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           startActivity(intent);
            finish();}


        }


}