package kd.push.utils;

import android.content.Context;

public class QPreference {
    private static final String PERFERENCES_NAME = "qihoo_kd_data";

    public static void setPreference(Context ctx, String key, String value) {
        ctx.getSharedPreferences(PERFERENCES_NAME, 0).edit().putString(key, value).commit();
    }

    public static String getPreference(Context ctx, String key, String defValue) {
        return ctx.getSharedPreferences(PERFERENCES_NAME, 0).getString(key, defValue);
    }
}
