package com.example.zendynamix.tracckAndroidUI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by zendynamix on 7/29/2016.
 */
public class ProdAndPurchActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prod_and_purch_activity);
        ProdAndPurchFragment prodAndPurchFragment = new ProdAndPurchFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.inner_frame);
        if (fragment == null) {
            fragmentManager.beginTransaction().add(R.id.inner_frame, prodAndPurchFragment).commit();
        }

    }
}
