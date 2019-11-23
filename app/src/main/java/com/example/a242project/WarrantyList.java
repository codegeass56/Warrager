package com.example.a242project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class WarrantyList extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warranty_list);
        mContext = this;
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

                Map<String, Object> warranty = (Map<String, Object>) dataSnapshot.getValue();

                for (String childKey: warranty.keySet()) {

                    Map<String, Object> currentWarrantyObject = (Map<String, Object>) warranty.get(childKey);
                    Warranty currentWarranty = new Warranty(currentWarrantyObject.get("sellerName").toString(), currentWarrantyObject.get("sellerPhone").toString(),
                            currentWarrantyObject.get("sellerEmail").toString(),currentWarrantyObject.get("dateOfPurchase").toString(), currentWarrantyObject.get("productName").toString(),
                            currentWarrantyObject.get("productCategory").toString(),currentWarrantyObject.get("productPrice").toString(),currentWarrantyObject.get("pushid").toString());
                    warrantyList.add(currentWarranty);
                }

                WarrantyListAdapter adapter = new WarrantyListAdapter( mContext, R.layout.adapteritemlist, warrantyList);
                Log.d(TAG, "onDataChange: " + mContext);
                mListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
