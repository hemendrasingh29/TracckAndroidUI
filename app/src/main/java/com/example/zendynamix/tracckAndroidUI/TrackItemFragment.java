package com.example.zendynamix.tracckAndroidUI;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zendynamix.tracckAndroidUI.helper.ConnectionHelper;
import com.example.zendynamix.tracckAndroidUI.helper.DividerItemDecoration;
import com.example.zendynamix.tracckAndroidUI.itemdatabase.ItemBaseHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by zendynamix on 7/6/2016.
 */
public class TrackItemFragment extends Fragment {
    private static final String LOG_TAG = "Track Item Fragment";
    private RecyclerView mRecyclerView;
    private ItemAdapter mItemAdapter;
    private Drawable dividerDrawable;
    private Context mContext;
    private List<ItemData> itemDatalst = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SQLiteDatabase msqLiteDatabase;
    private Paint p = new Paint();

    public static TrackItemFragment newInstance() {
        return new TrackItemFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        FetchProductData fetchProductData = new FetchProductData();
        fetchProductData.execute();
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
                TextView archive;
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
        dbCreate();
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        Button itemStatusButton;
        TextView textView;
        TextView retailer;
        ImageView imageView;

        public ItemHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = getAdapterPosition();
                    Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                    intent.putExtra("position", itemPosition);
                    intent.putExtra("list", (Serializable) itemDatalst);
                    startActivity(intent);
                }
            });
            imageView = (ImageView) view.findViewById(R.id.list_item_image_view);
            textView = (TextView) view.findViewById(R.id.list_item_text);
            retailer = (TextView) view.findViewById(R.id.retailer_name);
            itemStatusButton = (Button) view.findViewById(R.id.list_item_track_status_button);
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
            String itemName = itemData.getItemName();
            if (itemName.length() > 16) {
                String s = itemName.substring(0, 16);
                String itemNm = s + "...";
                holder.textView.setText(itemNm);
            }
            holder.retailer.setText(getString(R.string.retailer));
            holder.itemStatusButton.setText(itemData.getDeliveryStatus());
            String itemUri = itemData.getItemImageUri();
            if (itemUri != null) {
                Picasso.with(getContext()).load("http://api.tracck.com:4000/productimg/" + itemUri).into(holder.imageView);
            }
            String delivered = itemData.getDeliveryStatus();
            if (delivered.charAt(0) == 'D' || delivered.charAt(0) == 'd') {
                holder.itemStatusButton.setBackground(getResources().getDrawable(R.drawable.button_shape));
            } else {
                holder.itemStatusButton.setBackground(getResources().getDrawable(R.drawable.button_notdelivered));
            }
        }

        @Override
        public int getItemCount() {
            return itemDataLst.size();
        }

        public void removeItem(int position) {
            itemDataLst.remove(position);
            notifyItemRemoved(position);
        }
    }

    void dbCreate() {
        mContext = getActivity().getApplicationContext();
        msqLiteDatabase = new ItemBaseHelper(mContext).getWritableDatabase();

    }

    protected class FetchProductData extends AsyncTask<String, Void, List<ItemData>> {

        public FetchProductData() {
        }

        private List<ItemData> getProductDataFromJson(String jsonResponse)
                throws JSONException {

            Log.e(LOG_TAG, "JSON DATA>>>>" + jsonResponse);
            //OBJECTS
            final String ORDER_ID = "orderId";
            final String PRODUCT_IMAGE_URI = "productImage";
            final String PRODUCT_NAME = "productName";
            final String PRODUCT_DELIVERY_STATUS = "currentStatus";
            final String RETAILER_ID = "RetailerId";
            final String PRODUCT_ID = "productId";
            List<ItemData> result = new ArrayList<>();

            try {
                JSONArray productArray = new JSONArray(jsonResponse);
                for (int i = 0; i < productArray.length(); i++) {

                    JSONObject jsonItem = productArray.getJSONObject(i);
                    String orderId = jsonItem.getString(ORDER_ID);
                    String itemImageUri = jsonItem.getString(PRODUCT_IMAGE_URI);
                    String itemName = jsonItem.getString(PRODUCT_NAME);
                    String itemDeliveryStatus = jsonItem.getString(PRODUCT_DELIVERY_STATUS);
                    String retailerId = jsonItem.getString(RETAILER_ID);
                    String productId = jsonItem.getString(PRODUCT_ID);
                    ItemData data = new ItemData();

                    data.setOrderId(orderId);
                    data.setItemImageUri(itemImageUri);
                    data.setItemName(itemName);
                    data.setRetailer(retailerId);
                    data.setDeliveryStatus(itemDeliveryStatus);
                    data.setProductId(productId);

                    result.add(data);
                }

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error ", e);
            }
            return result;
        }

        @Override
        protected List<ItemData> doInBackground(String... param) {

            try {
                final String PP_BASE_URL = "http://api.tracck.com:4000/consumerItemList/7326422178";
                Uri builtUri = Uri.parse(PP_BASE_URL);

                return getProductDataFromJson(ConnectionHelper.fetch(builtUri.toString()));
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<ItemData> result) {
            if (result != null) {
                if (result.isEmpty()) {
                    Toast.makeText(getActivity(), "NO INTERNET", Toast.LENGTH_LONG).show();
                } else {
                    mItemAdapter.setmItemData(result);
                }
            }
        }
    }
}
