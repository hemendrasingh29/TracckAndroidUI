package com.example.zendynamix.tracckAndroidUI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zendynamix on 10/12/2016.
 */
public class ChatTrackstatusCameraimage extends Fragment{
    private TextView locationTview, eventTimeTview, activityTview, activityIdTview;
    private ImageView cameraImage;
    private ItemData itemData = new ItemData();
    List<ItemData> actpurch;

  static   ChatTrackstatusCameraimage newInstance(){

      ChatTrackstatusCameraimage chatTrackstatusCameraimage=new ChatTrackstatusCameraimage();
      return chatTrackstatusCameraimage;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getActivity().getIntent().getExtras();
        actpurch= (List<ItemData>) bundle.getSerializable("ACTIVITYS_PURCH");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.chat_trackstatus_cameraimage,container,false);
        cameraImage= (ImageView) view.findViewById(R.id.camera_image_view);
        eventTimeTview = (TextView) view.findViewById(R.id.event_time_textview);
 for(int i=0;i<actpurch.size();i++){
     itemData=actpurch.get(i);
 }
        List<String> activityTime=itemData.getEventTime();
                for (String s:activityTime){
                    eventTimeTview.append(s+"\n\n\n\n");
                }
        return view;
    }


}
