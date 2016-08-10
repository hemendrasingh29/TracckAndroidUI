package com.example.zendynamix.tracckAndroidUI.itemdatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.zendynamix.tracckAndroidUI.itemdatabase.ItemContract.ItemEntry;


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
        sqLiteDatabase.execSQL("CREATE TABLE "+ItemEntry.TABLE_NAME +"("+"_ID integer primary key autoincrement,"+
                ItemEntry.Cols.PRODUCTID + "," +
                ItemEntry.Cols.PRODUCT_NAME + "," +
                ItemEntry.Cols.PRODUCT_KEY_FEATURE + "," +
                ItemEntry.Cols.IMAGE_URI + "," +
                ItemEntry.Cols.KEY + "," +
                ItemEntry.Cols.VALUE + "," +
                ItemEntry.Cols.IMAGE_URLS + "," +
                ItemEntry.Cols.ORDER_ID + "," +
                ItemEntry.Cols.ORDER_DATE + "," +
                ItemEntry.Cols.PAYMET_METHOD + "," +
                ItemEntry.Cols.TOTAL_AMT + "," +
                ItemEntry.Cols.RETAILER_ID + ")");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }

}
