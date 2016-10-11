package com.example.zendynamix.tracckAndroidUI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.zendynamix.tracckAndroidUI.utils.PictureUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {
    private static final String LOG = "PRODUCT_DETAIL_ACTIVIY";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int CAMERA_RESULT_OK = 0;
    private File photoFile;
    Bitmap bitmap;
    private ItemData itemData = new ItemData();
    ImageView cameraImageView, cameraButton;
    Button sendButton;

    private List<ItemData> imageKeyTech = new ArrayList<>();
    private List<ItemData> activityPurch = new ArrayList<>();
    private List<ItemData> mainLst = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bundle bundle = getIntent().getExtras();
//        int position = bundle.getInt("POSITION");
//        imageKeyTech = (List<ItemData>) bundle.getSerializable("IMAGE_KEY_TEC");
//        activityPurch = (List<ItemData>) bundle.getSerializable("ACTIVITYS_PURCH");
//        mainLst = (List<ItemData>) bundle.getSerializable("MAINLIST");
//         itemData=mainLst.get(position);
//        String retailer=itemData.getRetailerId();

        //Log.v(LOG, "**********" + imageKeyTech.size()+"************"+retailer);


        photoFile = getPhotoFile(itemData);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (savedInstanceState == null) {
                ProductDetailFragment productDetailFragment = new ProductDetailFragment();

                getSupportFragmentManager().beginTransaction().add(R.id.container_header, productDetailFragment).commit();
            }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraImageView.setImageBitmap(null);
                photoFile.delete();
            }
        });
        cameraButton = (ImageView) findViewById(R.id.camera_image);
        cameraImageView = (ImageView) this.findViewById(R.id.camera_image_view);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
                updatePhotoView();
            }
        });
        updatePhotoView();

//        activityLoaction=(TextView) findViewById(R.id.activity_location);
//        activityLoaction.setText((CharSequence) itemData.getLocationName());
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean cantakePicture = photoFile != null &&
                takePictureIntent.resolveActivity(getPackageManager()) != null;
        cameraButton.setEnabled(cantakePicture);
        if (cantakePicture) {
            Uri uri = Uri.fromFile(photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == CAMERA_RESULT_OK) {
        }
    }

    public File getPhotoFile(ItemData itemData) {
        File externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir == null) {
            return null;
        }
        ItemData itemData1 = new ItemData();
        return new File(externalFilesDir, itemData1.getPhotoFilename());
    }

    private void updatePhotoView() {
        if (photoFile == null || !photoFile.exists()) {
            cameraImageView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getscaledBitmap(photoFile.getPath(), this);
            cameraImageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updatePhotoView();
    }
}
