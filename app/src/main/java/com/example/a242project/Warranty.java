package com.example.a242project;

import android.graphics.Bitmap;

@SuppressWarnings("ALL")
class Warranty {
    public String sellerName, sellerPhone, sellerEmail, dateOfPurchase, productName, productCategory, productPrice, pushid;

    public Bitmap receipt;
    Warranty(){}
    Warranty(String sellerName, String sellerPhone, String sellerEmail, String dateOfPurchase, String productName, String productCategory, String productPrice, String pushid)
    {
        this.sellerEmail = sellerEmail;
        this.dateOfPurchase = dateOfPurchase;
        this.sellerName = sellerName;
        this.productCategory = productCategory;
        this.productName = productName;
        this.productPrice = productPrice;
        this.sellerPhone = sellerPhone;
        this.pushid = pushid;
    }

    public Bitmap getReceipt() {
        return receipt;
    }

    public void setReceipt(Bitmap receipt) {
        this.receipt = receipt;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getPushid() {
        return pushid;
    }

    public void setPushid(String pushid) {
        this.pushid = pushid;
    }
}
