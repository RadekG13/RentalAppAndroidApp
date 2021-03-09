package com.example.logowanie_inzynierka2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.logowanie_inzynierka2.Extensions.ImageResizer;
import com.example.logowanie_inzynierka2.Model.ApartmentViewModel;
import com.example.logowanie_inzynierka2.Model.ResponseApartmentViewModel;
import com.example.logowanie_inzynierka2.Model.ResponseViewModel;
import com.example.logowanie_inzynierka2.Remote.IMyAPI;
import com.example.logowanie_inzynierka2.Remote.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApartmentsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<String> items;
    FloatingActionButton buttonAdd;

    File selectedFile, f= new File("bad_path"); // avoid null exception
    IMyAPI  iMyAPI = RetrofitClient.getInstance().create(IMyAPI.class);
   List<ResponseApartmentViewModel> apartments;

    private AlertDialog.Builder dialogAddBuilder;
    private AlertDialog dialogAdd;
    private EditText newApartment_Address, newApartment_Desc;
    private Button saveButton, cancelButton;
    private ImageView image;
    private ProgressBar progressBar;
    final int REQUEST_CODE_GALLERY=999;



    /*GsonBuilder builder = new GsonBuilder().registerTypeAdapter(byte[].class, new JsonSerializer<byte[]>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(Base64.getEncoder().encodeToString(src));
        }
    });
    Gson gson = builder.create(); */




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartments);





      /*  items = new ArrayList<>();
        items.add("Zdrowa 11");
        items.add("Grabiszyńska 33");
        items.add("Stalowa 180");
        items.add("Plac Grunwaldzki 3");
        items.add("Jedności Narodowej 150");
        items.add("Tęczowa 26 ");
        items.add("Paderewskiego 150 ");
        items.add("Pszowska 22 Wodzisław Sl");*/
        ////
        recyclerView= (RecyclerView) findViewById(R.id.recycleView_Apartaments);
        buttonAdd = (FloatingActionButton) findViewById(R.id.floatingActionButton4);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ////


        recyclerView.setVisibility(View.GONE);



       getApartments(recyclerView, this, progressBar);





        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewApartmentDialog();

            }
        });

    }

    private void setRecyclerView(RecyclerView recyclerView, List<ResponseApartmentViewModel> list) {

        if(list!=null){
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter= new ApartmentsAdapter(this, list);
            recyclerView.setAdapter(adapter);
        }
        else{
            Toast.makeText(ApartmentsActivity.this, "Brak mieszkan!", Toast.LENGTH_SHORT).show();
        }

    }

    public void createNewApartmentDialog(){
        dialogAddBuilder = new AlertDialog.Builder(ApartmentsActivity.this);
        final View ApartmentPopupView=getLayoutInflater().inflate(R.layout.apartmentpopup, null);
        newApartment_Address = (EditText) ApartmentPopupView.findViewById(R.id.eT_Address);
        newApartment_Desc = (EditText) ApartmentPopupView.findViewById(R.id.eT_Desc);
        saveButton=(Button) ApartmentPopupView.findViewById(R.id.bt_Add);
        cancelButton=(Button) ApartmentPopupView.findViewById(R.id.bt_Cancel);
        image=(ImageView) ApartmentPopupView.findViewById(R.id.imageView3);

        dialogAddBuilder.setView(ApartmentPopupView);
        dialogAdd = dialogAddBuilder.create();
        dialogAdd.show();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage(ApartmentsActivity.this);


            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ApartmentViewModel apartment = new ApartmentViewModel(newApartment_Address.getText().toString(), newApartment_Desc.getText().toString());

                if(f.length()!=0){


                    AddApartment(f);

                    Toast.makeText(ApartmentsActivity.this, "Dodano!", Toast.LENGTH_SHORT).show();
                }
                else{

            Toast.makeText(ApartmentsActivity.this, "Brak pliku!", Toast.LENGTH_SHORT).show();}
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAdd.dismiss();
            }
        });
    }

    private void AddApartment( File file){
        ApartmentViewModel apartment = new ApartmentViewModel(newApartment_Address.getText().toString(), newApartment_Desc.getText().toString());



        RequestBody apartmentFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), apartmentFile);





        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrievedToken  = preferences.getString("TOKEN",null);//second parameter default value.











        Call<ResponseViewModel> call =iMyAPI.AddApartment(retrievedToken,filePart,apartment);

        call.enqueue(new Callback<ResponseViewModel>() {
            @Override
            public void onResponse(Call<ResponseViewModel> call, Response<ResponseViewModel> response) {
                if (response.isSuccessful())
                {
                    Toast.makeText(ApartmentsActivity.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {
                        String body = response.errorBody().string(); //Raw json
                        Toast.makeText(ApartmentsActivity.this,body, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseViewModel> call, Throwable t) {
                Toast.makeText(ApartmentsActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

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



    private void getApartments(RecyclerView recyclerView, Context context, ProgressBar progressBar) {

        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrievedToken = preferences.getString("TOKEN", null);

        Call<List<ResponseApartmentViewModel>> call = iMyAPI.GetApartments(retrievedToken);

        call.enqueue(new Callback<List<ResponseApartmentViewModel>>() {
            @Override
            public void onResponse(Call<List<ResponseApartmentViewModel>> call, Response<List<ResponseApartmentViewModel>> response) {
                if (response.isSuccessful()) {

                    List<ResponseApartmentViewModel>  list = response.body();

                    // int i = list.get(0).getPhoto().length();
                    //String s=String.valueOf(i);

                    //Toast.makeText(ApartmentsActivity.this, s , Toast.LENGTH_SHORT).show();

                    if(list!=null){
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        adapter= new ApartmentsAdapter(context, list);
                        recyclerView.setAdapter(adapter);

                        recyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();

                    }
                    else{recyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(ApartmentsActivity.this, "Brak mieszkan!", Toast.LENGTH_SHORT).show();
                    }


/*
                    String pomoc = "cos";


                    try {
                        JSONArray array =  new JSONArray(response.body().toString());

                        String test =array.getString(0);
                        ResponseApartmentViewModel apartments = gson.fromJson(test,ResponseApartmentViewModel.class);
pomoc =apartments.getTitle();
                        Toast.makeText(ApartmentsActivity.this, "czy "+ apartments.getTitle(), Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e(pomoc, "pliska");
                  /*  try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());




                        Log.d(jsonObject.toString(), "proszedzialaj");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/




               //ResponseApartmentViewModel apartments = gson.fromJson(response.body().toString(),ResponseApartmentViewModel.class);


                //    Toast.makeText(ApartmentsActivity.this, "cos2", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String body = response.errorBody().string(); //Raw json
                        Toast.makeText(ApartmentsActivity.this, body, Toast.LENGTH_LONG).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<ResponseApartmentViewModel>> call, Throwable t) {
                Toast.makeText(ApartmentsActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });





    }






}