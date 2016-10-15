package com.example.zendynamix.tracckAndroidUI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by zendynamix on 10/12/2016.
 */
public class LaunchingActivity extends AppCompatActivity implements Animation.AnimationListener{
    ImageView imageViewLauncher;
    Animation animation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_activity);
      animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        animation.setAnimationListener(this);


        imageViewLauncher= (ImageView) findViewById(R.id.img_launcher);
        imageViewLauncher.startAnimation(animation);
        imageViewLauncher.setImageResource(R.drawable.launch_page_image);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(LaunchingActivity.this, TrackItemMainActivity.class);
                LaunchingActivity.this.startActivity(mainIntent);
                LaunchingActivity.this.finish();
            }
        }, 3000);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
