package com.example.zendynamix.tracckAndroidUI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zendynamix on 7/29/2016.
 */
public class ProdAndPurchFragment extends Fragment {

    public ProdAndPurchFragment(){}
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static final String LOG_TAG = "prodAndpur";
    private static final String LIST_KEY_FEATURE = "keyFeature";
    private static final String LIST_TECHSPEC_KEY = "keyTechSpec";
    private static final String LIST_TECHSPEC_VALUE = "valueTechSpec";
    private List<String> keyFeaturelist, techSpecKey, techSpecValue;
    private static final String PAY_MODE = "payMode";
    private static final String TOTAL_AMT = "totalAmt";
    private static final String ORDER_DATE = "ordDate";
    String payMethod,totalAmount,orderDate;




    static ProdAndPurchFragment newInstance(List<String> keyFeaturelist, List<String> techSpecKey, List<String> techSpecValue, String payMod,String totAmt,String ordDate) {
        Bundle args = new Bundle();
        args.putStringArrayList(LIST_KEY_FEATURE, (ArrayList<String>) keyFeaturelist);
        args.putStringArrayList(LIST_TECHSPEC_KEY, (ArrayList<String>) techSpecKey);
        args.putStringArrayList(LIST_TECHSPEC_VALUE, (ArrayList<String>) techSpecValue);
        args.putString(PAY_MODE,payMod);
        args.putString(TOTAL_AMT,totAmt);
        args.putString(ORDER_DATE,ordDate);

        ProdAndPurchFragment prodAndPurchFragment = new ProdAndPurchFragment();
        prodAndPurchFragment.setArguments(args);
        return prodAndPurchFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.keyFeaturelist = getArguments().getStringArrayList(LIST_KEY_FEATURE);
        this.techSpecKey = getArguments().getStringArrayList(LIST_TECHSPEC_KEY);
        this.techSpecValue = getArguments().getStringArrayList(LIST_TECHSPEC_VALUE);
        this.payMethod=getArguments().getString(PAY_MODE);
        this.totalAmount=getArguments().getString(TOTAL_AMT);
        this.orderDate=getArguments().getString(ORDER_DATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.prod_and_purch_activity, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(ProductPurchaseDetail.newInstance(payMethod,totalAmount,orderDate), "PURCHASE DETAIL");
        adapter.addFragment(ProductFeatureDetail.newInstance(keyFeaturelist, techSpecKey, techSpecValue), "PRODUCT DETAIL");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}



