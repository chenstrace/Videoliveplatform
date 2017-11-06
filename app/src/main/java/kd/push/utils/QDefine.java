package kd.push.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import kd.push.Constants;

public class QDefine {
    public static final String APP_START_TYPE = "app_start";
    public static final String DOWN_FINISH_EVENT = "down_finish";
    public static final String DOWN_INSTALL_EVENT = "down_install";
    public static final String DOWN_START_EVENT = "down_start";
    private static char[] ENCODE_KEY = null;
    public static final String GET_STATUS_TYPE = "get_status";
    public static final String LOCAL_ACTION = "com.qihoo.iot.psdk.action.local";
    public static final long NET_ACCESS_PERIOD = 180000;
    public static final String NOTIFY_ACTION = "com.qihoo.iot.psdk.action.notify";
    public static final String NOTIFY_CLICK_EVENT = "notify_click";
    public static final long ONE_DAY = 86400000;
    public static final int ONE_HOUR = 3600000;
    public static final int ONE_MINUTE = 60000;
    public static final int ONE_SECOND = 1000;
    public static final String SET_ALIAS_TYPE = "set_alias";
    public static final String SET_TAGS_TYPE = "set_tags";
    private static final String TAG = "QDefine";
    public static final long WAP_ACCESS_PERIOD = 60000;
    public static final long WIFI_ACCESS_PERIOD = 300000;
    private static String encodeKey = "";

    public static String getImei(Context ctx) {
        String imei = "";
        if (ctx == null) {
            return imei;
        }
        imei = ((TelephonyManager) ctx.getSystemService("phone")).getDeviceId();
        if (imei == null) {
            return "";
        }
        return imei;
    }

    public static String getSN(Context ctx) {
        String sn = "";
        if (ctx == null) {
            return sn;
        }
        sn = ((TelephonyManager) ctx.getSystemService("phone")).getSimSerialNumber();
        if (sn == null) {
            return "";
        }
        return sn;
    }

    public static String getPackName(Context ctx) {
        String pkg = "";
        if (ctx != null) {
            return ctx.getPackageName();
        }
        return pkg;
    }

    public static String getAppName(Context ctx) {
        String appName = "";
        try {
            PackageManager manager = ctx.getPackageManager();
            return (String) manager.getApplicationLabel(manager.getApplicationInfo(ctx.getPackageName(), 0));
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
            return appName;
        } catch (Error err) {
            QLOG.error(TAG, err);
            return appName;
        }
    }

    public static String getModel() {
        String model = Build.MODEL;
        if (model == null) {
            model = "";
        }
        return model.replace(" ", "").replace(",", "");
    }

    public static String getOSVersion() {
        String osVersion = "";
        try {
            osVersion = VERSION.RELEASE;
            if (osVersion == null) {
                return "";
            }
            return osVersion;
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
            return osVersion;
        }
    }

    public static String getAndroidId(Context ctx) {
        String android_id = "";
        try {
            android_id = Secure.getString(ctx.getContentResolver(), "android_id");
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        }
        return android_id;
    }

    public static String MD5(String s) {
        String res = "";
        if (!TextUtils.isEmpty(s)) {
            char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            try {
                byte[] btInput = s.getBytes();
                MessageDigest mdInst = MessageDigest.getInstance("MD5");
                mdInst.update(btInput);
                char[] str = new char[(s.length() * 2)];
                int k = 0;
                for (byte byte0 : mdInst.digest()) {
                    int i = k + 1;
                    str[k] = hexDigits[(byte0 >>> 4) & 15];
                    k = i + 1;
                    str[i] = hexDigits[byte0 & 15];
                }
                res = new String(str);
            } catch (Exception ex) {
                QLOG.error(TAG, ex);
            }
        }
        QLOG.debug(TAG, "md5  input: " + s);
        QLOG.debug(TAG, "md5 output: " + res);
        return res;
    }

