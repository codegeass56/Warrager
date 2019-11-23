package com.example.a242project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.net.URL;
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

public class WarrantyList extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button logout;
    private Button goToAddWarranty;
    private GoogleSignInClient mGoogleSignInClient;
    private String AES = "AES";

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warranty_list);
        mContext = this;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        logout = (Button) findViewById(R.id.Logout);
        goToAddWarranty = (Button) findViewById(R.id.goToAddWarranty);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        ListView mListView = (ListView) findViewById(R.id.list);

        DatabaseReference warrantyRef = FirebaseDatabase
                .getInstance()
                .getReference("Warranty")
                .child(uid);

        ArrayList<Warranty> warrantyList = new ArrayList<>();
        warrantyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Decrypt
                    try {
                        Map<String, Object> warranty = (Map<String, Object>) dataSnapshot.getValue();

                        for (String childKey: warranty.keySet()) {
                            Map<String, Object> currentWarrantyObject = (Map<String, Object>) warranty.get(childKey);
                            Warranty currentWarranty = new Warranty(currentWarrantyObject.get("sellerName").toString(), currentWarrantyObject.get("sellerPhone").toString(),
                                    currentWarrantyObject.get("sellerEmail").toString(),currentWarrantyObject.get("dateOfPurchase").toString(), currentWarrantyObject.get("productName").toString(),
                                    currentWarrantyObject.get("productCategory").toString(),currentWarrantyObject.get("productPrice").toString(),currentWarrantyObject.get("pushid").toString());
                            Warranty decryptedWarranty = decrypt(currentWarranty, "zxcasdqwe");
                        warrantyList.add(decryptedWarranty);
                        }
                        Log.d(TAG, "onDataChange: " + warrantyList.get(0).getProductCategory());
                        WarrantyListAdapter adapter = new WarrantyListAdapter( mContext, R.layout.adapteritemlist, warrantyList);
                        mListView.setAdapter(adapter);
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
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
        startActivity(intent);
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);
    }

    private SecretKeySpec generateKey(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;

    }
}
