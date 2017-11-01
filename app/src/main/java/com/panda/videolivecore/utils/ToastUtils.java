package com.panda.videolivecore.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static boolean isDebug = true;
    public static Toast sToast;

    public static void show(Context context, String msg) {
        if (sToast != null) {
            sToast.cancel();
        }
        sToast = Toast.makeText(context, msg, 0);
        sToast.show();
    }

    public static void show(Context context, String msg, int duration) {
        if (sToast != null) {
            sToast.cancel();
        }
        sToast = Toast.makeText(context, msg, duration);
        sToast.show();
    }
}
