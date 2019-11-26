package com.example.a242project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

@SuppressWarnings("ALL")
public class WarrantyList extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ImageButton logout;
    private ImageButton goToAddWarranty;
    private GoogleSignInClient mGoogleSignInClient;
    private String AES = "AES";
    private TextView emptyListMessage;
    private Context mContext;
    private ArrayList<Warranty> warrantyList;
    private ListView mListView;
    private WarrantyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warranty_listview);
        emptyListMessage = findViewById(R.id.emptyListMessage);

        mContext = this;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        logout = findViewById(R.id.Logout);
        goToAddWarranty = findViewById(R.id.goToAddWarranty);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        mListView = (ListView) findViewById(R.id.mListView);

        DatabaseReference warrantyRef = FirebaseDatabase
                .getInstance()
                .getReference("Warranty")
                .child(uid);

        warrantyList = new ArrayList<>();
        warrantyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Decrypt
                    try {
                        Map<String, Object> warranty = (Map<String, Object>) dataSnapshot.getValue();

                        if(warranty!=null) {
                            emptyListMessage.setVisibility(View.GONE);
                            for (String childKey : warranty.keySet()) {
                                Map<String, Object> currentWarrantyObject = (Map<String, Object>) warranty.get(childKey);
                                Warranty currentWarranty = new Warranty(currentWarrantyObject.get("sellerName").toString(), currentWarrantyObject.get("sellerPhone").toString(),
                                        currentWarrantyObject.get("sellerEmail").toString(), currentWarrantyObject.get("dateOfPurchase").toString(), currentWarrantyObject.get("productName").toString(),
                                        currentWarrantyObject.get("productCategory").toString(), currentWarrantyObject.get("productPrice").toString(), currentWarrantyObject.get("pushid").toString(), currentWarrantyObject.get("image").toString(),
                                        currentWarrantyObject.get("purchaseLocation").toString());
                                Warranty decryptedWarranty = decrypt(currentWarranty, "zxcasdqwe");
                                warrantyList.add(decryptedWarranty);
                            }
                            adapter = new WarrantyListAdapter(mContext, R.layout.warranty_listitem, warrantyList);
                            mListView.setAdapter(adapter);
                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                    Intent warrantyDetails = new Intent(getApplicationContext(), SelectedWarranty.class);
                                    warrantyDetails.putExtra("sellername",warrantyList.get((int)id).getSellerName());
                                    warrantyDetails.putExtra("sellerphone",warrantyList.get((int)id).getSellerPhone());
                                    warrantyDetails.putExtra("selleremail",warrantyList.get((int)id).getSellerEmail());
                                    warrantyDetails.putExtra("dateofpurchase",warrantyList.get((int)id).getDateOfPurchase());
                                    warrantyDetails.putExtra("productname",warrantyList.get((int)id).getProductName());
                                    warrantyDetails.putExtra("productcategory",warrantyList.get((int)id).getProductCategory());
                                    warrantyDetails.putExtra("productprice",warrantyList.get((int)id).getProductPrice());
                                    warrantyDetails.putExtra("image",warrantyList.get((int)id).getImage());
                                    warrantyDetails.putExtra("pushid",warrantyList.get((int)id).getPushid());
                                    warrantyDetails.putExtra("purchaseLocation",warrantyList.get((int)id).getPurchaseLocation());
                                    startActivity(warrantyDetails);
                                }
                            });
                        }
                        else {
                            emptyListMessage.setVisibility(View.VISIBLE);
                        }
                    } catch (NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        goToAddWarranty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainActivity();
            }
        });
    }

    private Warranty decrypt(Warranty DataWarranty, String password) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        String decryptedValue;

        byte[] decodedValue = Base64.decode(DataWarranty.sellerName, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodedValue);
        decryptedValue = new String (decValue);
        DataWarranty.setSellerName(decryptedValue);

        decodedValue = Base64.decode(DataWarranty.sellerPhone, Base64.DEFAULT);
        decValue = c.doFinal(decodedValue);
        decryptedValue = new String (decValue);
        DataWarranty.setSellerPhone(decryptedValue);

        decodedValue = Base64.decode(DataWarranty.sellerEmail, Base64.DEFAULT);
        decValue = c.doFinal(decodedValue);
        decryptedValue = new String (decValue);
        DataWarranty.setSellerEmail(decryptedValue);

        decodedValue = Base64.decode(DataWarranty.dateOfPurchase, Base64.DEFAULT);
        decValue = c.doFinal(decodedValue);
        decryptedValue = new String (decValue);
        DataWarranty.setDateOfPurchase(decryptedValue);

        decodedValue = Base64.decode(DataWarranty.productName, Base64.DEFAULT);
        decValue = c.doFinal(decodedValue);
        decryptedValue = new String (decValue);
        DataWarranty.setProductName(decryptedValue);

        decodedValue = Base64.decode(DataWarranty.productCategory, Base64.DEFAULT);
        decValue = c.doFinal(decodedValue);
        decryptedValue = new String (decValue);
        DataWarranty.setProductCategory(decryptedValue);

        decodedValue = Base64.decode(DataWarranty.productPrice, Base64.DEFAULT);
        decValue = c.doFinal(decodedValue);
        decryptedValue = new String (decValue);
        DataWarranty.setProductPrice(decryptedValue);

        decodedValue = Base64.decode(DataWarranty.image, Base64.DEFAULT);
        decValue = c.doFinal(decodedValue);
        decryptedValue = new String (decValue);
        DataWarranty.setImage(decryptedValue);

        return DataWarranty;

    }

    private void signOut() {
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    openLogin();
                    }
                });
    }

    private void openLogin() {
        Intent intent = new Intent(this, Login.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);
    }

    private SecretKeySpec generateKey(String password) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        return new SecretKeySpec(key, "AES");
    }
}
