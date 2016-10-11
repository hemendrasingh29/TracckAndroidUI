package com.example.zendynamix.tracckAndroidUI.itemdatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.zendynamix.tracckAndroidUI.itemdatabase.ItemContract.ConsumerOrder;


/**
 * Created by zendynamix on 8/9/2016.
 */
public class ItemBaseHelper extends SQLiteOpenHelper {


    public static final int VERSION = 1;

    private static final String DATABASE_NAME = "itemDataBase";

    public ItemBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE "+ConsumerOrder.TABLE_NAME +" ("+
                ConsumerOrder.Cols.ORDER_ID+","+
                ConsumerOrder.Cols.RETAILER_ID + "," +
                ConsumerOrder.Cols.ORDER_DATE + "," +
                ConsumerOrder.Cols.PRODUCT_DELIVERY_STATUS + "," +
                ConsumerOrder.Cols.PRODUCT_NAME + "," +
                ConsumerOrder.Cols.PRODUCT_ID  + "," +
                ConsumerOrder.Cols.ARCHIVE +","+
                ConsumerOrder.Cols.IMAGE_URI +",PRIMARY KEY ("+
                ConsumerOrder.Cols.PRODUCT_ID +","+
                ConsumerOrder.Cols.RETAILER_ID +","+
                ConsumerOrder.Cols.ORDER_DATE + "))");

//        sqLiteDatabase.execSQL("CREATE TABLE "+ RetailerEntry.TABLE_NAME+"("+RetailerEntry.Cols.RETAILER_ID +","+
//                RetailerEntry.Cols.RETAILER_SHORT_NAME +","+
//                RetailerEntry.Cols.RETAILER_FULL_NAME +","+
//                RetailerEntry.Cols.RETAILER_DISPLAY_NAME +")");
//
//        sqLiteDatabase.execSQL("CREATE TABLE "+ Product.TABLE_NAME+ "("+Product.Cols.PRODUCT_ID_RETAILER +","+
//                Product.Cols.PRODUCT_RETAILER+","+
//                Product.Cols.MODEL_NUMBER+","+
//                Product.Cols.MANUFACTURER_ID+","+
//                Product.Cols.MANUFACTURER_NAME+")");
//
//        sqLiteDatabase.execSQL("CREATE TABLE "+ Consumer.TABLE_NAME+ "("+Consumer.Cols.CONSUMER_ID+","+
//                Consumer.Cols.USER_NAME+","+
//                Consumer.Cols.PASSWORD+","+
//                Consumer.Cols.FIRST_NAME+","+
//                Consumer.Cols.LAST_NAME+","+
//                Consumer.Cols.EMAIL+","+
//                Consumer.Cols.PHONE_NUMBER+")");

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
