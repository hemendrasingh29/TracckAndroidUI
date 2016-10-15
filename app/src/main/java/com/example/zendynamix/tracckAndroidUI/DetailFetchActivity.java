package com.example.zendynamix.tracckAndroidUI;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.zendynamix.tracckAndroidUI.helper.ConnectionHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DetailFetchActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailFetchActivity.class.getSimpleName();
    private static final String IMAGE_KEY_TEC = "itemImageTec";
    private static final String ACTIVITYS_PURCH = "activityPurch";
    private static final String MAINLIST = "activityPurch";
    private static final String POSITION="position";
    private ItemData itemData;
    private List<ItemData> imgKeyTec = new ArrayList();
    private List<ItemData> activPurch = new ArrayList();
    private List<ItemData> positionList = new ArrayList<>();
    private List<String> keyf = new ArrayList<>();
    static private ProgressBar progressBar;

    private String orderId;
    private String prodId;

    int position;
    List<String> keyFeature, techSpecKey, techSpecValue, imageUrlL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datafetchactivity);
        progressBar= (ProgressBar) findViewById(R.id.progress_bar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            position = bundle.getInt("position");
            positionList = (List<ItemData>) bundle.getSerializable("list");
            itemData = positionList.get(position);
            orderId = itemData.getOrderID();
            prodId = itemData.getProductId();

        }
        FetchProductDetailMoreData fetchProductDetailMoreData = new FetchProductDetailMoreData(prodId);
        fetchProductDetailMoreData.execute();
    }

    // PRODUCT DETAIL TASK
    protected class FetchProductDetailMoreData extends AsyncTask<String, Void, List<ItemData>> {
        String prodId;

        public FetchProductDetailMoreData(String productID) {
            prodId = productID;
        }

        private List<ItemData> getProductDataFromJson(String jsonResponse)
                throws JSONException {

            Log.e(LOG_TAG, "JSON DATA>>>>" + jsonResponse);
            final String PRODUCT_NAME = "name";
            final String IMAGE_URLS = "imageUrls";
            final String KEY_FEATURE = "keyFeatures";
            final String TECH_SPECIFICATION = "techSpecification";
            final String KEY = "key";
            final String VALUE = "value";
            List<ItemData> result = new ArrayList<>();
            List<String> kFeature = new ArrayList<>();
            List<String> imgUrlsList = new ArrayList<>();
            List<String> techSpecKey = new ArrayList<>();
            List<String> techSpecValue = new ArrayList<>();
            ItemData data = new ItemData();
            try {

                JSONObject jsonObject = new JSONObject(jsonResponse);
                String prodName = jsonObject.getString(PRODUCT_NAME);
                data.setProductName(prodName);
                JSONArray imgUrls = jsonObject.optJSONArray(IMAGE_URLS);
                for (int i = 0; i < imgUrls.length(); i++) {
                    String imageUrl = imgUrls.getString(i);
                    imgUrlsList.add(imageUrl);
                    data.addImageUrls(imgUrlsList);
                    result.add(data);
                }
                JSONArray keyFeature = jsonObject.getJSONArray(KEY_FEATURE);
                for (int j = 0; j < keyFeature.length(); j++) {
                    String keyFea = keyFeature.getString(j);
                    kFeature.add(keyFea);
                    data.addKeyLFeature(kFeature);
                    result.add(data);
                }
                JSONArray techspec = jsonObject.getJSONArray(TECH_SPECIFICATION);
                for (int k = 0; k < techspec.length(); k++) {
                    JSONObject tspec = techspec.getJSONObject(k);
                    String key = tspec.getString(KEY);
                    String value = tspec.getString(VALUE);
                    techSpecKey.add(key);
                    techSpecValue.add(value);
                    data.addKeyL(techSpecKey);
                    data.addValueL(techSpecValue);
                    result.add(data);

                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error ", e);
            }
            return result;
        }

        @Override
        protected List<ItemData> doInBackground(String... param) {

            try {
                final String FORECAST_BASE_URL = "http://api.tracck.com:4000/productDetail/amazonIndia/" + prodId;
                Uri builtUri = Uri.parse(FORECAST_BASE_URL);
                return getProductDataFromJson(ConnectionHelper.fetch(builtUri.toString()));
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<ItemData> result) {
            if (result.size() > 0) {
                imgKeyTec.addAll(result);
            }
            FetchPurchaseDetail fetchPurchaseDetail = new FetchPurchaseDetail(orderId);
            fetchPurchaseDetail.execute();
        }
    }

    // PURCHASE DETAIL TASK
    protected class FetchPurchaseDetail extends AsyncTask<String, Void, List<ItemData>> {
        String orderID;

        public FetchPurchaseDetail(String ordId) {
            orderID = ordId;
        }

        private List<ItemData> getPurchasetDataFronJason(String jasonResponse) throws JSONException {

            Log.e(LOG_TAG, "purchase detail json>>>>" + jasonResponse);
            final String ORDERS = "orders";
            final String ACTIVITIES = "activities";
            final String SHIPMENTS = "shipments";
            final String PAYMENT_METHOD = "paymentMethod";
            final String ORDER_TOTAL_AMT = "orderTotalAmount";
            final String ORDER_DATE = "orderDate";
            final String LOCATION = "location";
            final String EVENT_TIME = "eventTime";
            final String ACTIVITY = "activity";
            final String _ID = "_id";

            List<ItemData> result = new ArrayList<>();
            List<String> locName = new ArrayList<>();
            List<String> evtTime = new ArrayList<>();
            List<String> oredActvty = new ArrayList<>();
            List<String> actID = new ArrayList<>();
            ItemData data= new ItemData();
            try {
                JSONArray jsonArray = new JSONArray(jasonResponse);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    JSONArray orderArray = jsonObject.getJSONArray(ORDERS);

                    for (int j = 0; j < orderArray.length(); j++) {
                        JSONObject orderDetail = orderArray.getJSONObject(j);
                        String paymentMethod = orderDetail.getString(PAYMENT_METHOD);
                        String totalAmount = orderDetail.getString(ORDER_TOTAL_AMT);
                        String orderDate = orderDetail.getString(ORDER_DATE);
                        data.setPaymentMethod(paymentMethod);
                        data.setOrderDate(orderDate);
                        data.setTotalAmount(totalAmount);
                        result.add(data);

                        JSONArray shipmentArray = orderDetail.getJSONArray(SHIPMENTS);
                        for (int k = 0; k < shipmentArray.length(); k++) {
                            JSONObject shipmentObject = shipmentArray.getJSONObject(k);
                            JSONArray activities = shipmentObject.getJSONArray(ACTIVITIES);
                            for (int l = 0; l < activities.length(); l++) {
                                JSONObject activitObj = activities.getJSONObject(l);

                                try {
                                    String locationName = activitObj.getString(LOCATION);
                                    String eventTime = activitObj.getString(EVENT_TIME);
                                    String activity = activitObj.getString(ACTIVITY);
                                    String activityId = activitObj.getString(_ID);
                                    locName.add(locationName);
                                    evtTime.add(eventTime);
                                    oredActvty.add(activity);
                                    actID.add(activityId);

                                    data.addLocationName(locName);
                                    data.addEventTime(evtTime);
                                    data.addOrderActvity(oredActvty);
                                    data.addActivityId(actID);
                                    result.add(data);

                                } catch (JSONException e) {
                                    Log.v(LOG_TAG, "jsonError" + e);
                                }
                            }
                        }
                    }
                }

            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }

            Log.v(LOG_TAG,"***---------------------**"+result.size());
            return result;
        }

        @Override
        protected List<ItemData> doInBackground(String... strings) {
            try {
                final String PURCHASE_BASE_URAL = "http://api.tracck.com:4000/consumerOrder/" + orderId;

                Uri buildUri = Uri.parse(PURCHASE_BASE_URAL);

                return getPurchasetDataFronJason(ConnectionHelper.fetch(buildUri.toString()));
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<ItemData> activePurch) {
            activPurch.addAll(activePurch);
            Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
            intent.putExtra("POSITION",position);
            intent.putExtra("IMAGE_KEY_TEC", (Serializable) imgKeyTec);
            intent.putExtra("ACTIVITYS_PURCH", (Serializable) activPurch);
            intent.putExtra("MAINLIST", (Serializable) positionList);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

}

