package com.example.logowanie_inzynierka2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {


    Button btn_editUser, btn_deleteUser, btn_logoutUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ////
        btn_editUser=(Button) findViewById(R.id.btn_edit_user);
        btn_deleteUser=(Button) findViewById(R.id.btn_delete_user);
        btn_logoutUser=(Button) findViewById(R.id.btn_logout);
        ////


        btn_logoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        btn_editUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        btn_deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });
    }

    private void deleteUser() {


    }

    private void logout() {
        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();

        Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
        startActivity(intent);


    }
}