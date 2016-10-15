package com.example.zendynamix.tracckAndroidUI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zendynamix on 7/12/2016.
 */
public class ProductDetailFragment extends Fragment {
    private static final String LOG_TAG = ProductDetailFragment.class.getSimpleName();
    private static final String DIALOG = "DIALOG";
    private static final String KEY_FEATURE_LIST="keyFeature";
    private static final String TEC_SPEC_KAY_LIST="tecSpecKey";
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
    private List<ItemData> imageKeyTech;
    private List<ItemData> actiPurch;
    private List<ItemData> detailList = new ArrayList<>();
    private String orderId;
    private String payMethod, totalAmount, orderDate;
    private FrameLayout frameLayout;
    List<String> keyFeature, techSpecKey, techSpecValue, imageUrlL;
    private ImageView cameraImageView;
    private TextView activityLoaction;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            int position = bundle.getInt("POSITION");
            itemDatList = (List<ItemData>) bundle.getSerializable("MAINLIST");
            imageKeyTech = (List<ItemData>) bundle.getSerializable("IMAGE_KEY_TEC");
            actiPurch = (List<ItemData>) bundle.getSerializable("ACTIVITYS_PURCH");
            itemData = itemDatList.get(position);
            productId = itemData.getProductId();
            orderId = itemData.getOrderID();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.header, container, false);
        dImageViewDetail = (ImageView) view.findViewById(R.id.image_view_detail);
        final String imgUri = itemData.getProductImageUri();
        Picasso.with(getContext()).load("http://api.tracck.com:4000/productimg/" + imgUri).into(dImageViewDetail);
        dImageViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < imageKeyTech.size(); i++) {
                    itemData = imageKeyTech.get(i);
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
        String retailerId = itemData.getRetailerId();
        if (retailerId.equals("55fa4a2dcedbfb9516707ce7")) {
            dRetailerName.setText(getString(R.string.retailer_amazon));
        } else {
            dRetailerName.setText(getString(R.string.retailer_flipkart));
        }


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
                    startProdDetailFrag();
                    moreInfo.setText("Less Detail");
                } else {

                    Fragment fragment = getFragmentManager().findFragmentById(R.id.inner_frame);
                    if (fragment.isVisible()) {
                        fragment.getView().setVisibility(View.GONE);
                    } else
                        fragment.getView().setVisibility(View.VISIBLE);

                }
            }

        });

        return view;
    }

    void createActivtyFrag(){
    FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        ChatTrackstatusCameraimage chatTrackstatusCameraimage=new ChatTrackstatusCameraimage();
        fragmentManager.beginTransaction().replace(R.id.inner_frame,chatTrackstatusCameraimage).commit();
    }

    private void startProdDetailFrag() {
        for (int i = 0; i < imageKeyTech.size(); i++) {
            itemData = imageKeyTech.get(i);
        }
        keyFeature = itemData.getKeyLFeature();
        techSpecKey = itemData.getKeyL();
        techSpecValue = itemData.getValueL();
        for (int i = 0; i < actiPurch.size(); i++) {
            itemData = actiPurch.get(i);

            payMethod = itemData.getPaymentMethod();
            totalAmount = itemData.getTotalAmount();
            orderDate = itemData.getOrderDate();
        }

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        //  Fragment fragment = getFragmentManager().findFragmentById(R.id.inner_frame);
        // if () {
        fragmentManager.beginTransaction().replace(R.id.inner_frame, ProdAndPurchFragment.newInstance(keyFeature, techSpecKey, techSpecValue, payMethod, totalAmount, orderDate)).commit();
        // }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.show_title);
        menuItem.setTitle(itemData.getProductName());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
