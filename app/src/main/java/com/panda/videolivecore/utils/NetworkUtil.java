package com.panda.videolivecore.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkUtil {
    static final String regEx = "[\\u4e00-\\u9fa5]";

    public enum NetState {
        NET_NO,
        NET_2G,
        NET_3G,
        NET_4G,
        NET_WIFI,
        NET_UNKNOWN
    }

    public static int getChineseCount(String str) {
        int count = 0;
        Matcher m = Pattern.compile(regEx).matcher(str);
        while (m.find()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                count++;
            }
        }
        return count;
    }

    public static boolean mobileMumVerify(String phoneNum) {
        return Pattern.compile("^((13[0-9])|(15[^4,\\D])|(170)|(18[0-9])|(14[5,7]))\\d{8}$").matcher(phoneNum).matches();
    }

    public static boolean nickNameVerify(String nickname) {
        return Pattern.compile("^[\\u4e00-\\u9fa5A-Za-z0-9_]*$").matcher(nickname).matches();
    }

    public static boolean isNetConnected(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        if (cm == null) {
            return false;
        }
        NetworkInfo[] infos = cm.getAllNetworkInfo();
        if (infos == null) {
            return false;
        }
        for (NetworkInfo ni : infos) {
            if (ni.isConnected()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWifiConnected(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        if (cm == null) {
            return false;
        }
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || networkInfo.getType() != 1) {
            return false;
        }
        return true;
    }

    public static boolean is3gConnected(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        if (cm == null) {
            return false;
        }
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || networkInfo.getType() != 0) {
            return false;
        }
        return true;
    }

    public static NetState getNetStateType(NetworkInfo ni) {
        NetState stateCode = NetState.NET_UNKNOWN;
        switch (ni.getType()) {
            case 0:
                switch (ni.getSubtype()) {
                    case 1:
                    case 2:
                    case 4:
                    case 7:
                    case 11:
                        return NetState.NET_2G;
                    case 3:
                    case 5:
                    case 6:
                    case 8:
                    case 9:
                    case 10:
                    case 12:
                    case 14:
                    case 15:
                        return NetState.NET_3G;
                    case 13:
                        return NetState.NET_4G;
                    default:
                        return NetState.NET_UNKNOWN;
                }
            case 1:
                return NetState.NET_WIFI;
            default:
                return NetState.NET_UNKNOWN;
        }
    }

    public static boolean CheckWifiNetworkState(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null || !info.isAvailable() || getNetStateType(info) == NetState.NET_WIFI) {
            return true;
        }
        return false;
    }

    public static int GetNetworkType(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            return -1;
        }
        NetState state = getNetStateType(info);
        if (state == NetState.NET_NO) {
            return 0;
        }
        if (state == NetState.NET_WIFI) {
            return 1;
        }
        if (state == NetState.NET_2G) {
            return 2;
        }
        if (state == NetState.NET_3G) {
            return 3;
        }
        if (state == NetState.NET_4G) {
            return 4;
        }
        if (state == NetState.NET_UNKNOWN) {
            return -1;
        }
        return -1;
    }
}
