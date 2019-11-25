package com.example.a242project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

public class SelectedWarranty extends AppCompatActivity {
    TextView date_tv, priceTv, categorySpinnerTv, sellernameDisplay, selleremailDisplay, sellerphoneDisplay, productnameDisplay;
    ImageView myReceipt;
    Button deleteButton;
    String itemID;
    Bitmap decodedByte;

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
        myReceipt = findViewById(R.id.myReceipt);

        deleteButton = findViewById(R.id.deleteButton);

        Intent i = getIntent();

        categorySpinnerTv.setText(i.getStringExtra("productcategory"));
        selleremailDisplay.setText(i.getStringExtra("selleremail"));
        sellernameDisplay.setText(i.getStringExtra("sellername"));
        sellerphoneDisplay.setText(i.getStringExtra("sellerphone"));
        productnameDisplay.setText(i.getStringExtra("productname"));
        date_tv.setText(i.getStringExtra("dateofpurchase"));
        priceTv.setText(i.getStringExtra("productprice"));
        itemID = i.getStringExtra("pushid");
        byte[] decodedString = Base64.decode(i.getStringExtra("image").getBytes(), Base64.DEFAULT);
        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        myReceipt.setImageBitmap(decodedByte);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                DatabaseReference warrantyRef = FirebaseDatabase
                        .getInstance()
                        .getReference("Warranty")
                        .child(uid).child(itemID);
                warrantyRef.removeValue();
                Toast.makeText(SelectedWarranty.this, "Warranty Deleted, Refresh to see changes", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        myReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Intent intent = new Intent(getApplicationContext(), FullReceipt.class);
                intent.putExtra("picture", byteArray);
                startActivity(intent);
            }
        });
    }
}