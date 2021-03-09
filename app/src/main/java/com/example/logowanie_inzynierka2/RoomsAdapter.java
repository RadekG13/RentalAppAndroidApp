package com.example.logowanie_inzynierka2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder2> {

    private LayoutInflater layoutInflater;
    private List<String> data2;

    RoomsAdapter(Context context, List<String> data){
        this.layoutInflater=LayoutInflater.from(context);
        this.data2 = data;

    }

    @NonNull
    @Override
    public ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_cardview_rooms, parent, false);
        return new ViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder2 holder, int position) {
        String title = data2.get(position);
        holder.textTitle.setText(title);

        //tutej dodac pozostale


    }

    @Override
    public int getItemCount() {
        return data2.size();
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder{

        TextView textTitle, textDesc, textBail, textFee, textStatus;

    public ViewHolder2(@NonNull View itemView) {
        super(itemView);
        textTitle = itemView.findViewById(R.id.tv_Title);
        textStatus= itemView.findViewById(R.id.tv_Status);
        textBail = itemView.findViewById(R.id.tV_bail);
        textFee = itemView.findViewById(R.id.tV_rentFee);
        textDesc = itemView.findViewById(R.id.tV_desc);
    }
}

}
