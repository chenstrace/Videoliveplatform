package kd.push.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class QUtil {
    private static String ANDROID_DEVICE_MD5 = null;
    private static String ANDROID_ID = null;
    private static String ANDROID_IMEI_MD5 = null;
    public static final String BRANCH_ID = "qhpush";
    private static final String GLOBAL_RID_PATH = "/data/com/qihoo/stat/";
    private static final char[] SECRET = new char[]{'d', 'r', 'e', 'a', 'm', '3', '6', '0', '@', 'c', 'h', 'i', 'n', 'a'};
    private static String SN = null;
    private static String TAG = "QUtil";
    private static ActivityManager am = null;
    private static boolean bWifiAccess = false;
    private static ComponentName cn = null;
    private static final String[] pList = new String[]{"READ_PHONE_STATE", "INTERNET", "ACCESS_NETWORK_STATE", "GET_TASKS", "WRITE_EXTERNAL_STORAGE"};
    private static String pn = null;
    private static List<RunningAppProcessInfo> rap = null;

    protected static String getContextActivityName(Context ctx) {
        String activityName = "";
        try {
            String contextString = ctx.toString();
            activityName = contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        }
        return activityName;
    }

    protected static String getCurrentActivity(Context ctx) {
        String name = "";
        try {
            name = ((RunningTaskInfo) ((ActivityManager) ctx.getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getShortClassName().replace(".", "");
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        }
        return name;
    }

    public static String getApplicationName(Context ctx) {
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

    protected static void checkPermission(Context ctx) {
        try {
            String[] permission = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 4096).requestedPermissions;
            for (int index = 0; index < pList.length; index++) {
                boolean bExist = false;
                if (permission != null) {
                    for (String replace : permission) {
                        if (replace.replace("android.permission.", "").equals(pList[index])) {
                            bExist = true;
                            break;
                        }
                    }
                }
                if (!bExist) {
                    Toast.makeText(ctx, "您的程序没有声明android.permission." + pList[index] + "权限", 1).show();
                    break;
                }
            }
            if (permission != null) {
                for (String replace2 : permission) {
                    if (replace2.replace("android.permission.", "").equals("ACCESS_WIFI_STATE")) {
                        bWifiAccess = true;
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
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
        if ("QHOPENSDK_CHANNEL".equals(key)) {
            return QData.getChannel(ctx, value);
        }
        return value;
    }

    protected static String getMac(Context ctx) {
        String mac = "";
        try {
            if (!bWifiAccess) {
                return mac;
            }
            WifiManager wifiMgr = (WifiManager) ctx.getSystemService("wifi");
            WifiInfo info = wifiMgr == null ? null : wifiMgr.getConnectionInfo();
            if (info != null) {
                return info.getMacAddress();
            }
            return mac;
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
            return mac;
        } catch (Error err) {
            QLOG.error(TAG, err);
            return mac;
        }
    }

    protected static String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    protected static String getCountry() {
        return Locale.getDefault().getCountry();
    }

    protected static String getVersionName(Context ctx) {
        String version = "";
        try {
            return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
            return version;
        } catch (Error err) {
            QLOG.error(TAG, err);
            return version;
        }
    }

    protected static String getVersionCode(Context ctx) {
        String code = "";
        try {
            code = "" + ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionCode;
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        } catch (Error err) {
            QLOG.error(TAG, err);
        }
        return code;
    }

    public static int getVersionCode(Context ctx, String packName) {
        try {
            PackageInfo info = ctx.getPackageManager().getPackageInfo(packName, 0);
            if (info != null) {
                return info.versionCode;
            }
            return -1;
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
            return -1;
        } catch (Error err) {
            QLOG.error(TAG, err);
            return -1;
        }
    }

    public static boolean getNetAvailable(Context ctx) {
        ConnectivityManager connectivity = (ConnectivityManager) ctx.getSystemService("connectivity");
        if (connectivity == null || connectivity.getActiveNetworkInfo() == null) {
            return false;
        }
        return connectivity.getActiveNetworkInfo().isAvailable();
    }

    protected static String getNetType(Context ctx) {
        String typeName = "";
        try {
            NetworkInfo info = ((ConnectivityManager) ctx.getSystemService("connectivity")).getActiveNetworkInfo();
            if (info != null) {
                typeName = info.getTypeName().toLowerCase();
                if (!typeName.equals("wifi")) {
                    typeName = info.getExtraInfo().toLowerCase() + "-" + info.getSubtypeName().toLowerCase();
                }
            }
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        } catch (Error err) {
            QLOG.error(TAG, err);
        }
        return typeName;
    }

    protected static String getDeviceModel() {
        String model = "";
        try {
            model = Build.MODEL;
            if (model == null) {
                return "";
            }
            return model;
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
            return model;
        }
    }

    protected static String getDeviceBoard() {
        String board = "";
        try {
            board = Build.BOARD;
            if (board == null) {
                return "";
            }
            return board;
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
            return board;
        }
    }

    protected static String getDeviceBrand() {
        String board = "";
        try {
            board = Build.BRAND;
            if (board == null) {
                return "";
            }
            return board;
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
            return board;
        }
    }

    protected static String getDeviceManufacturer() {
        String manufacturer = "";
        try {
            manufacturer = Build.MANUFACTURER;
            if (manufacturer == null) {
                return "";
            }
            return manufacturer;
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
            return manufacturer;
        }
    }

    public static long readSDStorage() {
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                File sdcardDir = Environment.getExternalStorageDirectory();
                QLOG.debug(TAG, "The sdcard dir: " + sdcardDir.getPath());
                StatFs sf = new StatFs(sdcardDir.getPath());
                long freeSDSize = ((((long) sf.getAvailableBlocks()) * ((long) sf.getBlockSize())) / 1024) / 1024;
                QLOG.debug(TAG, "Free sdcard size:" + freeSDSize + "MB");
                return freeSDSize;
            }
            QLOG.warn(TAG, "The sdcard does not exist!");
            return 0;
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
            return 0;
        } catch (Error err) {
            QLOG.error(TAG, err);
            return 0;
        }
    }

    protected static String getDeviceOSVersion() {
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

    protected static String getImei(Context ctx) {
        String imei = "";
        try {
            imei = ((TelephonyManager) ctx.getSystemService("phone")).getDeviceId();
            if (imei == null) {
                return "";
            }
            return imei;
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
            return imei;
        }
    }

    protected static String getCpuName() {
        String cpu = "";
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            cpu = br.readLine().split(":\\s+", 2)[1];
            br.close();
            fr.close();
            return cpu;
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
            return cpu;
        } catch (Error err) {
            QLOG.error(TAG, err);
            return cpu;
        }
    }

    protected static String getOperator(Context ctx) {
        String operator = "";
        try {
            operator = ((TelephonyManager) ctx.getSystemService("phone")).getNetworkOperatorName();
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        }
        return operator;
    }

    protected static String getScreen(Context ctx) {
        String screen = "";
        DisplayMetrics dm = ctx.getApplicationContext().getResources().getDisplayMetrics();
        return "" + dm.widthPixels + "x" + dm.heightPixels;
    }

    protected static String getZone() {
        return "" + TimeZone.getDefault();
    }

    protected static String MD5(String s) {
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
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    protected static String generateSession(Context ctx) {
        int id = new Random().nextInt();
        if (id < 0) {
            id = -id;
        }
        String r = "000" + id;
        if (r.length() > 4) {
            r = r.substring(r.length() - 4);
        }
        long ts = java.lang.System.currentTimeMillis();
        long createTime = ts / 1000;
        String session = MD5(getMetaData(ctx, "QHOPENSDK_APPKEY") + ts + getImei(ctx) + r).toLowerCase();
        QLOG.debug(TAG, "Generate session: " + session + ", createTime: " + createTime);
        return session;
    }

    private static String readSDRid() {
        String rid = "";
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                String filePath = Environment.getExternalStorageDirectory().getPath() + GLOBAL_RID_PATH;
                File file = new File(filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                rid = getFileContent(filePath + "randId");
                file = new File(Environment.getExternalStorageDirectory().getPath() + "/data/com/" + BRANCH_ID + "/stat/");
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        } catch (Error err) {
            QLOG.error(TAG, err);
        }
        return rid;
    }

    private static void writeSDRid(String rid) {
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                String fileName = Environment.getExternalStorageDirectory().getPath() + GLOBAL_RID_PATH + "randId";
                if (TextUtils.isEmpty(getFileContent(fileName))) {
                    saveFile(fileName, rid);
                }
            }
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        } catch (Error err) {
            QLOG.error(TAG, err);
        }
    }

    protected static String refreshRid(Context ctx) {
        String rid = "";
        try {
            rid = readSDRid();
            if (TextUtils.isEmpty(rid)) {
                rid = QData.getPreferenceString(ctx, "randId", "");
                if (TextUtils.isEmpty(rid)) {
                    rid = getRandId();
                    QData.setPreference(ctx, "randId", rid);
                }
                writeSDRid(rid);
            }
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        } catch (Error err) {
            QLOG.error(TAG, err);
        }
        return rid;
    }

    protected static long getTtimes(Context ctx, String rid, String channel, String packname) {
        long i = QData.getPreferenceLong(ctx, "ttimes", 1);
        int s = 1;
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                String content = getFileContent(Environment.getExternalStorageDirectory().getPath() + "/data/com/" + BRANCH_ID + "/stat/" + rid + "_" + channel + "_" + packname);
                if (!TextUtils.isEmpty(content)) {
                    s = Integer.parseInt(content);
                }
            }
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        } catch (Error err) {
            QLOG.error(TAG, err);
        }
        if (((long) s) <= i) {
            return i;
        }
        i = (long) s;
        QData.setPreference(ctx, "ttimes", (long) s);
        return i;
    }

    protected static void saveTtimes(Context ctx) {
        try {
            long i = QData.getPreferenceLong(ctx, "ttimes", 1) + 1;
            QData.setPreference(ctx, "ttimes", i);
            QData.setPreference(ctx, "lastVersion", getVersionName(ctx));
            if ("mounted".equals(Environment.getExternalStorageState())) {
                saveFile(Environment.getExternalStorageDirectory().getPath() + "/data/com/" + BRANCH_ID + "/stat/" + refreshRid(ctx) + "_" + getMetaData(ctx, "QHOPENSDK_CHANNEL") + "_" + QDefine.getPackName(ctx), "" + i);
            }
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        } catch (Error err) {
            QLOG.error(TAG, err);
        }
    }

    protected static String getRandId() {
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

    protected static String getTransferId() {
        String rid = "";
        int id = new Random().nextInt();
        if (id < 0) {
            id = -id;
        }
        String r = "0000000" + id;
        if (r.length() > 8) {
            r = r.substring(r.length() - 8);
        }
        String s = "" + java.lang.System.currentTimeMillis();
        return getCurrentTime() + (s.substring(s.length() - 3) + r);
    }

    public static void saveFile(String fileName, String content) {
        if (!TextUtils.isEmpty(content)) {
            FileOutputStream output = null;
            try {
                FileOutputStream output2 = new FileOutputStream(fileName);
                try {
                    byte[] data = content.getBytes("UTF-8");
                    output2.write(data, 0, data.length);
                    output2.close();
                    output = null;
                    if (output != null) {
                        try {
                            output.close();
                        } catch (Exception ex2) {
                            QLOG.error(TAG, ex2);
                        }
                    }
                } catch (Exception e) {
                    output = output2;
                    try {
                        if (output != null) {
                            try {
                                output.close();
                            } catch (Exception ex22) {
                                QLOG.error(TAG, ex22);
                            }
                        }
                    } catch (Throwable th2) {
                        if (output != null) {
                            try {
                                output.close();
                            } catch (Exception ex222) {
                                QLOG.error(TAG, ex222);
                            }
                        }
                    }
                } catch (Throwable th3) {
                    output = output2;
                    if (output != null) {
                        output.close();
                    }
                }
            } catch (Exception e2) {
                if (output != null) {
                }
            }
        }
    }

    protected static void appendFile(String fileName, String content) {
        Exception ex;
        Throwable th;
        if (!TextUtils.isEmpty(content)) {
            FileOutputStream output = null;
            try {
                FileOutputStream output2 = new FileOutputStream(fileName, true);
                try {
                    byte[] data = content.getBytes("UTF-8");
                    output2.write(data, 0, data.length);
                    output2.close();
                    output = null;
                    if (output != null) {
                        try {
                            output.close();
                        } catch (Exception ex2) {
                            QLOG.error(TAG, ex2);
                        }
                    }
                } catch (Exception e) {
                    output = output2;
                    try {
                        if (output != null) {
                            try {
                                output.close();
                            } catch (Exception ex22) {
                                QLOG.error(TAG, ex22);
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (output != null) {
                            try {
                                output.close();
                            } catch (Exception ex222) {
                                QLOG.error(TAG, ex222);
                            }
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    output = output2;
                    if (output != null) {
                        output.close();
                    }
                }
            } catch (Exception e2) {
                if (output != null) {
                }
            }
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
                    String content2 = new String(buffer, 0, buffer.length, "UTF-8");
                    try {
                        input2.close();
                        input = null;
                        try {
                            if (file.length() > 20971520) {
                                file.delete();
                            }
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
                            content = content2;
                            if (input != null) {
                                input.close();
                            }
                            throw th;
                        }
                    } catch (Exception e2) {
                        input = input2;
                        content = content2;
                        if (input != null) {
                            input.close();
                        }
                        return content;
                    } catch (Throwable th4) {
                        th = th4;
                        input = input2;
                        content = content2;
                        if (input != null) {
                            input.close();
                        }
                        throw th;
                    }
                } catch (Exception e3) {
                    input = input2;
                    if (input != null) {
                        input.close();
                    }
                    return content;
                } catch (Throwable th5) {
                    th = th5;
                    input = input2;
                    if (input != null) {
                        input.close();
                    }
                }
                return content;
            }
            if (input != null) {
                try {
                    input.close();
                } catch (Exception ex2222) {
                    QLOG.error(TAG, ex2222);
                }
            }
            return content;
        } catch (Exception e4) {
            if (input != null) {
            }
            return content;
        }
    }

    protected static String getLogFile(Context ctx) {
        File dir = ctx.getFilesDir();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getPath() + "/" + BRANCH_ID + "_game_stat_log.json";
    }

    protected static String getExceptionFile(Context ctx) {
        File dir = ctx.getFilesDir();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getPath() + "/" + BRANCH_ID + "_game_stat_ex.log";
    }

    protected static int getAppStatus(Context ctx) {
        try {
            if (am == null) {
                am = (ActivityManager) ctx.getSystemService("activity");
            }
            cn = ((RunningTaskInfo) am.getRunningTasks(1).get(0)).topActivity;
            if (pn == null) {
                pn = ctx.getPackageName();
            }
            if (cn.getPackageName().equals(pn)) {
                return 100;
            }
            return -1;
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
            return -1;
        }
    }

    protected static String getCurrentDay() {
        Date d = new Date();
        int year = d.getYear() + 1900;
        int month = d.getMonth() + 1;
        int day = d.getDate();
        String ts = "" + year;
        if (month < 10) {
            ts = ts + "0" + month;
        } else {
            ts = ts + month;
        }
        if (day < 10) {
            return ts + "0" + day;
        }
        return ts + day;
    }

    private static String getCurrentTime() {
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

    protected static String encode(String str) {
        String code = "";
        try {
            byte[] de = str.getBytes();
            for (int i = 0; i < de.length; i++) {
                de[i] = (byte) (de[i] ^ SECRET[i % SECRET.length]);
            }
            code = Base64.encodeToString(de, 11);
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        }
        return code;
    }

    public static long getSeconds() {
        return java.lang.System.currentTimeMillis() / 1000;
    }

    protected static String decode(String code) {
        byte[] de = Base64.decode(code, 11);
        for (int i = 0; i < de.length; i++) {
            de[i] = (byte) (de[i] ^ SECRET[i % SECRET.length]);
        }
        return new String(de);
    }

    public static String getAndroidImeiMd5(Context ctx) {
        try {
            if (ANDROID_IMEI_MD5 == null && ctx != null) {
                ANDROID_IMEI_MD5 = MD5(getImei(ctx)).toLowerCase();
            }
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        }
        return ANDROID_IMEI_MD5;
    }

    private static String getSerialNumber() {
        if (SN == null) {
            try {
                Class<?> c = Class.forName("android.os.SystemProperties");
                SN = (String) c.getMethod("get", new Class[]{String.class}).invoke(c, new Object[]{"ro.serialno"});
            } catch (Exception ex) {
                QLOG.error(TAG, ex);
                SN = "";
            }
        }
        return SN;
    }

    private static String getAndroidId(ContentResolver resolver) {
        if (ANDROID_ID == null) {
            ANDROID_ID = System.getString(resolver, "android_id");
        }
        return ANDROID_ID;
    }

    public static String getAndroidDeviceMd5(Context ctx) {
        try {
            if (ANDROID_DEVICE_MD5 == null) {
                ANDROID_DEVICE_MD5 = MD5(getImei(ctx) + getSerialNumber() + getAndroidId(ctx.getContentResolver())).toLowerCase();
            }
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        }
        return ANDROID_DEVICE_MD5;
    }
}
