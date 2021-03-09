package com.example.logowanie_inzynierka2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logowanie_inzynierka2.Model.ResponseApartmentViewModel;

import java.util.List;

public class ApartmentsAdapter extends RecyclerView.Adapter<ApartmentsAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<ResponseApartmentViewModel> data;

    ApartmentsAdapter(Context context, List<ResponseApartmentViewModel> data){
        this.layoutInflater= LayoutInflater.from(context);
        this.data=data;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_cardview_apartaments,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //

        ResponseApartmentViewModel Apartment = data.get(position);
        holder.textAddress.setText(Apartment.getTitle()); //data.get(position));//
        holder.textDescription.setText(Apartment.getDescription());

        Bitmap photo = StringToBitMap(Apartment.getPhoto());
        holder.imageView.setImageBitmap(photo);



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textAddress, textDescription;
        ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), ApartamentdetailActivity.class);
                    ResponseApartmentViewModel Apartment = data.get(getAdapterPosition());
                    i.putExtra("title", Apartment.getTitle()); //data.get(getAdapterPosition())
                    i.putExtra("Id", Apartment.getApartmentId());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(i);
                }
            });

            textAddress= itemView.findViewById(R.id.tv_Title);
            textDescription= itemView.findViewById(R.id.tv_Status);
            imageView = itemView.findViewById(R.id.imageView2);
        }
    }
    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
