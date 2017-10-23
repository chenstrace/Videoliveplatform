package com.panda.videolivecore.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class ImeUtils {
    public static void hideSoftInputBox(Context context) {
        Activity activity = (Activity) context;
        InputMethodManager imm = (InputMethodManager) activity.getSystemService("input_method");
        if (imm.isActive() && activity.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void showSoftInputBox(Context context) {
        Activity activity = (Activity) context;
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService("input_method");
            imm.showSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(), 0);
            imm.toggleSoftInput(0, 2);
        }
    }
}
