package com.example.logowanie_inzynierka2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logowanie_inzynierka2.Model.ResponseApartmentViewModel;
import com.example.logowanie_inzynierka2.Model.RoomViewModel;

import java.util.List;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder22> {

    private LayoutInflater layoutInflater;
    private List<RoomViewModel> dataRooms;
    private Context mContext;
    private AdapterCallback adapterCallback;

    RoomsAdapter(Context context, List<RoomViewModel> data){
        this.layoutInflater=LayoutInflater.from(context);
        this.dataRooms = data;
        this.mContext = context;

        try {
            adapterCallback = ((AdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString());
        }

    }

    @NonNull
    @Override
    public ViewHolder22 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_cardview_rooms, parent, false);
        return new ViewHolder22(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder22 holder, int position) {

        RoomViewModel room = dataRooms.get(position);


        holder.textTitle.setText(room.getTitle());
        holder.textDesc.setText(room.getDescription());
        holder.textFee.setText(String.valueOf(room.getRentFee()));
        holder.textDeposit.setText(String.valueOf(room.getDeposit()));
        String status="wolne";
         if(room.getStatus()==true){
             status="wynajęte";
         }
        holder.textStatus.setText(status);

        Bitmap photo = StringToBitMap(room.getPhoto());
        holder.imageView.setImageBitmap(photo);


    }

    @Override
    public int getItemCount() {
        return dataRooms.size();
    }

    public class ViewHolder22 extends RecyclerView.ViewHolder{

        TextView textTitle, textDesc, textDeposit, textFee, textStatus;
        ImageView imageView;

    public ViewHolder22(@NonNull View itemView) {
        super(itemView);


        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                RoomViewModel Room = dataRooms.get(getAdapterPosition());
                openOptionDialog(Room.getRoomId());
                return true;
            }
        });



        ///
        textTitle = itemView.findViewById(R.id.tv_Title1);
        textStatus= itemView.findViewById(R.id.tv_Status1);
        textDeposit = itemView.findViewById(R.id.tV_bail1);
        textFee = itemView.findViewById(R.id.tV_rentFee);
        textDesc = itemView.findViewById(R.id.tV_desc1);
        imageView = itemView.findViewById(R.id.imageView22);
    }

        private void openOptionDialog(String roomId) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
          builder.setTitle("Opcje")
                   .setCancelable(true)
                    .setPositiveButton("Edycja", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            try {
                                adapterCallback.EditRoom(roomId);
                            } catch (ClassCastException e) {
                                // do something
                            }





                        }
                    })
                    .setNegativeButton("Usunięcie", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                adapterCallback.RemoveRoom(roomId);
                            } catch (ClassCastException e) {
                                // do something
                            }
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();


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

    public static interface AdapterCallback {
        void EditRoom(String id);
        void RemoveRoom(String id);
    }
}
