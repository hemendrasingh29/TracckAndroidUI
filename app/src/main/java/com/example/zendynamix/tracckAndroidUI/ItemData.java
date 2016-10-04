package com.example.zendynamix.tracckAndroidUI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zendynamix on 7/6/2016.
 * test2
 */
public class ItemData implements Serializable {
    private static final String LOG = "Login activity>>>>";

    private String itemName;
    private String deliveryStatus;
    private String itemImageUri;
    private String retailer;
    private String productId;
    private String productName;
    private  String orderID;
    private String paymentMethod;
    private String totalAmount;
    private String orderDate;
    private String archive;


    private List<String> keyLFeature = new ArrayList<>();
    private List<String> imageUrls = new ArrayList<>();
    private List<String> keyL = new ArrayList<>();
    private List<String> valueL = new ArrayList<>();

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String item) {
        this.itemName = item;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String trackStatus) {
        this.deliveryStatus = trackStatus;
    }

    public String getRetailer() {
        return retailer;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }

    public String getItemImageUri() {
        return itemImageUri;
    }

    public void setItemImageUri(String itemImageUri) {
        this.itemImageUri = itemImageUri;
    }

    public String getPhotoFilename() {
        return "IMG_" + getItemName() + ".jpg";
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {return productName;}
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<String> getValueL() {
        return valueL;
    }

    public void addValueL(List<String> valueL) {
        this.valueL = valueL;
    }

    public List<String> getKeyL() {
        return keyL;
    }

    public void addKeyL(List<String> keyL) {
        this.keyL = keyL;
    }

    public List<String> getKeyLFeature() {
        return keyLFeature;
    }

    public void addKeyLFeature(List<String> keyLFeature) {
        this.keyLFeature = keyLFeature;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<String> getImageUrls() {return imageUrls;}

    public void addImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getArchive() {
        return archive;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
}