    public static String getRandId() {
        String rid = "";
        int seed = new Random().nextInt();
        if (seed <= 0 || seed > 1000) {
            seed = 1000;
        }
        long s = java.lang.System.currentTimeMillis() * ((long) seed);
        long id = new Random().nextLong();
        if (id < 0) {
            id = -id;
        }
        String r = "000000" + Long.toHexString(id);
        if (r.length() > 7) {
            r = r.substring(r.length() - 7);
        }
        rid = Long.toHexString(s) + r;
        if (rid.length() > 18) {
            rid = rid.substring(rid.length() - 18);
        }
        return rid.toUpperCase();
    }

    private static String generateKey() {
        int seed = new Random().nextInt();
        if (seed < 0) {
            seed = -seed;
        }
        String encodeKey = "00000" + seed;
        if (encodeKey.length() > 6) {
            return encodeKey.substring(encodeKey.length() - 6);
        }
        return encodeKey;
    }

    public static String getAppCacheDir() {
        String dir = "";
        try {
            dir = Environment.getExternalStorageDirectory().getPath() + "/appcache/";
            File file = new File(dir);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        } catch (Error err) {
            QLOG.error(TAG, err);
        }
        return dir;
    }

    public static String getVersion(Context ctx) {
        String version = "";
        if (ctx != null) {
            try {
                version = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
            } catch (Exception ex) {
                QLOG.error(TAG, ex);
            }
        }
        if (version == null) {
            version = "";
        }
        return version.replaceAll(" ", "").replace(",", "");
    }

    public static String getAn(Context ctx) {
        String an = "";
        DisplayMetrics dm = ctx.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        return "" + screenWidth + "x" + dm.heightPixels;
    }

    public static boolean networkAvailable(Context ctx) {
        ConnectivityManager connectivity = (ConnectivityManager) ctx.getSystemService("connectivity");
        if (connectivity == null || connectivity.getActiveNetworkInfo() == null) {
            return false;
        }
        return connectivity.getActiveNetworkInfo().isAvailable();
    }

    public static String getNetworkType(Context ctx) {
        String name = "";
        try {
            NetworkInfo activeNetInfo = ((ConnectivityManager) ctx.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetInfo != null && activeNetInfo.isAvailable()) {
                name = activeNetInfo.getExtraInfo();
            }
        } catch (Exception ex) {
            QLOG.debug(TAG, "ex: " + ex.toString());
        }
        return name;
    }

    public static boolean isWiFi(Context ctx) {
        NetworkInfo info = ((ConnectivityManager) ctx.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null || !"WIFI".equalsIgnoreCase(info.getTypeName())) {
            return false;
        }
        return true;
    }

    public static String getSSID(Context ctx) {
        String ssid = "";
        try {
            ssid = ((WifiManager) ctx.getSystemService("wifi")).getConnectionInfo().getSSID();
            if (ssid == null) {
                return "";
            }
            return ssid;
        } catch (Exception ex) {
            QLOG.debug(TAG, "ex: " + ex.toString());
            return ssid;
        }
    }

