package kd.push.utils;

import android.util.Log;
import kd.push.Constants;

public class QLOG {
    public static void debug(String TAG, String info) {
        if (Constants.logEnable) {
            Log.d(TAG, info);
        }
    }

    public static void warn(String TAG, String info) {
        if (Constants.logEnable) {
            Log.w(TAG, info);
        }
    }

    public static void error(String TAG, Exception ex) {
        if (Constants.logEnable) {
            ex.printStackTrace();
            Log.w(TAG, "ex: " + ex.toString());
        }
    }

    public static void error(String TAG, Error err) {
        if (Constants.logEnable) {
            err.printStackTrace();
            Log.e(TAG, "err: " + err.toString());
        }
    }

    public static void error(String TAG, String info) {
        if (Constants.logEnable) {
            Log.e(TAG, info);
        }
    }
}
