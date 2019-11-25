package com.example.a242project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

@SuppressWarnings("ALL")
class WarrantyListAdapter extends ArrayAdapter<Warranty> {
    private static final String TAG = "WarrantyListAdapter";
    private Context mContext;
    private int mResource;

    WarrantyListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Warranty> objects) {
        super(context, resource, objects);
        mContext = this.getContext();
        Log.d(TAG, "getView: " + mContext);
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String sellerName = getItem(position).getSellerName();
        String sellerPhone = getItem(position).getSellerPhone();
        String sellerEmail = getItem(position).getSellerEmail();
        String dateOfPurchase = getItem(position).getDateOfPurchase();
        String productName = getItem(position).getProductName();
        String productCategory = getItem(position).getProductCategory();
        String productPrice = getItem(position).getProductPrice();
        String pushid = getItem(position).getPushid();

        Warranty warranty = new Warranty(sellerName,sellerPhone,sellerEmail,dateOfPurchase,productName,productCategory,productPrice,pushid,"");

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView tvSellerName = (TextView) convertView.findViewById(R.id.tvSellerName);
        TextView tvSellerPhone = (TextView) convertView.findViewById(R.id.tvSellerPhone);
        TextView tvSellerEmail = (TextView) convertView.findViewById(R.id.tvSellerEmail);
        TextView tvDateOfPurchase = (TextView) convertView.findViewById(R.id.tvDateOfPurchase);
        TextView tvProductName = (TextView) convertView.findViewById(R.id.tvProductName);
        TextView tvCategory = (TextView) convertView.findViewById(R.id.tvProductCategory);
        TextView tvProductPrice = (TextView) convertView.findViewById(R.id.tvProductPrice);

        tvSellerName.setText(sellerName);
        tvSellerPhone.setText(sellerPhone);
        tvSellerEmail.setText(sellerEmail);
        tvDateOfPurchase.setText(dateOfPurchase);
        tvProductName.setText(productName);
        tvCategory.setText(productCategory);
        tvProductPrice.setText(productPrice);

        return convertView;

    }
}
