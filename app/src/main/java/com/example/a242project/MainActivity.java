package com.example.a242project;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    TextView date_tv;
    Button addwarrantybt;
    EditText priceEditText, sellernameET, sellerphoneET, selleremailET, productnameET;
    Spinner categorySpinner;
    DatePickerDialog.OnDateSetListener dateSetListener;
    ImageView myReceipt;
    ImageButton camera;
    Uri imageUri;
    static final int reqCode = 1;
    ListView warrantylistview;
    ArrayList<Warranty> warrantyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myReceipt =  findViewById(R.id.myReceipt);
        camera = findViewById(R.id.camera);
        date_tv = findViewById(R.id.date_tv);
        priceEditText = findViewById(R.id.priceEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        warrantylistview = findViewById(R.id.warrantylistview);
        selleremailET = findViewById(R.id.selleremailET);
        sellerphoneET = findViewById(R.id.sellerphoneET);
        sellernameET = findViewById(R.id.sellernameET);
        productnameET = findViewById(R.id.productnameET);
        addwarrantybt = findViewById(R.id.addwarrantybt);

        date_tv.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        dateSetListener,
                        year,month,day);
                Objects.requireNonNull(datePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            month++;
            String date = day+"/"+month+"/"+year;
            date_tv.setText(date);
            }
        };
        ArrayAdapter<CharSequence> spinnerAdapt = ArrayAdapter.createFromResource(
                MainActivity.this,
                R.array.itemCategory,
                android.R.layout.simple_spinner_item);
        spinnerAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapt);

        //set camera on click
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED)
                {
                    //Toast.makeText(getApplicationContext(), "onRequest",Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, reqCode);
                }
                else{
                    Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(capture.resolveActivity(getPackageManager())!=null) {
                        startActivityForResult(capture,reqCode);
                    }
                }
        }});

        myReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri!=null)
                {
                    //Toast.makeText(getApplicationContext(),imageUri.toString(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(imageUri, "image/*");
                    startActivity(intent);
                }
            }
        });
        //String sellerName, String sellerPhone, String sellerEmail, String dateOfPurchase, String productName, String productCategory, String productPrice
        addwarrantybt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Warranty obj = new Warranty(sellernameET.getText().toString(), sellerphoneET.getText().toString(), selleremailET.getText().toString(),
                        date_tv.getText().toString(), productnameET.getText().toString(), categorySpinner.getSelectedItem().toString(), priceEditText.getText().toString());
                //encrypt object
            }
        });
    }
    //implement array adapter for listview
    class InformationRetrieval extends AsyncTask< >{ //params, progress, result

        @Override
        protected Object doInBackground(Object[] objects) {
            //get information from cloud
            //store into class
            //decrypt each attribute
            //arraylist.clear()
            //add to array list
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //set adapter
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==reqCode)
        {
            Bundle extras = Objects.requireNonNull(data).getExtras();
            Bitmap bitmap = (Bitmap) Objects.requireNonNull(extras).get("data");
            //Bitmap converted = bitmap.copy(Bitmap.Config.ARGB_8888, false);
            Bitmap converted = convert(bitmap, Bitmap.Config.ARGB_8888);
            myReceipt.setImageBitmap(converted);
            imageUri = getImageUri(getApplicationContext(), Objects.requireNonNull(bitmap));
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Receipt", null);
        return Uri.parse(path);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == reqCode){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // main logic
                Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(capture.resolveActivity(getPackageManager())!=null) {
                    startActivityForResult(capture,reqCode);
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "App requires camera and storage permission to scan your receipt",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private Bitmap convert(Bitmap bitmap, Bitmap.Config config) {
        Bitmap convertedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
        Canvas canvas = new Canvas(convertedBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return convertedBitmap;
    }
}
