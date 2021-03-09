package com.example.logowanie_inzynierka2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.logowanie_inzynierka2.Model.Notification;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        ListView notificationListView = (ListView) findViewById(R.id.lv_Notification);

        Notification newPayment = new Notification("Płatność", "kaucja: 600", "21-01-21");
        Notification newPayment2 = new Notification("Płatność", "Rachunki styczeń: 600", "24-01-21");
        Notification newPayment3 = new Notification("Płatność", "Rachunki luty: 630", "25-01-21");

        ArrayList<Notification> notificationList = new ArrayList<>();
        notificationList.add(newPayment);
        notificationList.add(newPayment2);
        notificationList.add(newPayment3);

       NotificationListAdapter adapter = new NotificationListAdapter(this, R.layout.notification_adapter_view, notificationList);
       notificationListView.setAdapter(adapter);

    }
}