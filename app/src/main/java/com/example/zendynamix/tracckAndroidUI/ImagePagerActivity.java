package com.example.zendynamix.tracckAndroidUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import java.io.Serializable;
import java.util.List;

/**
 * Created by zendynamix on 6/23/2016.
 */
public class ImagePagerActivity extends FragmentActivity {
    private static final String LOG_TAG=ImagePagerActivity.class.getSimpleName();
    private static final String URI_LIST = "uriList";
    private List<String> imageUrls;
    private ViewPager viewPager;

    public static Intent newIntent(Context packageContext, List<String> url) {
        Intent intent = new Intent(packageContext, ImagePagerActivity.class);
        intent.putExtra(URI_LIST, (Serializable) url);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image_pager_activity);
        imageUrls = (List<String>) getIntent().getSerializableExtra(URI_LIST);
        viewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);

        FragmentManager fragmentManager = getSupportFragmentManager();

        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                String url= imageUrls.get(position);
                return LargeImageDialog.newInstance(url);
            }
            @Override
            public int getCount() {
                return imageUrls.size();
            }
        });
        for (int i = 0; i < imageUrls.size(); i++) {
             {
                viewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
