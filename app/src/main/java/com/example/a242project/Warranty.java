package com.example.a242project;

@SuppressWarnings("ALL")
class Warranty {
    public String sellerName;
    public String sellerPhone;
    public String sellerEmail;
    public String dateOfPurchase;
    public String productName;
    public String productCategory;
    public String productPrice;
    public String pushid;
    public String image;
    public String purchaseLocation;

    Warranty(){}
    Warranty(String sellerName, String sellerPhone, String sellerEmail, String dateOfPurchase, String productName, String productCategory, String productPrice, String pushid, String image,String purchaseLocation)
    {
        this.sellerEmail = sellerEmail;
        this.dateOfPurchase = dateOfPurchase;
        this.sellerName = sellerName;
        this.productCategory = productCategory;
        this.productName = productName;
        this.productPrice = productPrice;
        this.sellerPhone = sellerPhone;
        this.pushid = pushid;
        this.image = image;
        this.purchaseLocation = purchaseLocation;
    }

    public String getPurchaseLocation() {
        return purchaseLocation;
    }

    public void setPurchaseLocation(String purchaseLocation) {
        this.purchaseLocation = purchaseLocation;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



}
