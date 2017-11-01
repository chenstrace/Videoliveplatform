package com.panda.videolivecore.utils;

import android.content.Context;

public class DpUtils {
    public static int Dp2Px(Context context, float dp) {
        return (int) ((dp * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int Px2Dp(Context context, float px) {
        return (int) ((px / context.getResources().getDisplayMetrics().density) + 0.5f);
    }
}
