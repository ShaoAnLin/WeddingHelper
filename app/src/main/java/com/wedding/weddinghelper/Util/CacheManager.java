package com.wedding.weddinghelper.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;

/**
 * Created by linshaoan on 2016/9/6.
 */
public class CacheManager {

    public static String HAVE_ACCOUNT_KEY = "HAVE_ACCOUNT_KEY";
    public static String OWN_NAME_KEY = "OWN_NAME_KEY";
    public static String JOIN_NAME_KEY = "JOIN_NAME_KEY";
    public static String OWN_PASSWORD_KEY = "OWN_PASSWORD_KEY";
    public static String JOIN_PASSWORD_KEY = "JOIN_PASSWORD_KEY";

    public static void writeString(Context context, final String KEY, String property) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SyncStateContract.Constants.CONTENT_DIRECTORY, context.MODE_PRIVATE).edit();
        editor.putString(KEY, property);
        editor.commit();
    }

    public static String readString(Context context, final String KEY) {
        return context.getSharedPreferences(SyncStateContract.Constants.CONTENT_DIRECTORY, context.MODE_PRIVATE).getString(KEY, null);
    }
}
