package com.panda.videolivecore.SharePreference;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import com.panda.videolivecore.CoreApplication;
import java.util.List;
import java.util.Map;
//import utils.LogUtils;

public class SharePreferenceHelper {
    private static SharedPreferences sSharedpreferences;

    private static SharedPreferences getSharePreference() {
        if (sSharedpreferences == null) {
            sSharedpreferences = PreferenceManager.getDefaultSharedPreferences(CoreApplication.getInstance().getApplication());
        }
        return sSharedpreferences;
    }

    public static boolean save(String key, String value) {
        Editor editor = getSharePreference().edit();
        try {
            editor.putString(key, value);
        } catch (Exception e) {
            editor.putString(key, value);
            //LogUtils.e(e);
        }
        return editor.commit();
    }

    public static boolean clear() {
        return getSharePreference().edit().clear().commit();
    }

    public static String load(String key, String defValue) {
        String str = defValue;
        try {
            str = getSharePreference().getString(key, defValue);
        } catch (Exception e) {
            //LogUtils.e(e);
        }
        return str;
    }

    public static boolean save(String key, int value) {
        Editor editor = getSharePreference().edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static int getInt(String key, int defValue) {
        return getSharePreference().getInt(key, defValue);
    }

    public static boolean save(String key, float value) {
        Editor editor = getSharePreference().edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    public static float getFloat(String key, float defValue) {
        return getSharePreference().getFloat(key, defValue);
    }

    public static boolean save(String key, long value) {
        Editor editor = getSharePreference().edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public static long getLong(String key, long defValue) {
        return getSharePreference().getLong(key, defValue);
    }

    public static boolean save(String key, Boolean value) {
        Editor editor = getSharePreference().edit();
        editor.putBoolean(key, value.booleanValue());
        return editor.commit();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getSharePreference().getBoolean(key, defValue);
    }

    public static boolean save(String keyName, List<?> list) {
        int size = list.size();
        if (size < 1) {
            return false;
        }
        Editor editor = getSharePreference().edit();
        int i;
        if (list.get(0) instanceof String) {
            for (i = 0; i < size; i++) {
                editor.putString(keyName + i, (String) list.get(i));
            }
        } else if (list.get(0) instanceof Long) {
            for (i = 0; i < size; i++) {
                editor.putLong(keyName + i, ((Long) list.get(i)).longValue());
            }
        } else if (list.get(0) instanceof Float) {
            for (i = 0; i < size; i++) {
                editor.putFloat(keyName + i, ((Float) list.get(i)).floatValue());
            }
        } else if (list.get(0) instanceof Integer) {
            for (i = 0; i < size; i++) {
                editor.putLong(keyName + i, (long) ((Integer) list.get(i)).intValue());
            }
        } else if (list.get(0) instanceof Boolean) {
            for (i = 0; i < size; i++) {
                editor.putBoolean(keyName + i, ((Boolean) list.get(i)).booleanValue());
            }
        }
        return editor.commit();
    }

    public static Map<String, ?> loadAllSharePreference(String key) {
        return getSharePreference().getAll();
    }

    public static boolean removeKey(String key) {
        Editor editor = getSharePreference().edit();
        editor.remove(key);
        return editor.commit();
    }

    public static boolean removeAllKey() {
        Editor editor = getSharePreference().edit();
        editor.clear();
        return editor.commit();
    }

    public static boolean HasKey(String key) {
        return getSharePreference().contains(key);
    }
}
