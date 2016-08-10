package com.example.zendynamix.tracckAndroidUI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ProductFeatureDetail extends Fragment {
    public static final String LOG_TAG = "prodAndpur";
    private static final String KEY_FEATURES = "keyFeature";
    private static final String TECH_SPEC_KEY_LIST = "techSpecKey";
    private static final String TECH_SPEC_VALUE_LIST = "techSpecValue";
    private TextView keyFeatureView;
    private TextView techSpecKeyView;
    private TextView techSpecValueView;


    private ArrayList<String> keyFeatures, techSpecKey, techSpecValue;


    static ProductFeatureDetail newInstance(List<String> keyFeatures, List<String> techSpecKey, List<String> techSpecValue) {
        ProductFeatureDetail productFeatureDetail = new ProductFeatureDetail();
        Bundle args = new Bundle();
        args.putStringArrayList(KEY_FEATURES, (ArrayList<String>) keyFeatures);
        args.putStringArrayList(TECH_SPEC_KEY_LIST, (ArrayList<String>) techSpecKey);
        args.putStringArrayList(TECH_SPEC_VALUE_LIST, (ArrayList<String>) techSpecValue);

        productFeatureDetail.setArguments(args);
        return productFeatureDetail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.keyFeatures = getArguments().getStringArrayList(KEY_FEATURES);
        this.techSpecKey = getArguments().getStringArrayList(TECH_SPEC_KEY_LIST);
        this.techSpecValue = getArguments().getStringArrayList(TECH_SPEC_VALUE_LIST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.product_feature, container, false);

        keyFeatureView = (TextView) view.findViewById(R.id.key_features);
        for (String s : keyFeatures) {
            keyFeatureView.append(s);
        }
        techSpecKeyView = (TextView) view.findViewById(R.id.key);
        for (String s : techSpecKey) {
            techSpecKeyView.append(s+"\n");
        }
        techSpecValueView = (TextView) view.findViewById(R.id.value);
        for (String s : techSpecValue) {
            techSpecValueView.append(s+"\n");
        }
        return view;
    }

}