package com.example.zendynamix.tracckAndroidUI.itemdatabase;

import android.provider.BaseColumns;

/**
 * Created by zendynamix on 8/9/2016.
 */
public final class ItemContract {
    public ItemContract() {
    }
    public static final class ItemEntry implements BaseColumns {
        public static final class ItemTable {
        }
        public static final String TABLE_NAME = "items";

        public static final class Cols {
            public static final String PRODUCTID = "productID";
            public static final String PRODUCT_NAME = "productName";
            public static final String PRODUCT_KEY_FEATURE = "productKeyFeature";
            public static final String IMAGE_URI = "imageUrl";
            public static final String KEY = "techfKey";
            public static final String VALUE = "techfValue";
            public static final String IMAGE_URLS = "imageUrls";
            public static final String ORDER_ID = "orderId";
            public static final String ORDER_DATE = "orderDate";
            public static final String PAYMET_METHOD = "paymentMethod";
            public static final String TOTAL_AMT = "totalAmount";
            public static final String RETAILER_ID = "retailerId";
        }
    }
}


