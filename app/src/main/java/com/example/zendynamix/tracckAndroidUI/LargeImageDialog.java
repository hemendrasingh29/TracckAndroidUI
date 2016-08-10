package com.example.zendynamix.tracckAndroidUI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by zendynamix on 8/5/2016.
 */
public class LargeImageDialog extends DialogFragment {
    private static final String LOG="large image";
    private static final String IMAGE_URI="imageUri";
    String imageUrl;
    ImageView imageView;

    static LargeImageDialog newInstance(String imgUri){
        Bundle bundle=new Bundle();
        bundle.putString(IMAGE_URI,imgUri);
       LargeImageDialog largeImageDialog=new LargeImageDialog();
        largeImageDialog.setArguments(bundle);
        return largeImageDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageUrl= getArguments().getString(IMAGE_URI);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.large_image_view,container,false);
        imageView= (ImageView) view.findViewById(R.id.limage_view);
        Picasso.with(getContext()).load(imageUrl).into(imageView);

        return view;
    }

}
