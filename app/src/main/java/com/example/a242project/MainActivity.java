package com.example.a242project;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    TextView date_tv;
    EditText priceEditText;
    Spinner categorySpinner;
    DatePickerDialog.OnDateSetListener dateSetListener;
    ImageView myReceipt;
    ImageButton camera;
    static final int reqCode = 1;
    boolean zoomOut=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myReceipt =  findViewById(R.id.myReceipt);
        camera = findViewById(R.id.camera);
        date_tv = findViewById(R.id.date_tv);
        priceEditText = findViewById(R.id.priceEditText);
        categorySpinner = findViewById(R.id.categorySpinner);

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

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED)
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, reqCode);
                Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(capture.resolveActivity(getPackageManager())!=null)
                startActivityForResult(capture,reqCode);
            }
        });
        myReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(zoomOut) {
                    Toast.makeText(getApplicationContext(), "NORMAL SIZE!", Toast.LENGTH_LONG).show();
                    myReceipt.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                }else{
                    Toast.makeText(getApplicationContext(), "FULLSCREEN!", Toast.LENGTH_LONG).show();
                    myReceipt.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                    myReceipt.setScaleType(ImageView.ScaleType.FIT_XY);
                    zoomOut = true;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==reqCode)
        {
            Bundle extras = Objects.requireNonNull(data).getExtras();
            Bitmap bitmap = (Bitmap) Objects.requireNonNull(extras).get("data");
            myReceipt.setImageBitmap(bitmap);
        }
    }
}
