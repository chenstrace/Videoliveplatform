package com.panda.videolivecore.net.info;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RtmpDispatchInfo {
    public static String STREAM_HD = "live_panda_mid";
    public static String STREAM_OD = "live_panda_small";
    public static String STREAM_SD = "live_panda";
    public List<String> addrsBack = new ArrayList();
    public String strMainAddr = "";

    public void read(JSONObject obj) throws JSONException {
        this.strMainAddr = obj.getString("main");
        JSONArray arrayBack = obj.getJSONArray("back");
        for (int i = 0; i < arrayBack.length(); i++) {
            String strData = arrayBack.getString(i);
            if (!strData.isEmpty()) {
                this.addrsBack.add(strData);
            }
        }
    }

    public String getStreamAddress(String sign, String time) {
        String strReturn = this.strMainAddr;
        if (strReturn.isEmpty()) {
            for (int i = 0; i < this.addrsBack.size(); i++) {
                strReturn = (String) this.addrsBack.get(i);
                if (!strReturn.isEmpty()) {
                    break;
                }
            }
        }
        return strReturn + "?sign=" + sign + "&time=" + time;
    }
}
