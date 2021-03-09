package com.example.logowanie_inzynierka2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ApartamentdetailActivity extends AppCompatActivity {
    TextView ApartmentAddress;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<String> items_Title;
    FloatingActionButton buttonAdd2;

    private AlertDialog.Builder dialogAddBuilder;
    private AlertDialog dialogAdd;
    private EditText newRoom_Title, newRoom_Desc, newRoom_Bail, newRoom_Fee;
    private Button saveButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartamentdetail);

        ApartmentAddress = (TextView) findViewById(R.id.tV_Address);

        Intent i =getIntent();
        String title=i.getStringExtra("title");

        ApartmentAddress.setText(title);

        ///back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView= findViewById(R.id.rV_rooms);


        /////////
        items_Title = new ArrayList<>();
        items_Title.add("Pokój 1");
        items_Title.add("Pokój 2");
        items_Title.add("Pokój 3");
        items_Title.add("Pokój 4");
        /////////

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RoomsAdapter(this, items_Title);
        recyclerView.setAdapter(adapter);




        buttonAdd2 = (FloatingActionButton) findViewById(R.id.fAB_Add);
        buttonAdd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewApartmentDialog();

            }
        });


    }



    public void createNewApartmentDialog(){
        dialogAddBuilder = new AlertDialog.Builder(ApartamentdetailActivity.this);
        final View ApartmentPopupView=getLayoutInflater().inflate(R.layout.roomspopup, null);
        newRoom_Title = (EditText) ApartmentPopupView.findViewById(R.id.eT_Title1);
        newRoom_Desc = (EditText) ApartmentPopupView.findViewById(R.id.eT_Desc2);
        newRoom_Bail = (EditText) ApartmentPopupView.findViewById(R.id.eT_Bail);
        newRoom_Fee = (EditText) ApartmentPopupView.findViewById(R.id.eT_Fee);
        saveButton=(Button) ApartmentPopupView.findViewById(R.id.bt_Add2);
        cancelButton=(Button) ApartmentPopupView.findViewById(R.id.bt_Cancel2);

        dialogAddBuilder.setView(ApartmentPopupView);
        dialogAdd = dialogAddBuilder.create();
        dialogAdd.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ApartamentdetailActivity.this, "Dodano!", Toast.LENGTH_SHORT).show();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAdd.dismiss();
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_apartament,menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.Edit){
            Toast.makeText(this, "Edycja!", Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.Remove){
            Toast.makeText(this, "Usunięcie!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}