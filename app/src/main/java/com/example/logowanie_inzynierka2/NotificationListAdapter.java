package com.example.logowanie_inzynierka2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.logowanie_inzynierka2.Model.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationListAdapter extends ArrayAdapter<Notification> {
    private Context notificationContext;
    int notificationResource;

    public NotificationListAdapter(Context context, int resource, ArrayList<Notification> objects) {
        super(context, resource, objects);
        notificationContext = context;
        notificationResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String title = getItem(position).getTitle();
        String description = getItem(position).getDescribe();
        String date = getItem(position).getDate();
        Notification notification = new Notification(title, description, date);

        LayoutInflater inflater = LayoutInflater.from(notificationContext);
        convertView = inflater.inflate(notificationResource, parent, false);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title3);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.tv_Desc3);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tv_date);

        tvTitle.setText(title);
        tvDescription.setText(description);
        tvDate.setText(date);
        return convertView;
    }
}
