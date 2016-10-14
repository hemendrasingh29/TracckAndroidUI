package com.example.zendynamix.tracckAndroidUI.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.zendynamix.tracckAndroidUI.ItemData;
import com.example.zendynamix.tracckAndroidUI.R;
import com.example.zendynamix.tracckAndroidUI.TrackItemMainActivity;
import com.example.zendynamix.tracckAndroidUI.helper.ConnectionHelper;
import com.example.zendynamix.tracckAndroidUI.itemdatabase.ItemBaseHelper;
import com.example.zendynamix.tracckAndroidUI.itemdatabase.ItemContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class ItemSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = ItemSyncAdapter.class.getSimpleName();
    List<ItemData> archiveList = new ArrayList<>();
    List<ItemData> liveList = new ArrayList<>();
    ContentResolver mContentResolver;
    SQLiteDatabase mSqLiteDatabase;

    // Interval at which to sync with the item, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int ORDER_NOTIFICATION_ID = 3004;


    private static final String[] NOTIFY_ORDER_PROJECTION = new String[]{
            ItemContract.ConsumerOrder.Cols.ORDER_ID,
            ItemContract.ConsumerOrder.Cols.RETAILER_ID,
            ItemContract.ConsumerOrder.Cols.ORDER_DATE,
            ItemContract.ConsumerOrder.Cols.PRODUCT_DELIVERY_STATUS,
            ItemContract.ConsumerOrder.Cols.PRODUCT_NAME,
            ItemContract.ConsumerOrder.Cols.PRODUCT_ID,
            ItemContract.ConsumerOrder.Cols.ARCHIVE,
            ItemContract.ConsumerOrder.Cols.IMAGE_URI
    };

    // these indices must match the projection
    private static final int INDEX_ORDER_ID = 0;
    private static final int INDEX_RETAILER_ID = 1;
    private static final int INDEX_ORDER_DATE = 2;
    private static final int INDEX_PRODUCT_DELIVERY_STATUS = 3;
    private static final int INDEX_PRODUCT_NAME = 4;
    private static final int INDEX_PRODUCT_ID = 5;
    private static final int INDEX_ARCHIVE = 6;
    private static final int INDEX_IMAGE_URI = 7;

    public ItemSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");

        try {
            final String FORECAST_BASE_URL = "http://api.tracck.com:4000/consumerItemList/7326422178";
            Uri builtUri = Uri.parse(FORECAST_BASE_URL);

            getProductDataFromJson(ConnectionHelper.fetch(builtUri.toString()));
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    private List<ItemData> getProductDataFromJson(String jsonResponse)
            throws JSONException {
        //OBJECTS
        final String ORDER_ID = "orderId";
        final String RETAILER_ID = "RetailerId";
        final String ORDER_DATE = "orderDate";
        final String PRODUCT_DELIVERY_STATUS = "currentStatus";
        final String PRODUCT_NAME = "productName";
        final String PRODUCT_ID = "productId";
        final String ARCHIVE = "archive";
        final String PRODUCT_IMAGE_URI = "productImage";

        try {
            JSONArray productArray = new JSONArray(jsonResponse);
            Vector<ContentValues> cVVector = new Vector<ContentValues>(productArray.length());
            Vector<ContentValues> updateVector = new Vector<ContentValues>(productArray.length());

            for (int i = 0; i < productArray.length(); i++) {
                ContentValues contentValues = new ContentValues();
                JSONObject jsonItem = productArray.getJSONObject(i);

                try {
                    String orderId = jsonItem.getString(ORDER_ID);
                    String retailerId = jsonItem.getString(RETAILER_ID);
                    String orderDate = jsonItem.getString(ORDER_DATE).substring(0,10);
                    String itemDeliveryStatus = jsonItem.getString(PRODUCT_DELIVERY_STATUS);
                    String productName = jsonItem.getString(PRODUCT_NAME);
                    String productId = jsonItem.getString(PRODUCT_ID);
                    String archiveStatus = jsonItem.getString(ARCHIVE);
                    String productImageUri = jsonItem.getString(PRODUCT_IMAGE_URI);


                    contentValues.put(ItemContract.ConsumerOrder.Cols.ORDER_ID, orderId);
                    contentValues.put(ItemContract.ConsumerOrder.Cols.RETAILER_ID, retailerId);
                    contentValues.put(ItemContract.ConsumerOrder.Cols.ORDER_DATE, orderDate);
                    contentValues.put(ItemContract.ConsumerOrder.Cols.PRODUCT_DELIVERY_STATUS, itemDeliveryStatus);
                    contentValues.put(ItemContract.ConsumerOrder.Cols.PRODUCT_NAME, productName);
                    contentValues.put(ItemContract.ConsumerOrder.Cols.PRODUCT_ID, productId);
                    contentValues.put(ItemContract.ConsumerOrder.Cols.ARCHIVE, archiveStatus);
                    contentValues.put(ItemContract.ConsumerOrder.Cols.IMAGE_URI, productImageUri);


                    mSqLiteDatabase = new ItemBaseHelper(getContext()).getReadableDatabase();

                    String selection = ItemContract.ConsumerOrder.Cols.ORDER_ID + "=? and "
                            + ItemContract.ConsumerOrder.Cols.ORDER_DATE + "=? and "
                            + ItemContract.ConsumerOrder.Cols.PRODUCT_ID + "=?";
                    String[] args = {contentValues.getAsString(ORDER_ID),
                                      contentValues.getAsString(ORDER_DATE),
                                      contentValues.getAsString(PRODUCT_ID)};


                    Cursor cur = getContext().getContentResolver().query(ItemContract.ConsumerOrder.CONTENT_URI, null, selection, args, null);

                    if (cur.moveToFirst()) {
                        updateVector.add(contentValues);
                    } else {
                        cVVector.add(contentValues);
                    }


                } catch (JSONException e) {

                    Log.v(LOG_TAG, "********json error" + e);
                }

            }
            if (updateVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[updateVector.size()];
                updateVector.toArray(cvArray);
                for (int i = 0; i < updateVector.size(); i++) {
                    String[] params = new String[3];
                    params[0] = cvArray[i].getAsString(ItemContract.ConsumerOrder.Cols.ORDER_ID);
                    params[1] = cvArray[i].getAsString(ItemContract.ConsumerOrder.Cols.ORDER_DATE);
                    params[2] = cvArray[i].getAsString(ItemContract.ConsumerOrder.Cols.PRODUCT_ID);
                    getContext().getContentResolver().update(ItemContract.ConsumerOrder.CONTENT_URI, cvArray[i],
                            ItemContract.ConsumerOrder.Cols.ORDER_ID + "=?,and "
                                    + ItemContract.ConsumerOrder.Cols.ORDER_DATE + "=?,and "
                                       + ItemContract.ConsumerOrder.Cols.PRODUCT_ID + "=?", params);
                }

                notifyOrder();
            }
            int inserted = 0;
            // add to database
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);

                getContext().getContentResolver().bulkInsert(ItemContract.ConsumerOrder.CONTENT_URI, cvArray);

                // delete old data so we don't build up an endless history
//                getContext().getContentResolver().delete(ItemContract.ConsumerOrder.CONTENT_URI,);
//                        ItemContract.ConsumerOrder.Cols.COLUMN_DATE + " <= ?",
//                        new String[] {Long.toString(dayTime.setJulianDay(julianStartDay-1))});
                notifyOrder();
            }

            Log.d(LOG_TAG, "Sync Complete cv array. " + cVVector.size() + " Inserted");

        } catch (JSONException e) {

            Log.e(LOG_TAG, "Error ", e);
        }

        return liveList;
    }

    private void notifyOrder() {
        Context context = getContext();
        //checking the last update and notify if it' the first of the day
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String displayNotificationsKey = context.getString(R.string.pref_enable_notifications_key);
        boolean displayNotifications = prefs.getBoolean(displayNotificationsKey,
                Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));

        if (displayNotifications) {

            String lastNotificationKey = context.getString(R.string.pref_last_notification);
            long lastSync = prefs.getLong(lastNotificationKey, 0);

            if (System.currentTimeMillis() - lastSync >= DAY_IN_MILLIS) {
                // Last sync was more than 1 day ago, let's send a notification with item.

                Resources resources = context.getResources();
                String title = "TracckAndroidUI";
                // NotificationCompatBuilder is a very convenient way to build backward-compatible
                // notifications.  Just throw in some data.
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getContext())
                                .setColor(resources.getColor(R.color.black))
                                .setSmallIcon(R.drawable.tracck_logo)
                                .setContentTitle(title);


                // Make something interesting happen when the user clicks on the notification.
                // In this case, opening the app is sufficient.
                Intent resultIntent = new Intent(context, TrackItemMainActivity.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);

                NotificationManager mNotificationManager =
                        (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(ORDER_NOTIFICATION_ID, mBuilder.build());

                //refreshing last sync
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong(lastNotificationKey, System.currentTimeMillis());
                editor.commit();
            }

        }
    }


    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        ItemSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }


}