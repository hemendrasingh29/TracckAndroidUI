package com.example.zendynamix.tracckAndroidUI.itemdatabase;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.zendynamix.tracckAndroidUI.itemdatabase.ItemContract.ConsumerOrder;


/**
 * Created by zendynamix on 8/9/2016.
 */
public class ItemProvider extends ContentProvider {

    private static final String LOG="ITEM PROVIDER";
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private ItemBaseHelper mOpenHelper;
    static final int ORDER = 200;

    static final int IMAGE_URI = 100;
    static final int PRODUCT_NAME = 101;
    static final int RETAILER_ID = 102;
    static final int PRODUCT_DELIVERY_STATUS = 103;
    static final int ARCHIVE = 104;

    private static final SQLiteQueryBuilder itemOrderByRetailerID;

    static {
        itemOrderByRetailerID = new SQLiteQueryBuilder();
    }


    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ItemContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, ItemContract.PATH_CONSUMER_ORDER, ORDER);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ItemBaseHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case ORDER:
                return ConsumerOrder.CONTENT_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            case ORDER: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ItemContract.ConsumerOrder.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
            Uri returnUri;
            switch (match) {
                case ORDER: {

                    long _id = db.insert(ItemContract.ConsumerOrder.TABLE_NAME,null, contentValues);
                    break;
                }
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
                    getContext().getContentResolver().notifyChange(uri, null);

        return null;
    }

//    private void normalizeDate(ContentValues values) {
//        // normalize the date value
//        if (values.containsKey(WeatherContract.WeatherEntry.COLUMN_DATE)) {
//            long dateValue = values.getAsLong(WeatherContract.WeatherEntry.COLUMN_DATE);
//            values.put(WeatherContract.WeatherEntry.COLUMN_DATE, WeatherContract.normalizeDate(dateValue));
//        }
//    }


    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase dbr= mOpenHelper.getReadableDatabase();
        switch (match) {
            case ORDER:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(ItemContract.ConsumerOrder.TABLE_NAME, null, value);
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String whereClause, String[] whereArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case ORDER: {

                long _id = db.update(ConsumerOrder.TABLE_NAME, contentValues, whereClause, whereArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return 1;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {

        return 0;
    }


}

