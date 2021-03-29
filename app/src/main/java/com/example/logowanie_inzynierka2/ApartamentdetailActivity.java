package com.example.logowanie_inzynierka2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.logowanie_inzynierka2.Extensions.ImageResizer;
import com.example.logowanie_inzynierka2.Model.ApartmentViewModel;
import com.example.logowanie_inzynierka2.Model.ResponseApartmentViewModel;
import com.example.logowanie_inzynierka2.Model.ResponseViewModel;
import com.example.logowanie_inzynierka2.Model.RoomViewModel;
import com.example.logowanie_inzynierka2.Remote.IMyAPI;
import com.example.logowanie_inzynierka2.Remote.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApartamentdetailActivity extends AppCompatActivity implements RoomsAdapter.AdapterCallback {
    TextView ApartmentAddress;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter1;
    ArrayList<String> items_Title;
    FloatingActionButton buttonAdd2;
    private ProgressBar progressBar;
    //-----------------------------------------------------------------------------------------
    private AlertDialog.Builder dialogAddBuilder;
    private AlertDialog dialogAdd;
    private EditText newRoom_Title, newRoom_Desc, newRoom_Bail, newRoom_Fee;
    private Button saveButton, cancelButton;
    //--------------------------------------------------------------------------------
    IMyAPI iMyAPI = RetrofitClient.getInstance().create(IMyAPI.class);

    File  f= new File("bad_path");
    private ImageView image, image1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartamentdetail);

        ApartmentAddress = (TextView) findViewById(R.id.tV_Address);

        Intent i =getIntent();
        String title=i.getStringExtra("Title");
        String apartmentId = i.getStringExtra("Id");
        ApartmentAddress.setText(title);

        ///back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ///
        recyclerView= findViewById(R.id.rV_rooms);
        buttonAdd2 = (FloatingActionButton) findViewById(R.id.fAB_Add);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        ///

        recyclerView.setVisibility(View.GONE);


        getRooms(recyclerView, this, progressBar, apartmentId);





        buttonAdd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewRoomDialog(null);

            }
        });




    }

    private void getRooms(RecyclerView recyclerView1, Context context1, ProgressBar progressBar1, String apartmentId) {

        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrievedToken = preferences.getString("TOKEN", null);

        Call<List<RoomViewModel>> call = iMyAPI.GetRooms(retrievedToken, apartmentId);

        call.enqueue(new Callback<List<RoomViewModel>>() {
            @Override
            public void onResponse(Call<List<RoomViewModel>> call, Response<List<RoomViewModel>> response) {

                Log.d("MyApp", "Code : "+response.code());
                if (response.code() == 401){
                    logout();
                }
                else {
                    if(response.isSuccessful()){
                        List<RoomViewModel> list = response.body();

                        if(list!=null){
                            recyclerView1.setLayoutManager(new LinearLayoutManager(context1));
                            adapter1 = new RoomsAdapter(context1, list);
                            recyclerView1.setAdapter(adapter1);
                        //    Toast.makeText(ApartamentdetailActivity.this, String.valueOf(list.get(0).getRentFee()), Toast.LENGTH_SHORT).show();
                            recyclerView1.setVisibility(View.VISIBLE);
                            progressBar1.setVisibility(View.GONE);
                            adapter1.notifyDataSetChanged();

                        }
                        else{
                            recyclerView1.setVisibility(View.VISIBLE);
                            progressBar1.setVisibility(View.GONE);
                            adapter1.notifyDataSetChanged();
                            Toast.makeText(ApartamentdetailActivity.this, "Brak mieszkan!", Toast.LENGTH_SHORT).show();
                        }




                    }
                    else {
                        try {
                            String body = response.errorBody().string(); //Raw json
                            Toast.makeText(ApartamentdetailActivity.this, body, Toast.LENGTH_LONG).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }


                }



            }

            @Override
            public void onFailure(Call<List<RoomViewModel>> call, Throwable t) {
                Toast.makeText(ApartamentdetailActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });



    }


    public void createNewRoomDialog(RoomViewModel  retrievedRoom){

        dialogAddBuilder = new AlertDialog.Builder(ApartamentdetailActivity.this);
        final View ApartmentPopupView=getLayoutInflater().inflate(R.layout.roomspopup, null);
        newRoom_Title = (EditText) ApartmentPopupView.findViewById(R.id.eT_Title1);
        newRoom_Desc = (EditText) ApartmentPopupView.findViewById(R.id.eT_Desc2);
        newRoom_Bail = (EditText) ApartmentPopupView.findViewById(R.id.eT_Bail);
        newRoom_Fee = (EditText) ApartmentPopupView.findViewById(R.id.eT_Fee);
        saveButton=(Button) ApartmentPopupView.findViewById(R.id.bt_Add2);
        cancelButton=(Button) ApartmentPopupView.findViewById(R.id.bt_Cancel2);
        image = (ImageView) ApartmentPopupView.findViewById(R.id.iV_Room);

        dialogAddBuilder.setView(ApartmentPopupView);
        dialogAdd = dialogAddBuilder.create();
        dialogAdd.show();

        //---------------------------SET DATA - DOWNLOADED FROM SERVER
        Boolean EditorAdd=false;
        String RoomId=null;
        if(retrievedRoom!=null){
            EditorAdd=true;
            String Title = retrievedRoom.getTitle();
            String Desc = retrievedRoom.getDescription();
            String Photo = retrievedRoom.getPhoto();
            String Bail = String.valueOf(retrievedRoom.getDeposit());
            String Fee = String.valueOf(retrievedRoom.getRentFee());
            Log.d("MyApp", Photo);
            newRoom_Title.setText(Title);
            newRoom_Desc.setText(Desc);
            newRoom_Bail.setText(Bail);
            newRoom_Fee.setText(Fee);
            Bitmap photo1 = StringToBitMap(Photo);
            image.setImageBitmap(photo1);
            RoomId = retrievedRoom.getRoomId();
        }

    //--------------------------------------------CHANGE DATA

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(ApartamentdetailActivity.this);
            }
        });

        String finalRoomId = RoomId;
        Boolean finalEditorAdd = EditorAdd;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RoomViewModel room = new RoomViewModel(null, newRoom_Title.getText().toString(),newRoom_Desc.getText().toString(),Integer.parseInt(newRoom_Bail.getText().toString()),Integer.parseInt(newRoom_Fee.getText().toString()),false,null,null);

                if(f.length()!=0){


                    //Create or Edit

                        CallEditOrCreate(room, f, finalRoomId);





                }
                else{

                    Toast.makeText(ApartamentdetailActivity.this, "Brak pliku!", Toast.LENGTH_SHORT).show();}

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAdd.dismiss();
            }
        });
    }




    //-----------------------------------APARTMENTS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_apartament,menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.Edit){

            Intent i =getIntent();
            String apartmentID=i.getStringExtra("Id");
            getApartment(apartmentID);


        }
        if(id==R.id.Remove){

            Intent i =getIntent();
            String apartmentID=i.getStringExtra("Id");
            RemoveApartment(apartmentID);


        }

        return super.onOptionsItemSelected(item);
    }

    private void startEditApartmentDialog(ResponseApartmentViewModel  retrievedApartment) {

        //---------------------------START DIALOG

        dialogAddBuilder = new AlertDialog.Builder(ApartamentdetailActivity.this);
        final View ApartmentPopupView=getLayoutInflater().inflate(R.layout.apartmentpopup, null);
        EditText newApartment_Address = (EditText) ApartmentPopupView.findViewById(R.id.eT_Address);
        EditText  newApartment_Desc = (EditText) ApartmentPopupView.findViewById(R.id.eT_Desc);
        saveButton=(Button) ApartmentPopupView.findViewById(R.id.bt_Add);
        cancelButton=(Button) ApartmentPopupView.findViewById(R.id.bt_Cancel);
         image=(ImageView) ApartmentPopupView.findViewById(R.id.imageView3);

        dialogAddBuilder.setView(ApartmentPopupView);
        dialogAdd = dialogAddBuilder.create();
        dialogAdd.show();

        //---------------------------SET DATA - DOWNLOADED FROM SERVER



        String Title=retrievedApartment.getTitle();
        String Desc=retrievedApartment.getDescription();
        String Photo=retrievedApartment.getPhoto();

        newApartment_Address.setText(Title);
        newApartment_Desc.setText(Desc);
        Bitmap photo = StringToBitMap(Photo);
        image.setImageBitmap(photo);

        //---------------------------CHANGE DATA
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(ApartamentdetailActivity.this);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ApartmentViewModel apartment = new ApartmentViewModel(newApartment_Address.getText().toString(), newApartment_Desc.getText().toString());

                if(f.length()!=0){




                    EditApartment(f, apartment);
                    //Toast.makeText(ApartamentdetailActivity.this, apartment.getTitle(), Toast.LENGTH_SHORT).show();



                }
                else{

                    Toast.makeText(ApartamentdetailActivity.this, "Brak pliku!", Toast.LENGTH_SHORT).show();}



            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAdd.dismiss();
            }
        });
    }

    private void EditApartment(File file,  ApartmentViewModel apartment) {



        RequestBody apartmentFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), apartmentFile);

        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrievedToken  = preferences.getString("TOKEN",null);//second parameter default value.

        Intent i =getIntent();
        String apartmentID=i.getStringExtra("Id");

        Call<ResponseViewModel> call = iMyAPI.UpdateApartment(retrievedToken, filePart, apartment, apartmentID);

        call.enqueue(new Callback<ResponseViewModel>() {
            @Override
            public void onResponse(Call<ResponseViewModel> call, Response<ResponseViewModel> response) {

                if (response.isSuccessful()){
                    Toast.makeText(ApartamentdetailActivity.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    dialogAdd.dismiss();


                    (new Handler())
                            .postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            Intent intent = new Intent(ApartamentdetailActivity.this,ApartmentsActivity.class);
                                            startActivity(intent);
                                        }
                                    }, 1000);




                }
                else
                {
                    try {
                        String body = response.errorBody().string(); //Raw json
                        Toast.makeText(ApartamentdetailActivity.this,body, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<ResponseViewModel> call, Throwable t) {
                Toast.makeText(ApartamentdetailActivity.this, t.toString(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void getApartment(String apartmentId) {

        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrievedToken = preferences.getString("TOKEN", null);



        Call<ResponseApartmentViewModel> call = iMyAPI.GetOneApartment(retrievedToken, apartmentId);

        call.enqueue(new Callback<ResponseApartmentViewModel>() {
            @Override
            public void onResponse(Call<ResponseApartmentViewModel> call, Response<ResponseApartmentViewModel> response) {

                Log.d("MyApp", "Code : "+response.code());
                if (response.code() == 401){
                    logout();

                }
                else{
                    if (response.isSuccessful()) {
                        ResponseApartmentViewModel  retrievedApartment=response.body();

                        //Toast.makeText(ApartamentdetailActivity.this, retrievedApartment.getTitle(), Toast.LENGTH_SHORT).show();

                        startEditApartmentDialog( retrievedApartment);

                    }
                    else{
                        Toast.makeText(ApartamentdetailActivity.this, "Blad!", Toast.LENGTH_SHORT).show();}

                }





            }

            @Override
            public void onFailure(Call<ResponseApartmentViewModel> call, Throwable t) {
                Toast.makeText(ApartamentdetailActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    //-----------------------------------DUPLICATED CODE - TO FIX

    private void logout() {
        Toast.makeText(ApartamentdetailActivity.this, "Zaloguj się ponownie!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ApartamentdetailActivity.this,MainActivity.class);
        startActivity(intent);
        //finish();
    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Zrob zdjecie", "Wybierz z galerii","Anuluj" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Zrob zdjecie")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Wybierz z galerii")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Anuluj")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        image.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();



                        try {
                            InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                            //  FileUtils.copyInputStreamToFile(inputStream, selectedFile);


                            ///Add rotation for  photos with bad orientation



                            Bitmap squareBitmap;
                            if (bitmap.getWidth() >= bitmap.getHeight())
                                squareBitmap = Bitmap.createBitmap(bitmap, bitmap.getWidth() / 2 - bitmap.getHeight() / 2, 0, bitmap.getHeight(), bitmap.getHeight());
                            else
                                squareBitmap = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight() / 2 - bitmap.getWidth() / 2, bitmap.getWidth(), bitmap.getWidth());




                            int maxsize = 2097152/8; //2MB divided by bytes per pixel (RGBA_F16 has 8 bytes)
                            Bitmap reducedBitmap = ImageResizer.reduceBitmapSize(squareBitmap,maxsize );
                            image.setImageBitmap(reducedBitmap);
                            f = new File(this.getCacheDir(), "cosnowego");
                            f.createNewFile();

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            reducedBitmap.compress(Bitmap.CompressFormat.PNG, 0 , bos);
                            byte[] bitmapdata = bos.toByteArray();
                            FileOutputStream fos = new FileOutputStream(f);
                            fos.write(bitmapdata);
                            fos.flush();
                            fos.close();



                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }/*
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                Photo.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }*/ catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
            }
        }
    }

    private Bitmap StringToBitMap(String encodedString) {
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

    private void RemoveApartment(String apartmentID) {
        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrievedToken  = preferences.getString("TOKEN",null);//second parameter default value.



        Call<ResponseViewModel> call =iMyAPI.DeleteApartment(retrievedToken,apartmentID);

        call.enqueue(new Callback<ResponseViewModel>() {
            @Override
            public void onResponse(Call<ResponseViewModel> call, Response<ResponseViewModel> response) {
                if (response.isSuccessful())
                {
                    Toast.makeText(ApartamentdetailActivity.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    (new Handler())
                            .postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            Intent intent = new Intent(ApartamentdetailActivity.this,ApartmentsActivity.class);
                                            startActivity(intent);
                                        }
                                    }, 1000);

                }
                else
                {
                    try {
                        String body = response.errorBody().string(); //Raw json
                        Toast.makeText(ApartamentdetailActivity.this,body, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseViewModel> call, Throwable t) {
                Toast.makeText(ApartamentdetailActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //--------------------------------ROOMS
    public void EditRoom(String roomId){
        //pobrać apartmentid
        Intent i =getIntent();
        String apartmentId = i.getStringExtra("Id");

        //pobrać z backendu jeden pokój
        getRoom(apartmentId, roomId);
        //włączyć dialog zmiany pokoju





    }

    public void RemoveRoom(String roomId){

        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrievedToken  = preferences.getString("TOKEN",null);//second parameter default value.


        //pobrać apartmentid
        Intent i =getIntent();
        String apartmentId = i.getStringExtra("Id");

        Call<ResponseViewModel> call =iMyAPI.DeleteRoom(retrievedToken,apartmentId,roomId);

        call.enqueue(new Callback<ResponseViewModel>() {
            @Override
            public void onResponse(Call<ResponseViewModel> call, Response<ResponseViewModel> response) {
                if (response.isSuccessful())
                {
                    Toast.makeText(ApartamentdetailActivity.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent i =getIntent();
                    String apartmentId = i.getStringExtra("Id");
                    getRooms(recyclerView, ApartamentdetailActivity.this, progressBar,apartmentId);
                }
                else
                {
                    try {
                        String body = response.errorBody().string(); //Raw json
                        Toast.makeText(ApartamentdetailActivity.this,body, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseViewModel> call, Throwable t) {
                Toast.makeText(ApartamentdetailActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void getRoom(String apartmentId, String roomId){
        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrievedToken  = preferences.getString("TOKEN",null);//second parameter default value.


        Call<RoomViewModel>  call = iMyAPI.GetOneRoom(retrievedToken,apartmentId,roomId);

        call.enqueue(new Callback<RoomViewModel>() {
            @Override
            public void onResponse(Call<RoomViewModel> call, Response<RoomViewModel> response) {

                Log.d("MyApp", "Code : "+response.code());
                if (response.code() == 401){
                    logout();

                }
                else{
                    if (response.isSuccessful()) {
                        RoomViewModel  retrievedRoom=response.body();

                        //Toast.makeText(ApartamentdetailActivity.this, retrievedApartment.getTitle(), Toast.LENGTH_SHORT).show();

                        createNewRoomDialog(retrievedRoom);

                    }
                    else{
                        Toast.makeText(ApartamentdetailActivity.this, "Blad!", Toast.LENGTH_SHORT).show();}

                }


            }

            @Override
            public void onFailure(Call<RoomViewModel> call, Throwable t) {
                Toast.makeText(ApartamentdetailActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });




    }


    private void CallEditOrCreate(RoomViewModel room,File file, String roomId) {

        RequestBody roomFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), roomFile);

        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrievedToken  = preferences.getString("TOKEN",null);//second parameter default value.

        Intent i =getIntent();
        String apartmentID=i.getStringExtra("Id");

        if(roomId!=null){
            Call<ResponseViewModel> call = iMyAPI.EditRoom(retrievedToken,filePart,room, apartmentID, roomId);

            call.enqueue(new Callback<ResponseViewModel>() {
                @Override
                public void onResponse(Call<ResponseViewModel> call, Response<ResponseViewModel> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(ApartamentdetailActivity.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        dialogAdd.dismiss();
                        // Intent intent = new Intent(getApplicationContext(), ApartmentsActivity.class);
                        //startActivity(intent);

                        /*(new Handler())
                                .postDelayed(
                                        new Runnable() {
                                            public void run() {

                                            }
                                        }, 500);*/

                        Intent i =getIntent();
                        String apartmentId = i.getStringExtra("Id");
                        getRooms(recyclerView, ApartamentdetailActivity.this, progressBar,apartmentId);

                    }
                    else
                    {
                        try {
                            String body = response.errorBody().string(); //Raw json
                            Toast.makeText(ApartamentdetailActivity.this,body, Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseViewModel> call, Throwable t) {
                    Toast.makeText(ApartamentdetailActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else{
            Call<ResponseViewModel> call = iMyAPI.AddRoom(retrievedToken, filePart, room, apartmentID);

            call.enqueue(new Callback<ResponseViewModel>() {
                @Override
                public void onResponse(Call<ResponseViewModel> call, Response<ResponseViewModel> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(ApartamentdetailActivity.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        dialogAdd.dismiss();
                        // Intent intent = new Intent(getApplicationContext(), ApartmentsActivity.class);
                        //startActivity(intent);
                        Intent i =getIntent();
                        String apartmentId = i.getStringExtra("Id");
                        getRooms(recyclerView, ApartamentdetailActivity.this, progressBar,apartmentId);


                    }
                    else
                    {
                        try {
                            String body = response.errorBody().string(); //Raw json
                            Toast.makeText(ApartamentdetailActivity.this,body, Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseViewModel> call, Throwable t) {
                    Toast.makeText(ApartamentdetailActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }


}