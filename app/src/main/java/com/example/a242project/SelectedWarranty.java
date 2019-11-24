package com.example.a242project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SelectedWarranty extends AppCompatActivity {
    TextView date_tv, priceTv, categorySpinnerTv, sellernameDisplay, selleremailDisplay, sellerphoneDisplay, productnameDisplay;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_warranty_details);

        date_tv = findViewById(R.id.date_tv);
        priceTv = findViewById(R.id.priceTv);
        categorySpinnerTv = findViewById(R.id.categorySpinnerTv);
        selleremailDisplay = findViewById(R.id.selleremailDisplay);
        sellernameDisplay = findViewById(R.id.sellernameDisplay);
        sellerphoneDisplay = findViewById(R.id.sellerphoneDisplay);
        productnameDisplay = findViewById(R.id.productnameDisplay);

        Intent i = getIntent();

        categorySpinnerTv.setText(i.getStringExtra("productcategory"));
        selleremailDisplay.setText(i.getStringExtra("selleremail"));
        sellernameDisplay.setText(i.getStringExtra("sellername"));
        sellerphoneDisplay.setText(i.getStringExtra("sellerphone"));
        productnameDisplay.setText(i.getStringExtra("productname"));
        date_tv.setText(i.getStringExtra("dateofpurchase"));
        priceTv.setText(i.getStringExtra("productprice"));
    }
}
