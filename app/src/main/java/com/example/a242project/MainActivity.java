package com.example.a242project;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    TextView date_tv;
    EditText priceEditText;
    Spinner categorySpinner;
    DatePickerDialog.OnDateSetListener dateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        date_tv = (TextView) findViewById(R.id.date_tv);
        priceEditText = (EditText) findViewById(R.id.priceEditText);
        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
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
            String date = day+"/"+month+"//"+year;
            date_tv.setText(date);
        }
    };
        ArrayAdapter<CharSequence> spinnerAdapt = ArrayAdapter.createFromResource(
                                            MainActivity.this,
                                                    R.array.itemCategory,
                                                    android.R.layout.simple_spinner_item);
        spinnerAdapt.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapt);
    }
}
