package com.example.logowanie_inzynierka2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.logowanie_inzynierka2.Model.Notification;
import com.example.logowanie_inzynierka2.Remote.IMyAPI;
import com.example.logowanie_inzynierka2.Remote.RetrofitClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {
    GridLayout mainGrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mainGrid = (GridLayout) findViewById(R.id.grid_menu);
        setSingleEvent(mainGrid);
    }

    private void setSingleEvent(GridLayout mainGrid) {
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(finalI==0){ ///albo switch!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    Intent intent = new Intent(MenuActivity.this,ApartmentsActivity.class);
                    intent.putExtra("info","This is activity from card item index  "+finalI);
                    startActivity(intent);}

                    if(finalI==4){
                        Intent intent = new Intent(MenuActivity.this,RepairActivity.class);
                        startActivity(intent);
                    }
                    if(finalI==2){
                       getSecret();
                    }

                    if(finalI==5){
                        Intent intent = new Intent(MenuActivity.this,SettingsActivity.class);
                        startActivity(intent);
                    }

                }
            });
        }

    }

    IMyAPI iMyAPI= RetrofitClient.getInstance().create(IMyAPI.class);;


    private void getSecret() {

        SharedPreferences preferences = getSharedPreferences("MY_APP",Context.MODE_PRIVATE);
        String retrievedToken  = preferences.getString("TOKEN",null);//second parameter default value.

        Call<ResponseBody> call = iMyAPI.getSecret(retrievedToken);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {


                    try {


                        Toast.makeText(MenuActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(MenuActivity.this, "obsluz to", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MenuActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    ////
    private int hot_number = 2;
    private TextView ui_hot = null;
    //////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        final MenuItem menuItem=menu.findItem(R.id.notification1);

       View actionView = MenuItemCompat.getActionView(menuItem);


        ui_hot = (TextView) actionView.findViewById(R.id.hotlist_hot);
        updateHotCount(hot_number);
        new MyMenuItemStuffListener(actionView) {
           @Override
            public void onClick(View v) {
               onHotlistSelected();
            }
       };

        return super.onCreateOptionsMenu(menu);
    }

    private void onHotlistSelected() {
        Toast.makeText(this, "test!", Toast.LENGTH_SHORT).show();
        updateHotCount(0);
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }


    public void updateHotCount(final int new_hot_number) {
        hot_number = new_hot_number;
        if (ui_hot == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (new_hot_number == 0)
                    ui_hot.setVisibility(View.INVISIBLE);
                else {
                    ui_hot.setVisibility(View.VISIBLE);
                    ui_hot.setText(Integer.toString(new_hot_number));
                }
            }
        });
    }

    static abstract class MyMenuItemStuffListener implements View.OnClickListener {

        private View view;

        MyMenuItemStuffListener(View view) {
            this.view = view;

            view.setOnClickListener(this);

        }

        @Override abstract public void onClick(View v);


    }





    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

      //  if(id==R.id.notification1){


        //}

        return super.onOptionsItemSelected(item);
    }
}