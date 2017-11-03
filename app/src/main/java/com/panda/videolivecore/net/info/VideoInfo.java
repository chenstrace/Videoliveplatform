package com.panda.videolivecore.net.info;

import android.util.JsonReader;
import com.panda.videolivecore.net.UrlConst;
import java.io.IOException;
import java.util.HashMap;

public class VideoInfo {
    public static String STATUS_BEGIN = "2";
    public static String STATUS_DEL = "4";
    public static String STATUS_END = "3";
    public static String STATUS_INIT = "1";
    public static String STREAM_HD = "HD";
    public static String STREAM_OD = "OD";
    public static String STREAM_SD = "SD";
    public static int STREAM_TYPE_360 = 1;
    public static String STREAM_TYPE_HD = "_mid";
    public static String STREAM_TYPE_OD = "_small";
    public static String STREAM_TYPE_SD = "";
    public static int STREAM_TYPE_UNKNOWN = 0;
    public String name = "";
    public String plFlag = "";
    public String roomKey = "";
    public String sign = "";
    public String status = "";
    public HashMap<String, String> streamAddrs = new HashMap();
    public String time = "0";
    public String ts = "";

    public void read(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String strName = reader.nextName();
            if ("name".equalsIgnoreCase(strName)) {
                this.name = reader.nextString();
            } else if ("time".equalsIgnoreCase(strName)) {
                this.time = reader.nextString();
            } else if ("stream_addr".equalsIgnoreCase(strName)) {
                readStreamAddr(reader);
            } else if ("room_key".equalsIgnoreCase(strName)) {
                this.roomKey = reader.nextString();
            } else if ("status".equalsIgnoreCase(strName)) {
                this.status = reader.nextString();
            } else if ("plflag".equalsIgnoreCase(strName)) {
                this.plFlag = reader.nextString();
            } else if ("ts".equalsIgnoreCase(strName)) {
                this.ts = reader.nextString();
            } else if ("sign".equalsIgnoreCase(strName)) {
                this.sign = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }

    protected void readStreamAddr(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String strName = reader.nextName();
            if (STREAM_HD.equalsIgnoreCase(strName)) {
                this.streamAddrs.put(STREAM_HD, reader.nextString());
            } else if (STREAM_OD.equalsIgnoreCase(strName)) {
                this.streamAddrs.put(STREAM_OD, reader.nextString());
            } else if (STREAM_SD.equalsIgnoreCase(strName)) {
                this.streamAddrs.put(STREAM_SD, reader.nextString());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }

    public boolean getStreamAddress(String strStreamType, StringBuffer strAddress, String sign, String time) {
        int nType = 0;
        String strNum = "";
        try {
            String[] strTemp = this.plFlag.split("_");
            if (strTemp.length > 0 && !strTemp[0].isEmpty()) {
                nType = Integer.parseInt(strTemp[0]);
            }
            if (strTemp.length > 1) {
                strNum = strTemp[1];
            }
            if (!(strNum.isEmpty() || this.roomKey.isEmpty())) {
                if (nType == STREAM_TYPE_360) {
                    return true;
                }
                strAddress.append(UrlConst.getLiveStreamUrl(strNum, this.roomKey, strStreamType, sign, time));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean getStreamStatus(String strKey) {
        String strValue = (String) this.streamAddrs.get(strKey);
        if (strValue == null || strValue.compareToIgnoreCase("1") != 0) {
            return false;
        }
        return true;
    }

    public boolean IsIniting() {
        return STATUS_INIT.compareToIgnoreCase(this.status) == 0 || STATUS_END.compareToIgnoreCase(this.status) == 0;
    }
}