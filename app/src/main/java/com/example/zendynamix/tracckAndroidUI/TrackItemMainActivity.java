package com.example.zendynamix.tracckAndroidUI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

public class TrackItemMainActivity extends SingleFragmentActivity implements LoginFragment.CallBacks {
    private static final String LOG = "Track Item Fragment";

    @Override
    protected Fragment createFragment() {
        return TrackItemFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onSubmitButton(String number) {

//        if (number.equals("999999999")) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, TrackItemFragment.newInstance()).commit();
//        } else {
//            Toast toast = Toast.makeText(this, "Check Number", Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
//            toast.show();
//        }
    }
}