    public static String getMetaData(Context ctx, String key) {
        String value = "";
        try {
            ApplicationInfo info = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), 128);
            if (info != null) {
                try {
                    value = "" + info.metaData.get(key).toString();
                } catch (Exception ex) {
                    QLOG.error(TAG, ex);
                }
            }
        } catch (Exception ex2) {
            QLOG.error(TAG, ex2);
        }
        QLOG.debug(TAG, "meta " + key + ": " + value);
        return value;
    }

    public static void appendToFile(String str, String filename) {
        Exception ex;
        Throwable th;
        FileOutputStream stream = null;
        OutputStreamWriter writer = null;
        try {
            FileOutputStream stream2 = new FileOutputStream(filename, true);
            try {
                OutputStreamWriter writer2 = new OutputStreamWriter(stream2);
                try {
                    writer2.write(str);
                    writer2.close();
                    stream2.close();
                    if (writer2 != null) {
                        try {
                            writer2.close();
                        } catch (Exception ex2) {
                            QLOG.error(TAG, ex2);
                            writer = writer2;
                            stream = stream2;
                            return;
                        }
                    }
                    if (stream2 != null) {
                        stream2.close();
                    }
                    writer = writer2;
                    stream = stream2;
                } catch (Exception e) {
                    writer = writer2;
                    stream = stream2;
                    try {
                        if (writer != null) {
                            try {
                                writer.close();
                            } catch (Exception ex22) {
                                QLOG.error(TAG, ex22);
                                return;
                            }
                        }
                        if (stream == null) {
                            stream.close();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (writer != null) {
                            try {
                                writer.close();
                            } catch (Exception ex222) {
                                QLOG.error(TAG, ex222);
                                throw th;
                            }
                        }
                        if (stream != null) {
                            stream.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    writer = writer2;
                    stream = stream2;
                    if (writer != null) {
                        writer.close();
                    }
                    if (stream != null) {
                        stream.close();
                    }
                    throw th;
                }
            } catch (Exception e2) {
                stream = stream2;
                if (writer != null) {
                    writer.close();
                }
                if (stream == null) {
                    stream.close();
                }
            } catch (Throwable th4) {
                th = th4;
                stream = stream2;
                if (writer != null) {
                    writer.close();
                }
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (Exception e3) {
//            if (writer != null) {
//                writer.close();
//            }
//            if (stream == null) {
//                stream.close();
//            }
        }
    }

    public static String getFileContent(String filename) {
        Exception ex;
        Throwable th;
        String content = "";
        FileInputStream input = null;
        try {
            File file = new File(filename);
            if (file.exists()) {
                FileInputStream input2 = new FileInputStream(file);
                try {
                    byte[] buffer = new byte[((int) file.length())];
                    input2.read(buffer);
                    String content2 = new String(buffer, 0, buffer.length);
                    try {
                        input2.close();
                        input = null;
                        if (input != null) {
                            try {
                                input.close();
                            } catch (Exception ex2) {
                                QLOG.error(TAG, ex2);
                                content = content2;
                            }
                        }
                        content = content2;
                    } catch (Exception e) {
                        input = input2;
                        content = content2;
                        try {
                            if (input != null) {
                                try {
                                    input.close();
                                } catch (Exception ex22) {
                                    QLOG.error(TAG, ex22);
                                }
                            }
                            return content;
                        } catch (Throwable th2) {
                            th = th2;
                            if (input != null) {
                                try {
                                    input.close();
                                } catch (Exception ex222) {
                                    QLOG.error(TAG, ex222);
                                }
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        input = input2;
                        content = content2;
                        if (input != null) {
                            input.close();
                        }
                        throw th;
                    }
                } catch (Exception e2) {
                    input = input2;
                    if (input != null) {
                        input.close();
                    }
                    return content;
                } catch (Throwable th4) {
                    th = th4;
                    input = input2;
                    if (input != null) {
                        input.close();
                    }
                }
                return content;
            }
            QLOG.debug(TAG, filename + " does not exist!");
            if (input != null) {
                try {
                    input.close();
                } catch (Exception ex2222) {
                    QLOG.error(TAG, ex2222);
                }
            }
            return content;
        } catch (Exception e3) {
            if (input != null) {
            }
            return content;
        }
    }

    public static String getTime() {
        String ts = "";
        try {
            ts = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        } catch (Error err) {
            QLOG.error(TAG, err);
        }
        return ts;
    }

    public static String getCurrentTime() {
        Date d = new Date();
        int year = d.getYear() + 1900;
        int month = d.getMonth() + 1;
        int day = d.getDate();
        int hours = d.getHours();
        int minutes = d.getMinutes();
        int seconds = d.getSeconds();
        String ts = "" + year;
        if (month < 10) {
            ts = ts + "0" + month;
        } else {
            ts = ts + month;
        }
        if (day < 10) {
            ts = ts + "0" + day;
        } else {
            ts = ts + day;
        }
        if (hours < 10) {
            ts = ts + "0" + hours;
        } else {
            ts = ts + hours;
        }
        if (minutes < 10) {
            ts = ts + "0" + minutes;
        } else {
            ts = ts + minutes;
        }
        if (seconds < 10) {
            return ts + "0" + seconds;
        }
        return ts + seconds;
    }

    public static String registerSdId(Context ctx, String str) {
        String regId = "";
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                String filePath = Environment.getExternalStorageDirectory().getPath() + "/data/com/" + QUtil.BRANCH_ID + "/regId/";
                File file = new File(filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                String fileName = filePath + ctx.getPackageName();
                if (str != null) {
                    QUtil.saveFile(fileName, str);
                } else {
                    regId = QUtil.getFileContent(fileName);
                    QLOG.debug(TAG, "fileName: " + fileName + ", regId: " + regId);
                }
            } else {
                QLOG.debug(TAG, "SD card is oos");
            }
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        } catch (Error err) {
            QLOG.error(TAG, err);
        }
        return regId;
    }

    public static String getEncodeKey(Context ctx) {
        try {
            encodeKey = getMetaData(ctx, Constants.QHOPENSDK_APPID);
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        } catch (Error err) {
            QLOG.error(TAG, err);
        }
        return encodeKey;
    }

    public static void broadcastToLocal(Context ctx, String type, String data) {
        Intent intent = new Intent();
        intent.setAction(LOCAL_ACTION);
        intent.setPackage(ctx.getPackageName());
        intent.putExtra("targetPack", ctx.getPackageName());
        intent.putExtra("type", type);
        intent.putExtra("data", data);
        QLOG.debug(TAG, LOCAL_ACTION + type);
        ctx.sendBroadcast(intent);
    }

    public static void broadcastToLocal(Context ctx, String type, String data, String appPackName, int appVersionCode) {
        Intent intent = new Intent();
        intent.setAction(LOCAL_ACTION);
        intent.setPackage(ctx.getPackageName());
        intent.putExtra("targetPack", ctx.getPackageName());
        intent.putExtra("type", type);
        intent.putExtra("data", data);
        intent.putExtra("appPackName", appPackName);
        intent.putExtra("appVersionCode", appVersionCode);
        ctx.sendBroadcast(intent);
    }

    public static String encode(String str) {
        String code = "";
        try {
            int i;
            if (ENCODE_KEY == null) {
                ENCODE_KEY = new char[encodeKey.length()];
                for (i = 0; i < encodeKey.length(); i++) {
                    ENCODE_KEY[i] = encodeKey.charAt(i);
                }
            }
            byte[] de = str.getBytes();
            for (i = 0; i < de.length; i++) {
                de[i] = (byte) (de[i] ^ ENCODE_KEY[i % ENCODE_KEY.length]);
            }
            code = Base64.encodeToString(de, 11);
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        }
        return code;
    }

    public static String decode(String code) {
        int i;
        if (ENCODE_KEY == null) {
            ENCODE_KEY = new char[encodeKey.length()];
            for (i = 0; i < encodeKey.length(); i++) {
                ENCODE_KEY[i] = encodeKey.charAt(i);
            }
        }
        byte[] de = Base64.decode(code, 11);
        for (i = 0; i < de.length; i++) {
            de[i] = (byte) (de[i] ^ ENCODE_KEY[i % ENCODE_KEY.length]);
        }
        return new String(de);
    }

    public static String getTokenID(Context context) {
        if (context == null) {
            return null;
        }
        String imei = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        String AndroidID = System.getString(context.getContentResolver(), "android_id");
        return getMD5code(imei + AndroidID + getDeviceSerial());
    }

    private static String getDeviceSerial() {
        String serial = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            return (String) c.getMethod("get", new Class[]{String.class}).invoke(c, new Object[]{"ro.serialno"});
        } catch (Exception e) {
            return serial;
        }
    }

    private static String getMD5code(String string) {
        String str = null;
        if (!TextUtils.isEmpty(string)) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(string.getBytes());
                byte[] b = md.digest();
                StringBuffer buf = new StringBuffer("");
                for (int i : b) {
                    int i2=0;
                    if (i2 < 0) {
                        i2 += 256;
                    }
                    if (i2 < 16) {
                        buf.append("0");
                    }
                    buf.append(Integer.toHexString(i2));
                }
                str = buf.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }
}
