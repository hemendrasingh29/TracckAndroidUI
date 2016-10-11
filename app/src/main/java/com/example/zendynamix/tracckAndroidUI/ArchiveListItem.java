package com.example.zendynamix.tracckAndroidUI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zendynamix.tracckAndroidUI.helper.DividerItemDecoration;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by zendynamix on 10/3/2016.
 */
public class ArchiveListItem extends Fragment {

    private static final String LOG_TAG = "ARCHIVE_FRAGMENT";
    private static final String ARCHIVE_LIST = "archiveList";
    private RecyclerView mRecyclerView;
    private ItemAdapter mItemAdapter;
    private Drawable dividerDrawable;
    private Context mContext;
    private List<ItemData> itemDatalst;
    private Paint p = new Paint();
    private SwipeRefreshLayout swipeRefreshLayout;


    public static ArchiveListItem newInstance(List<ItemData> archList) {
        ArchiveListItem archiveListItem = new ArchiveListItem();
        Bundle args=new Bundle();
        archiveListItem.setArguments(args);
        args.putSerializable(ARCHIVE_LIST, (Serializable) archList);
        return archiveListItem;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemDatalst= (List<ItemData>)getArguments().getSerializable(ARCHIVE_LIST);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_track_item_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_list_item_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.divider);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        swipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateUI();
                mItemAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        updateUI();
        return view;
    }

    private void initSwape() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    mItemAdapter.removeItem(position);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_add);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_delete);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void updateUI() {
        mItemAdapter = new ItemAdapter(mContext, itemDatalst);
        mRecyclerView.setAdapter(mItemAdapter);
        initSwape();

    }


    private class ItemHolder extends RecyclerView.ViewHolder {
        Button archItemStatusButton;
        TextView archTextView;
        TextView archRetailer;
        ImageView archImageView;


        public ItemHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = getAdapterPosition();
                    Intent intent = new Intent(getActivity(),ProductDetailActivity.class);
                    intent.putExtra("position", itemPosition);
                    intent.putExtra("list", (Serializable) itemDatalst);
                    startActivity(intent);
                }
            });

            archImageView = (ImageView) view.findViewById(R.id.list_item_image_view);
            archTextView = (TextView) view.findViewById(R.id.list_item_text);
            archRetailer = (TextView) view.findViewById(R.id.retailer_name);
            archItemStatusButton = (Button) view.findViewById(R.id.list_item_track_status_button);
        }
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

        private List<ItemData> itemDataLst = new ArrayList<>();

        public void setmItemData(List<ItemData> result) {
            itemDataLst.clear();
            itemDataLst.addAll(result);
            initSwape();
            notifyDataSetChanged();
        }

        public ItemAdapter(Context context, List<ItemData> itemDatas) {
            itemDataLst = itemDatas;
            mContext = context;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.track_item_list, parent, false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            ItemData itemData = itemDataLst.get(position);

            String itemName = itemData.getProductName();
            if (itemName.length() > 16) {
                String s = itemName.substring(0, 16);
                String itemNm = s + "...";
                holder.archTextView.setText(itemNm);
            }
            String retailerId=itemData.getRetailerId();
            if(retailerId.equals("55fa4a2dcedbfb9516707ce7")){
                holder.archRetailer.setText(getString(R.string.retailer_amazon));
            }else{
                holder.archRetailer.setText(getString(R.string.retailer_flipkart));
            }
            holder.archItemStatusButton.setText(itemData.getDeliveryStatus());
            String itemUri = itemData.getProductImageUri();
            if (itemUri != null) {
                Picasso.with(getContext()).load("http://api.tracck.com:4000/productimg/" + itemUri).into(holder.archImageView);
            }

            String delivered = itemData.getDeliveryStatus();
            holder.archItemStatusButton.setText("archive");
//            if (delivered.charAt(0) == 'D' || delivered.charAt(0) == 'd') {
//                holder.archItemStatusButton.setBackground(getResources().getDrawable(R.drawable.button_shape));
//            } else {
//                holder.archItemStatusButton.setBackground(getResources().getDrawable(R.drawable.button_notdelivered));
//            }

        }

        public void removeItem(int position) {
            itemDataLst.remove(position);
            notifyItemRemoved(position);
        }
        @Override
        public int getItemCount() {
            return itemDataLst.size();
        }
    }
}

