package com.example.zendynamix.tracckAndroidUI;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.zendynamix.tracckAndroidUI.itemdatabase.ItemBaseHelper;
import com.example.zendynamix.tracckAndroidUI.itemdatabase.ItemContract;

import java.util.ArrayList;
import java.util.List;

public class TrackItemMainActivity extends SingleFragmentActivity {
    private static final String LOG_TAG = "TRACK_ITEM_ACTIVITY";
    private SQLiteDatabase mcItemBaseHelper, mrItemBaseHelper, mdeleteHelper;
    private Context context;
    List<ItemData> archiveList = new ArrayList<>();
    List<ItemData> liveList = new ArrayList<>();
    Fragment fragment;

    @Override
    protected Fragment createFragment() {
        dbReade();
        return TabbedMainFragment.newInstance(liveList, archiveList);
    }
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public void onSubmitButton(String number) {
//
//        if (number.equals("999999999")) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, TabbedMainFragment.newInstance(liveList, archiveList)).commit();
//            dbReade();
//        } else {
//            Toast toast = Toast.makeText(this, "Check Number", Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
//            toast.show();
//        }
//    }


    void dbReade() {
        context = getApplicationContext();
        mrItemBaseHelper = new ItemBaseHelper(context).getReadableDatabase();

        String[] projection = new String[]{
                ItemContract.ConsumerOrder.Cols.ORDER_ID,
                ItemContract.ConsumerOrder.Cols.RETAILER_ID,
                ItemContract.ConsumerOrder.Cols.ORDER_DATE,
                ItemContract.ConsumerOrder.Cols.PRODUCT_DELIVERY_STATUS,
                ItemContract.ConsumerOrder.Cols.PRODUCT_NAME,
                ItemContract.ConsumerOrder.Cols.PRODUCT_ID,
                ItemContract.ConsumerOrder.Cols.ARCHIVE,
                ItemContract.ConsumerOrder.Cols.IMAGE_URI};

        Cursor cur = mrItemBaseHelper.query(ItemContract.ConsumerOrder.TABLE_NAME, projection, null, null, null, null, ItemContract.ConsumerOrder.Cols.ORDER_DATE + " DESC");
        if (cur.moveToFirst()) {
            while (cur.isAfterLast() == false) {
                ItemData data = new ItemData();
                String orderId = cur.getString(cur.getColumnIndexOrThrow(ItemContract.ConsumerOrder.Cols.ORDER_ID));
                String retailerId = cur.getString(cur.getColumnIndexOrThrow(ItemContract.ConsumerOrder.Cols.RETAILER_ID));
                String orderDate = cur.getString(cur.getColumnIndexOrThrow(ItemContract.ConsumerOrder.Cols.ORDER_DATE));
                String prodDeliverStatus = cur.getString(cur.getColumnIndexOrThrow(ItemContract.ConsumerOrder.Cols.PRODUCT_DELIVERY_STATUS));
                String productName = cur.getString(cur.getColumnIndexOrThrow(ItemContract.ConsumerOrder.Cols.PRODUCT_NAME));
                String productId = cur.getString(cur.getColumnIndexOrThrow(ItemContract.ConsumerOrder.Cols.PRODUCT_ID));
                String archiveStatus = cur.getString(cur.getColumnIndexOrThrow(ItemContract.ConsumerOrder.Cols.ARCHIVE));
                String imageUri = cur.getString(cur.getColumnIndexOrThrow(ItemContract.ConsumerOrder.Cols.IMAGE_URI));

                data.setOrderID(orderId);
                data.setRetailerId(retailerId);
                data.setOrderDate(orderDate);
                data.setDeliveryStatus(prodDeliverStatus);
                data.setProductName(productName);
                data.setProductId(productId);
                data.setArchive(archiveStatus);
                data.setProductImageUri(imageUri);

                if (archiveStatus.equals("true")) {
                    archiveList.add(data);
                } else {
                    liveList.add(data);

                }
                cur.moveToNext();
            }
            Log.v(LOG_TAG,"************-------------------archive----------"+archiveList.size()+"*****LIVE*****"+liveList.size());
            cur.close();
        }
    }
}




