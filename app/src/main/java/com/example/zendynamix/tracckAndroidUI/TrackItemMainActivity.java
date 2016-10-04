package com.example.zendynamix.tracckAndroidUI;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.example.zendynamix.tracckAndroidUI.helper.ConnectionHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrackItemMainActivity extends SingleFragmentActivity {
    private static final String LOG_TAG = "TRACK_ITEM_ACTIVITY";
    private SQLiteDatabase mItemBaseHelper, mrItemBaseHelper, mdeleteHelper;
    private Context context;
    List<ItemData> archiveList = new ArrayList<>();
    List<ItemData> liveList = new ArrayList<>();
//    @Override
//    protected Fragment createFragment() {
//        return TabbedMainFragment.newInstance();
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public void onSubmitButton(String number) {

//        if (number.equals("999999999")) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, LiveListItem.newInstance()).commit();
//        } else {
//            Toast toast = Toast.makeText(this, "Check Number", Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
//            toast.show();
//        }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_fragment);
        FetchProductData fetchProductData = new FetchProductData();
        fetchProductData.execute();
        super.onCreate(savedInstanceState);
    }

    protected class FetchProductData extends AsyncTask<String, Void, List<ItemData>> {

        public FetchProductData() {
        }

        private List<ItemData> getProductDataFromJson(String jsonResponse)
                throws JSONException {
            //OBJECTS
            final String ORDER_ID="orderId";
            final String PRODUCT_IMAGE_URI = "productImage";
            final String PRODUCT_NAME = "productName";
            final String RETAILER_ID = "RetailerId";
            final String PRODUCT_ID="productId";
            final String PRODUCT_DELIVERY_STATUS = "currentStatus";
            final String ARCHIVE = "archive";

            try {
                JSONArray productArray = new JSONArray(jsonResponse);

                for (int i = 0; i < productArray.length(); i++) {
                    ItemData data = new ItemData();
                    ContentValues contentValues = new ContentValues();
                    JSONObject jsonItem = productArray.getJSONObject(i);

                    String orderID = jsonItem.getString(ORDER_ID);
                    String itemImageUri = jsonItem.getString(PRODUCT_IMAGE_URI);
                    String itemName = jsonItem.getString(PRODUCT_NAME);
                    String productID=jsonItem.getString(PRODUCT_ID);
                    String retailerId = jsonItem.getString(RETAILER_ID);
                    String itemDeliveryStatus = jsonItem.getString(PRODUCT_DELIVERY_STATUS);
                    String archiveStatus = jsonItem.getString(ARCHIVE);
                    if (itemDeliveryStatus == null) {
                        data.setDeliveryStatus("null");
                    } else {
                        data.setOrderID(orderID);
                        data.setRetailer(retailerId);
                        data.setProductId(productID);
                        data.setItemImageUri(itemImageUri);
                        data.setItemName(itemName);
                        data.setDeliveryStatus(itemDeliveryStatus);
                        data.setArchive(archiveStatus);

//                        contentValues.put(ItemContract.ConsumerOrder.Cols.IMAGE_URI, itemImageUri);
//                        contentValues.put(ItemContract.ConsumerOrder.Cols.PRODUCT_NAME, itemName);
//                        contentValues.put(ItemContract.ConsumerOrder.Cols.RETAILER_ID, retailerId);
//                        contentValues.put(ItemContract.ConsumerOrder.Cols.PRODUCT_DELIVERY_STATUS, itemDeliveryStatus);
//                        contentValues.put(ItemContract.ConsumerOrder.Cols.ARCHIVE, archiveStatus);
                        //  long rowId = mItemBaseHelper.insert(ItemContract.ConsumerOrder.TABLE_NAME, null, contentValues);
                        //  Log.e(LOG_TAG, "Row id" + rowId);
                        if (archiveStatus.equals("true") || archiveList.equals("")) {
                            archiveList.add(data);
                        } else {
                            liveList.add(data);
                        }
                    }
                }
            } catch (JSONException e) {

                Log.e(LOG_TAG, "Error ", e);
            }

            return liveList;
        }

        @Override
        protected List<ItemData> doInBackground(String... param) {

            try {
                final String FORECAST_BASE_URL = "http://api.tracck.com:4000/consumerItemList/7326422178";
                Uri builtUri = Uri.parse(FORECAST_BASE_URL);

                return getProductDataFromJson(ConnectionHelper.fetch(builtUri.toString()));
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<ItemData> result) {
            if (result != null) {
                if (result.size() < 1) {
                    Toast.makeText(getApplication(), "NO INTERNET", Toast.LENGTH_LONG).show();
                }
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

                if (fragment == null) {
                    fragmentManager.beginTransaction().add(R.id.fragment_container, TabbedMainFragment.newInstance(result, archiveList)).commit();
                }
            }
        }
    }

//    void dbReade() {
//        mrItemBaseHelper = new ItemBaseHelper(context).getReadableDatabase();
//        List<String> list = new ArrayList<>();
//        String[] projection = new String[]{
//                ItemContract.ConsumerOrder.Cols.IMAGE_URI,
//                ItemContract.ConsumerOrder.Cols.PRODUCT_NAME,
//                ItemContract.ConsumerOrder.Cols.RETAILER_ID,
//                ItemContract.ConsumerOrder.Cols.PRODUCT_DELIVERY_STATUS,
//                ItemContract.ConsumerOrder.Cols.ARCHIVE};
//
//        String selection = ItemContract.ConsumerOrder.Cols.RETAILER_ID + " = ?";
//        String[] selectionArgs = {"55fa4a2dcedbfb9516707ce7"};
//        Cursor cur = mrItemBaseHelper.query(ItemContract.ConsumerOrder.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
//        if (cur.moveToFirst()) {
//            while (cur.isAfterLast() == false) {
//                String mfId = cur.getString(cur.getColumnIndexOrThrow(ItemContract.ConsumerOrder.Cols.IMAGE_URI));
//                String mfName = cur.getString(cur.getColumnIndexOrThrow(ItemContract.ConsumerOrder.Cols.PRODUCT_NAME));
//                String mNumber = cur.getString(cur.getColumnIndexOrThrow(ItemContract.ConsumerOrder.Cols.RETAILER_ID));
//                String archive = cur.getString(cur.getColumnIndexOrThrow(ItemContract.ConsumerOrder.Cols.ARCHIVE));
//                //  Log.e(LOG_TAG, "JSON DATA>>>><<<<<<<<<<<RESULT " + mfId + " " + "   " + mfName + "   " + mNumber + " " + archive);
//                cur.moveToNext();
//            }
//        }
//        cur.close();
    }




