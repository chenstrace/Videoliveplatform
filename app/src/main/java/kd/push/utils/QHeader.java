package kd.push.utils;

import android.content.Context;
import android.text.TextUtils;
import kd.push.Constants;
import org.json.JSONObject;

public class QHeader {
    private static String TAG = "QHeader";
    public static final String os = "Android";
    private String alias = "";
    private String appId = "";
    private String appKey = "";
    private String appName = "";
    private String board = "";
    private String brand = "";
    private String channel = "";
    private String country = "";
    private String cpu = "";
    private String imei = "";
    private String language = "";
    private String lastVersion = "";
    private String m1 = "";
    private String m2 = "";
    private String mTag = "";
    private String mac = "";
    private String manufacturer = "";
    private String model = "";
    private String netType = "";
    private String operator = "";
    private String osVersion = "";
    private String packname = "";
    private String registerId = "";
    private String rid = "";
    private String sTag = "";
    private String screen = "";
    private String tags = "";
    private String tid = "";
    private long ttimes = 0;
    private String versionCode = "";
    private String versionName = "";

    protected QHeader(Context ctx, String registerId) {
        this.appName = QUtil.getApplicationName(ctx);
        this.appKey = QUtil.getMetaData(ctx, "QHOPENSDK_APPKEY");
        this.appId = QUtil.getMetaData(ctx, Constants.QHOPENSDK_APPID);
        this.channel = QUtil.getMetaData(ctx, "QHOPENSDK_CHANNEL");
        this.packname = QDefine.getPackName(ctx);
        this.versionName = QUtil.getVersionName(ctx);
        this.versionCode = QUtil.getVersionCode(ctx);
        this.netType = QUtil.getNetType(ctx);
        this.language = QUtil.getLanguage();
        this.country = QUtil.getCountry();
        this.model = QUtil.getDeviceModel();
        this.imei = QUtil.getImei(ctx);
        this.operator = QUtil.getOperator(ctx);
        this.screen = QUtil.getScreen(ctx);
        this.manufacturer = QUtil.getDeviceManufacturer();
        this.osVersion = QUtil.getDeviceOSVersion();
        this.cpu = QUtil.getCpuName();
        this.lastVersion = QData.getPreferenceString(ctx, "lastVersion", "");
        this.rid = QUtil.refreshRid(ctx);
        this.tid = QUtil.getTransferId();
        this.ttimes = QUtil.getTtimes(ctx, this.rid, this.channel, this.packname);
        this.board = QUtil.getDeviceBoard();
        this.brand = QUtil.getDeviceBrand();
        this.mac = QUtil.getMac(ctx);
        this.mTag = QSession.PRODUCT;
        this.sTag = QSession.PRODUCT;
        this.registerId = registerId;
        this.m1 = QUtil.getAndroidImeiMd5(ctx);
        this.m2 = QUtil.getAndroidDeviceMd5(ctx);
    }

    protected JSONObject convertJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("appName", this.appName);
            json.put("appKey", this.appKey);
            json.put("appId", this.appId);
            json.put("channel", this.channel);
            json.put("packname", this.packname);
            json.put("versionName", this.versionName);
            json.put("versionCode", this.versionCode);
            if (!TextUtils.isEmpty(this.lastVersion)) {
                json.put("lastVersion", this.lastVersion);
            }
            json.put("netType", this.netType);
            json.put("language", this.language);
            json.put("country", this.country);
            json.put("model", this.model);
            json.put("imei", this.imei);
            json.put("operator", this.operator);
            json.put("screen", this.screen);
            json.put("manufacturer", this.manufacturer);
            json.put("osVersion", this.osVersion);
            json.put("sdkVersion", QSession.sdkVersion);
            json.put("os", "Android");
            json.put("cpu", this.cpu);
            json.put("rid", this.rid);
            json.put("tid", this.tid);
            json.put("ttimes", this.ttimes);
            if (!TextUtils.isEmpty(this.mac)) {
                json.put("mac", this.mac);
            }
            json.put("board", this.board);
            json.put("brand", this.brand);
            json.put("mTag", this.mTag);
            json.put("sTag", this.sTag);
            json.put("m1", this.m1);
            json.put("m2", this.m2);
            json.put("registerId", this.registerId);
            if (!TextUtils.isEmpty(this.alias)) {
                json.put("alias", this.alias);
            }
            if (!TextUtils.isEmpty(this.tags)) {
                json.put("tags", this.tags);
            }
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        } catch (Error err) {
            QLOG.error(TAG, err);
        }
        return json;
    }

    protected void print() {
        QLOG.debug(TAG, "sdkVersion: 1.0.0");
        QLOG.debug(TAG, "os: Android");
        QLOG.debug(TAG, "appName: " + this.appName);
        QLOG.debug(TAG, "appKey: " + this.appKey);
        QLOG.debug(TAG, "appId: " + this.appId);
        QLOG.debug(TAG, "channel: " + this.channel);
        QLOG.debug(TAG, "packname: " + this.packname);
        QLOG.debug(TAG, "versionName: " + this.versionName);
        QLOG.debug(TAG, "lastVersion: " + this.lastVersion);
        QLOG.debug(TAG, "versionCode: " + this.versionCode);
        QLOG.debug(TAG, "netType: " + this.netType);
        QLOG.debug(TAG, "mac: " + this.mac);
        QLOG.debug(TAG, "language: " + this.language);
        QLOG.debug(TAG, "country: " + this.country);
        QLOG.debug(TAG, "model: " + this.model);
        QLOG.debug(TAG, "board: " + this.board);
        QLOG.debug(TAG, "imei: " + this.imei);
        QLOG.debug(TAG, "operator: " + this.operator);
        QLOG.debug(TAG, "screen: " + this.screen);
        QLOG.debug(TAG, "manufacturer: " + this.manufacturer);
        QLOG.debug(TAG, "osVersion: " + this.osVersion);
        QLOG.debug(TAG, "cpu: " + this.cpu);
        QLOG.debug(TAG, "rid: " + this.rid);
        QLOG.debug(TAG, "tid: " + this.tid);
        QLOG.debug(TAG, "mTag: " + this.mTag);
    }
}
