package com.panda.videolivecore.net;

import java.math.BigDecimal;

public class NumericUtils {
    public static String getLivePeople(String livetext) {
        try {
            double livenum = Double.parseDouble(livetext);
            if (livenum >= 1.0E8d) {
                return removeZero(new BigDecimal(String.valueOf(livenum / 1.0E8d)).setScale(1, 4).toString()) + "亿";
            } else if (livenum < 10000.0d) {
                return String.valueOf((int) livenum);
            } else {
                return removeZero(new BigDecimal(String.valueOf(livenum / 10000.0d)).setScale(1, 4).toString()) + "万";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String removeZero(String livetext) {
        if (livetext != null) {
            try {
                if (livetext.endsWith(".0")) {
                    livetext = livetext.substring(0, livetext.length() - 2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return livetext;
    }
}
