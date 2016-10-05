package com.example.zendynamix.tracckAndroidUI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zendynamix.tracckAndroidUI.helper.ConnectionHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zendynamix on 7/12/2016.
 */
public class ProductDetailFragment extends Fragment {
    private static final String LOG_TAG = ProductDetailFragment.class.getSimpleName();
    private static final String DIALOG = "DIALOG";
    private static final String DETAI_STATUS = "status";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int CAMERA_RESULT_OK = 0;
    private static final String INDEX = "index";
    private ItemData itemData;
    private List<ItemData> itemDatList = new ArrayList<>();
    private ImageView dImageViewDetail;
    private TextView dRetailerName, dDeliveryStatus;
    private TextView dItemTitle;
    private ImageView cameraButton;
    private Bitmap bitmap;
    private ImageView cameraImage;
    private Context context;
    private File photoFile;
    private Button sendButton;
    private String productId;
    private Button moreInfo;
    private List<ItemData> detailList = new ArrayList<>();
    private String orderId;
    private String payMethod, totalAmount, orderDate;
    private FrameLayout frameLayout;
    List<String> keyFeature, techSpecKey, techSpecValue, imageUrlL;

    private ImageView cameraImageView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            int position = bundle.getInt("position");
            itemDatList = (List<ItemData>) bundle.getSerializable("list");
            itemData = itemDatList.get(position);
            productId = itemData.getProductId();
            orderId = itemData.getOrderID();
            FetchProductDetailMoreData fetchProductDetailMoreData = new FetchProductDetailMoreData(productId);
            fetchProductDetailMoreData.execute();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.header, container, false);
        dImageViewDetail = (ImageView) view.findViewById(R.id.image_view_detail);
        final String imgUri = itemData.getItemImageUri();
        Picasso.with(getContext()).load("http://api.tracck.com:4000/productimg/" + imgUri).into(dImageViewDetail);
        dImageViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < detailList.size(); i++) {
                    itemData = detailList.get(i);
                }
                imageUrlL = itemData.getImageUrls();
                Intent intent = ImagePagerActivity.newIntent(getActivity(), imageUrlL);
                startActivity(intent);
            }
        });
        dItemTitle = (TextView) view.findViewById(R.id.detail_item_title);
        dRetailerName = (TextView) view.findViewById(R.id.detail_retailer_name);
        dDeliveryStatus = (TextView) view.findViewById(R.id.detail_delivery_status);
        // dItemTitle.setText(itemData.getItemName());
        dRetailerName.setText(getString(R.string.retailer));
        String dStatus = itemData.getDeliveryStatus();
        if (dStatus.charAt(0) == 'D' || dStatus.charAt(0) == 'd') {
            dDeliveryStatus.setTextColor(getResources().getColor(R.color.dark_green));
        } else {
            dDeliveryStatus.setTextColor(getResources().getColor(R.color.dark_yellow));
        }
        dDeliveryStatus.setText(itemData.getDeliveryStatus());

        moreInfo = (Button) view.findViewById(R.id.moreInfo);

        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = (String) moreInfo.getText();
                Log.v(LOG_TAG, "****************************************" + status);
                if (status.equals("More Detail")) {
                    moreInfo.setText("Less Detail");
                    startProdDetailFrag();
                }
                if (status.equals("Less Detail")) {
                    // moreInfo.setText("More Detail");
                    Fragment fragment = getFragmentManager().findFragmentById(R.id.inner_frame);
                    if (fragment.isVisible()) {
                        fragment.getView().setVisibility(View.GONE);
                    } else
                        fragment.getView().setVisibility(View.VISIBLE);
                }
            }
        });

//        if (dImageViewDetail != null) {
//            Animation hyperAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.hyperspace_jump);
//            dImageViewDetail.startAnimation(hyperAnimation);
//        }
        return view;
    }

    private void startProdDetailFrag() {
        for (int i = 0; i < detailList.size(); i++) {
            itemData = detailList.get(i);
        }
        keyFeature = itemData.getKeyLFeature();
        techSpecKey = itemData.getKeyL();
        techSpecValue = itemData.getValueL();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = getFragmentManager().findFragmentById(R.id.inner_frame);
        if (fragment == null) {
            fragmentManager.beginTransaction().replace(R.id.inner_frame, ProdAndPurchFragment.newInstance(keyFeature, techSpecKey, techSpecValue, payMethod, totalAmount, orderDate)).commit();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.show_title);
        menuItem.setTitle(itemData.getItemName());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // PRODUCT DETAIL TASK
    protected class FetchProductDetailMoreData extends AsyncTask<String, Void, List<ItemData>> {
        String pid;

        public FetchProductDetailMoreData(String productID) {
            pid = productID;
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
                final String FORECAST_BASE_URL = "http://api.tracck.com:4000/productDetail/amazonIndia/" + pid;
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
                if (result.isEmpty()) {
                    //   Toast.makeText(getActivity(), "NO INTERNET", Toast.LENGTH_LONG).show();
                }
                detailList.addAll(result);
                FetchPurchaseDetail fetchPurchaseDetail = new FetchPurchaseDetail(orderId);
                fetchPurchaseDetail.execute();
            }
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

                        itemData.setPaymentMethod(paymentMethod);
                        itemData.setOrderDate(orderDate);
                        itemData.setTotalAmount(totalAmount);
                        result.add(itemData);

                        JSONArray shipmentArray = orderDetail.getJSONArray(SHIPMENTS);
                        for (int k = 0; k < shipmentArray.length(); k++) {
                            JSONObject shipmentObject = shipmentArray.getJSONObject(k);
                            JSONArray activities = shipmentObject.getJSONArray(ACTIVITIES);
                            for (int l = 0; l < activities.length(); l++) {
                                JSONObject activitObj = activities.getJSONObject(l);
                                String name = activitObj.getString(LOCATION);
                                String evntTime = activitObj.getString(EVENT_TIME);
                                String activity = activitObj.getString(ACTIVITY);
                                String iD = activitObj.getString(_ID);
                                Log.e(LOG_TAG, "purchase detail**************************** json>>>>" + name);
                                Log.e(LOG_TAG, "purchase detail**************************** json>>>>" + evntTime);
                                Log.e(LOG_TAG, "purchase detail**************************** json>>>>" + activity);
                                Log.e(LOG_TAG, "purchase detail**************************** json>>>>" + iD);
                            }
                        }
                    }
                }

            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
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
        protected void onPostExecute(List<ItemData> itemDatas) {
            payMethod = itemData.getPaymentMethod();
            totalAmount = itemData.getTotalAmount();
            orderDate = itemData.getOrderDate();
        }
    }

}
