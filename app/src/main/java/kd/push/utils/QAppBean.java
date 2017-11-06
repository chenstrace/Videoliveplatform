package kd.push.utils;

import org.json.JSONObject;

public class QAppBean {
    private static final String TAG = "QAppBean";
    public int apkSize = 0;
    public String downloadUrl = "";
    public String logoUrl = "";
    public String msgId = "";
    public String pname = "";
    public String softId = "";
    public String softName = "";
    public int versionCode = 0;
    public String versionName = "";

    public QAppBean(String content) {
        parser(content);
    }

    public void print() {
        QLOG.debug(TAG, "softId: " + this.softId);
        QLOG.debug(TAG, "softName: " + this.softName);
        QLOG.debug(TAG, "logoUrl: " + this.logoUrl);
        QLOG.debug(TAG, "pname: " + this.pname);
        QLOG.debug(TAG, "versionCode: " + this.versionCode);
        QLOG.debug(TAG, "versionName: " + this.versionName);
        QLOG.debug(TAG, "apkSize: " + this.apkSize);
        QLOG.debug(TAG, "downloadUrl: " + this.downloadUrl);
    }

    private void parser(String content) {
        try {
            JSONObject json = new JSONObject(content);
            if (!json.isNull("si")) {
                this.softId = json.getString("si");
            }
            if (!json.isNull("sn")) {
                this.softName = json.getString("sn");
            }
            if (!json.isNull("lu")) {
                this.logoUrl = json.getString("lu");
            }
            if (!json.isNull("pn")) {
                this.pname = json.getString("pn");
            }
            if (!json.isNull("vc")) {
                this.versionCode = json.getInt("vc");
            }
            if (!json.isNull("vn")) {
                this.versionName = json.getString("vn");
            }
            if (!json.isNull("as")) {
                this.apkSize = json.getInt("as");
            }
            if (!json.isNull("du")) {
                this.downloadUrl = json.getString("du");
            }
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        } catch (Error err) {
            QLOG.error(TAG, err);
        }
    }
}
