package com.example.zendynamix.tracckAndroidUI.helper;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zendynamix on 7/13/2016.
 */

// FETCH JSON DATA HELPER
public class ConnectionHelper {
    private final static String LOG_TAG = ConnectionHelper.class.getSimpleName();

    private ConnectionHelper() {
    }
    public static String fetch(String urlString) {
        HttpURLConnection urlConnection = null;
        try {

            URL url = new URL(urlString);
            Log.v(LOG_TAG, "*****" + url);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                // buffer for debugging.
                buffer.append(line + "\n");
            }
            reader.close();
            String jmovieString = buffer.toString();
            Log.e(LOG_TAG, "jsondata" + jmovieString);
            return jmovieString;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return "";
    }
}
