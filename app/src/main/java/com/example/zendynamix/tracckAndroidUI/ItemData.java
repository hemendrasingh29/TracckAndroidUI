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


    private  String orderID;
    private String retailerId;
    private String deliveryStatus;
    private String productName;
    private String productId;
    private String archive;
    private String productImageUri;
    private String paymentMethod;
    private String totalAmount;
    private String orderDate;



    private List<String> locationName=new ArrayList<>();
    private List<String> eventTime=new ArrayList<>();
    private List<String> orderActvity= new ArrayList<>();
    private List<String> activityId= new ArrayList<>();

    private List<String> keyLFeature = new ArrayList<>();
    private List<String> imageUrls = new ArrayList<>();
    private List<String> keyL = new ArrayList<>();
    private List<String> valueL = new ArrayList<>();



    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String trackStatus) {
        this.deliveryStatus = trackStatus;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailer) {
        this.retailerId = retailer;
    }


    public String getPhotoFilename() {
        return "IMG_" + getProductName() + ".jpg";
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

    public String getProductImageUri() {
        return productImageUri;
    }

    public void setProductImageUri(String productImageUri) {
        this.productImageUri = productImageUri;
    }

    public List<String> getEventTime() {
        return eventTime;
    }

    public void addEventTime(List<String> eventTime) {
        this.eventTime = eventTime;
    }

    public List<String> getActivityId() {
        return activityId;
    }

    public void addActivityId(List<String> activityId) {
        this.activityId = activityId;
    }

    public List<String> getLocationName() {
        return locationName;
    }

    public void addLocationName(List<String> locationName) {
        this.locationName = locationName;
    }

    public List<String> getOrderActvity() {
        return orderActvity;
    }

    public void addOrderActvity(List<String> orderActvity) {
        this.orderActvity = orderActvity;
    }
}


