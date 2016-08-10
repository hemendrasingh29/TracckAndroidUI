package com.example.zendynamix.tracckAndroidUI;

/**
 * Created by zendynamix on 7/29/2016.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ProductPurchaseDetail extends Fragment {
    public static final String LOG_TAG = "Fragment One";
    private static final String PAY_MODE="payMode";
    private static final String TOTAL_AMT="totAmount";
    private static final String ORDER_DATE="orderDate";
    private TextView payModeV,totAmtV,orderDateV;
    String payModT,totAmtT,ordDateT;


    static ProductPurchaseDetail newInstance(String payMode, String totalAmt, String orderDate) {

        Bundle args=new Bundle();
        args.putString(PAY_MODE,payMode);
        args.putString(TOTAL_AMT,totalAmt);
        args.putString(ORDER_DATE,orderDate);
        ProductPurchaseDetail productPurchaseDetail =new ProductPurchaseDetail();
        productPurchaseDetail.setArguments(args);
        return productPurchaseDetail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         payModT=getArguments().getString(PAY_MODE);
         totAmtT=getArguments().getString(TOTAL_AMT);
         ordDateT=getArguments().getString(ORDER_DATE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.product_purchase, container, false);
        payModeV= (TextView) view.findViewById(R.id.pay_mode);
        totAmtV= (TextView) view.findViewById(R.id.total_amt);
        orderDateV= (TextView) view.findViewById(R.id.order_date);
        payModeV.setText("PAYMENT MODE:       "+payModT);
        totAmtV.setText("TOTAL COST:        "+totAmtT);
        orderDateV.setText("ORDER DATE:       "+ordDateT);
        return view;
    }

}