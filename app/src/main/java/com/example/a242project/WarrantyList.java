package com.example.a242project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class WarrantyList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warranty_list);
        recyclerView = findViewById(R.id.list);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        fetch();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public TextView txtSellerName;
        public TextView txtSellerPhone;
        public TextView txtSellerEmail;
        public TextView txtDateOfPurchase;
        public TextView txtProductName;
        public TextView txtProductCategory;
        public TextView txtProductPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.list_root);
            txtSellerName = itemView.findViewById(R.id.sellerNameItem);
            txtSellerPhone = itemView.findViewById(R.id.sellerPhoneItem);
            txtSellerEmail = itemView.findViewById(R.id.sellerEmailItem);
            txtDateOfPurchase = itemView.findViewById(R.id.dateOfPurchaseItem);
            txtProductName = itemView.findViewById(R.id.productNameItem);
            txtProductCategory = itemView.findViewById(R.id.productCategoryItem);
            txtProductPrice = itemView.findViewById(R.id.ProductPriceItem);
        }

        public void setTxtSellerName(String string) {
            txtSellerName.setText(string);
        }

        public void setTxtSellerPhone(String string) {
            txtSellerPhone.setText(string);
        }

        public void setTxtSellerEmail(String string) {
            txtSellerEmail.setText(string);
        }

        public void setTxtDateOfPurchase(String string) {
            txtDateOfPurchase.setText(string);
        }

        public void setTxtProductName(String string) {
            txtProductName.setText(string);
        }

        public void setTxtProductCategory(String string) {
            txtProductCategory.setText(string);
        }

        public void setTxtProductPrice(String string) {
            txtProductPrice.setText(string);
        }
    }
    private void fetch() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        DatabaseReference warrantyRef = FirebaseDatabase
                .getInstance()
                .getReference("Warranty")
                .child(uid);

        FirebaseRecyclerOptions<Warranty> options =
                new FirebaseRecyclerOptions.Builder<Warranty>()
                        .setQuery(warrantyRef, new SnapshotParser<Warranty>() {
                            @NonNull
                            @Override
                            public Warranty parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Warranty(snapshot.child("sellerName").getValue().toString(),
                                        snapshot.child("sellerPhone").getValue().toString(),
                                        snapshot.child("sellerEmail").getValue().toString(),
                                        snapshot.child("dateOfPurchase").getValue().toString(),
                                        snapshot.child("productName").getValue().toString(),
                                        snapshot.child("productCategory").getValue().toString(),
                                        snapshot.child("productPrice").getValue().toString(),
                                        "");
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Warranty, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item, parent, false);

                return new ViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(ViewHolder holder, final int position, Warranty warrantyobj) {
                holder.setTxtSellerName(warrantyobj.getSellerName());
                holder.setTxtSellerPhone(warrantyobj.getSellerPhone());
                holder.setTxtSellerEmail(warrantyobj.getSellerEmail());
                holder.setTxtDateOfPurchase(warrantyobj.getDateOfPurchase());
                holder.setTxtProductName(warrantyobj.getProductName());
                holder.setTxtProductCategory(warrantyobj.getProductCategory());
                holder.setTxtProductPrice(warrantyobj.getProductPrice());

                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(WarrantyList.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
