package com.example.a242project;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Output;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {
    private static final String FILE_NAME_ENC = "Warranty_Photo";
    TextView date_tv;
    Button addwarrantybt;
    EditText priceEditText, sellernameET, sellerphoneET, selleremailET, productnameET;
    Spinner categorySpinner;
    DatePickerDialog.OnDateSetListener dateSetListener;
    ImageView myReceipt;
    ImageButton camera;
    Uri imageUri;
    static final int reqCode = 1;
    DatabaseReference warranty;
    String AES = "AES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_warranty);
        myReceipt =  findViewById(R.id.myReceipt);
        camera = findViewById(R.id.camera);
        date_tv = findViewById(R.id.date_tv);
        priceEditText = findViewById(R.id.priceEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        selleremailET = findViewById(R.id.selleremailET);
        sellerphoneET = findViewById(R.id.sellerphoneET);
        sellernameET = findViewById(R.id.sellernameET);
        productnameET = findViewById(R.id.productnameET);
        addwarrantybt = findViewById(R.id.addwarrantybt);
        warranty = FirebaseDatabase.getInstance().getReference().child("Warranty");

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
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, reqCode);
                }
                else{
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Receipt");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "Warranty Receipt");
                    imageUri = getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    capture.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
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

        addwarrantybt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Make image into string, then pass into obj
                myReceipt.buildDrawingCache();
                Bitmap bmap = myReceipt.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] b = baos.toByteArray();
                String imageAsString = Base64.encodeToString(b, Base64.DEFAULT);


                Warranty obj = new Warranty(sellernameET.getText().toString(), sellerphoneET.getText().toString(), selleremailET.getText().toString(),
                        date_tv.getText().toString(), productnameET.getText().toString(), categorySpinner.getSelectedItem().toString(), priceEditText.getText().toString(), "", imageAsString);
                //encrypt object
                try {
                    Warranty encryptedObj = encrypt(obj,"zxcasdqwe");
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();
                    DatabaseReference warrantyRef = FirebaseDatabase
                            .getInstance()
                            .getReference("Warranty")
                            .child(uid);
                    DatabaseReference pushRef = warrantyRef.push();
                    String pushId = pushRef.getKey();
                    encryptedObj.setPushid(pushId);
                    pushRef.setValue(encryptedObj);
                    openWarrantyList();
                } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                    e.printStackTrace();
                }

            }
        });
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
                Toast.makeText(getApplicationContext(), "Manually give camera and storage permission to scan receipts", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == reqCode) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);
                    Log.d("D", "onActivityResult: " + "Added image");
                    myReceipt.setImageBitmap(thumbnail);
                    myReceipt.setRotation(90);
                    String imageurl = getRealPathFromURI(imageUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//            Bundle extras = data.getExtras();
//            Bitmap bitmap = (Bitmap) Objects.requireNonNull(extras).get("data");
//            //Bitmap converted = bitmap.copy(Bitmap.Config.ARGB_8888, false);
//            Bitmap converted = convert(bitmap, Bitmap.Config.ARGB_8888);
//            myReceipt.setImageBitmap(converted);
//            imageUri = getImageUri(getApplicationContext(), bitmap);
                //Don't need this code i'm assuming, so delete when program is finished and we don't need to refer to this code
            }
        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Receipt", null);
        return Uri.parse(path);
    }

    private Bitmap convert(Bitmap bitmap, Bitmap.Config config) {
        Bitmap convertedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
        Canvas canvas = new Canvas(convertedBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return convertedBitmap;
    }
    private void openWarrantyList() {
        Intent intent = new Intent(this, WarrantyList.class );
        startActivity(intent);
    }

    private Warranty encrypt(Warranty Data, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE,key);


        byte[] encVal = c.doFinal(Data.getSellerName().getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        Data.setSellerName(encryptedValue);

        encVal = c.doFinal(Data.getSellerPhone().getBytes());
        encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        Data.setSellerPhone(encryptedValue);

        encVal = c.doFinal(Data.getSellerEmail().getBytes());
        encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        Data.setSellerEmail(encryptedValue);

        encVal = c.doFinal(Data.getDateOfPurchase().getBytes());
        encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        Data.setDateOfPurchase(encryptedValue);

        encVal = c.doFinal(Data.getProductName().getBytes());
        encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        Data.setProductName(encryptedValue);

        encVal = c.doFinal(Data.getProductCategory().getBytes());
        encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        Data.setProductCategory(encryptedValue);

        encVal = c.doFinal(Data.getProductPrice().getBytes());
        encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        Data.setProductPrice(encryptedValue);

        encVal = c.doFinal(Data.getImage().getBytes());
        encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        Data.setImage(encryptedValue);

        return Data;

    }

    private SecretKeySpec generateKey(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;

    }
    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }

}