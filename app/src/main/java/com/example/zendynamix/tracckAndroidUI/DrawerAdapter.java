package com.example.zendynamix.tracckAndroidUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zendynamix on 7/8/2016.
 */
public class DrawerAdapter extends BaseAdapter{
     private final Context context;
    private List<DrawerData> drawerDatas=new ArrayList<>();
    public DrawerAdapter(Context context, List<DrawerData> list){
        this.context=context;
        drawerDatas=list;
    }
    @Override
    public int getCount() {

        return drawerDatas.size();
    }

    @Override
    public Object getItem(int position) {

        return drawerDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.drawer_item,parent,false);
        }else {
            view=convertView;
        }
        TextView menuText=(TextView)view.findViewById(R.id.menu_text);
        ImageView menuIcon=(ImageView)view.findViewById(R.id.menu_icon);

        menuText.setText(drawerDatas.get(position).getMenuItemText());
        menuIcon.setImageResource(drawerDatas.get(position).getMenuIcon());

        return view;
    }
}
