package com.example.zendynamix.tracckAndroidUI.itemdatabase;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.zendynamix.tracckAndroidUI.ItemData;
import com.example.zendynamix.tracckAndroidUI.itemdatabase.ItemContract.ItemEntry;

/**
 * Created by zendynamix on 8/9/2016.
 */
public class ItemCursorWrapper extends CursorWrapper{


    public static final String LOG_TAG=ItemCursorWrapper.class.getSimpleName();

    public  ItemCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public ItemData getItems(){
        String pId=getString(getColumnIndex(ItemEntry.Cols.PRODUCTID));
        String pName=getString(getColumnIndex(ItemEntry.Cols.PRODUCT_NAME));
        String pFeature=getString(getColumnIndex(ItemEntry.Cols.PRODUCT_KEY_FEATURE));
        String pImageUri=getString(getColumnIndex(ItemEntry.Cols.IMAGE_URI));
        String pKey=getString(getColumnIndex(ItemEntry.Cols.KEY));
        String pValue=getString(getColumnIndex(ItemEntry.Cols.VALUE));
        String pImageUrls=getString(getColumnIndex(ItemEntry.Cols.IMAGE_URLS));
        String pOrderId=getString(getColumnIndex(ItemEntry.Cols.ORDER_ID));
        String pOrderDate=getString(getColumnIndex(ItemEntry.Cols.ORDER_DATE));
        String paymentMode=getString(getColumnIndex(ItemEntry.Cols.PAYMET_METHOD));
        String pTotalAmt=getString(getColumnIndex(ItemEntry.Cols.TOTAL_AMT));
        String pRetailerId=getString(getColumnIndex(ItemEntry.Cols.RETAILER_ID));

        ItemData itemData=new ItemData();
        itemData.setProductId(pId);
        itemData.getProductName();
       // itemData.addKeyLFeature(pFeature);
        return itemData;
    }
}
