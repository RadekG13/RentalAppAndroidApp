package com.example.logowanie_inzynierka2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RepairActivity extends AppCompatActivity {
    private Spinner spn_Type;
    EditText edt_City;
    Button btn_Search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        ///
        edt_City=(EditText) findViewById(R.id.edt_city);
        spn_Type=(Spinner) findViewById(R.id.spinner);
        ///
        btn_Search=(Button) findViewById(R.id.bt_search);

        List<String> type = new ArrayList<>();


        type.add("Typ usługi");
        type.add("elektryk");
        type.add("hydraulik");
        type.add("stolarz stolarstwo");
        type.add("malowanie scian");
        type.add("alarmy");
        type.add("inne-uslugi");



        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, type);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_Type.setAdapter(typeAdapter);







        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ChosenCity=edt_City.getText().toString();
               String newChosenCity= ChosenCity.replaceAll(" ", "-");
                if(spn_Type.getSelectedItemPosition()!=0) {
                    String ChosenItem = spn_Type.getSelectedItem().toString();
                   String newChosenItem= ChosenItem.replaceAll(" ", "-");
                    searchRepair(newChosenCity, newChosenItem);
                    Toast.makeText(RepairActivity.this, newChosenCity +"  "+ newChosenItem, Toast.LENGTH_SHORT).show();
                }
else{
                    Toast.makeText(RepairActivity.this, "Nie wybrano typu usługi", Toast.LENGTH_SHORT).show();
                }


            }
        });




    }



    private void searchRepair(String city, String type){
        try{
            Uri uri = Uri.parse("https://www.remontuj.pl/p/"+city+";"+type);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

catch( ActivityNotFoundException e) {
        e.printStackTrace();
        Toast.makeText(this, "Błąd!", Toast.LENGTH_SHORT).show();

        }


    }



}