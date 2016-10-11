package com.example.zendynamix.tracckAndroidUI.itemdatabase;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by zendynamix on 8/9/2016.
 */
public final class ItemContract {
    public static final String CONTENT_AUTHORITY = "com.example.zendynamix.tracckAndroidUI";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_CONSUMER_ORDER = "consumerOrder";

    private ItemContract() {
    }

    public static final class ConsumerOrder implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONSUMER_ORDER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONSUMER_ORDER;

        public static final String TABLE_NAME = "consumerOrder";

        public static final class Cols {
            public static final String ORDER_ID="orderId";
            public static final String RETAILER_ID = "retailerId";
            public static final String ORDER_DATE="orderDate";
            public static final String PRODUCT_DELIVERY_STATUS = "currentStatus";
            public static final String PRODUCT_NAME = "productName";
            public static final String PRODUCT_ID="productId";
            public static final String ARCHIVE = "archive";
            public static final String IMAGE_URI = "imageUrl";
        }
        public static Uri buildOrder() {
            return CONTENT_URI.buildUpon().appendPath(CONTENT_TYPE).build();
        }
    }

        public static final class RetailerEntry implements BaseColumns {
            public static final String TABLE_NAME = "retailer";

            public static final class Cols {
                public static final String RETAILER_ID = "retailerId";
                public static final String RETAILER_SHORT_NAME = "retSortName";
                public static final String RETAILER_FULL_NAME = "retFullName";
                public static final String RETAILER_DISPLAY_NAME = "retDisplyName";

            }
        }
//
//        public static final class Product implements BaseColumns {
//            public static final String TABLE_NAME = "product";
//
//            public static final class Cols {
//                public static final String PRODUCT_ID_RETAILER = "prodIdRetailer";
//                public static final String PRODUCT_RETAILER = "productRetailer";
//                public static final String MODEL_NUMBER = "modelNumber";
//                public static final String MANUFACTURER_ID = "manufacturerId";
//                public static final String MANUFACTURER_NAME = "manufacturerName";
//            }
//        }
//
//        public static final class Consumer implements BaseColumns {
//
//            public static final String TABLE_NAME = "consumer";
//
//            public static final class Cols {
//                public static final String CONSUMER_ID = "consumerId";
//                public static final String USER_NAME = "userName";
//                public static final String PASSWORD = "password";
//                public static final String FIRST_NAME = "firstName";
//                public static final String LAST_NAME = "lastName";
//                public static final String EMAIL = "email";
//                public static final String PHONE_NUMBER = "phoneNo";
//
//            }
//        }
}


