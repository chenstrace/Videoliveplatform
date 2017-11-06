package com.panda.videoliveplatform.chat;

import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.widget.TextView;

public class EmoticonsRexgexUtils {
    private static int getFontHeight(TextView tv) {
        Paint paint = new Paint();
        paint.setTextSize(tv.getTextSize());
        FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil((double) (fm.bottom - fm.top));
    }

    public static boolean setTextFace(android.content.Context r22, java.util.ArrayList<com.panda.videoliveplatform.chat.EmoticonBean> r23, android.widget.TextView r24, java.lang.String r25, java.lang.Object r26, int r27, int r28) {
       return false;
    }
}
