package com.example.zendynamix.tracckAndroidUI.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ItemSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static ItemSyncAdapter itemSyncAdapterService = null;

    @Override
    public void onCreate() {
        Log.d("ItemSyncService", "onCreate - ItemSyncService");
        synchronized (sSyncAdapterLock) {
            if (itemSyncAdapterService == null) {
                itemSyncAdapterService = new ItemSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return itemSyncAdapterService.getSyncAdapterBinder();
    }
}