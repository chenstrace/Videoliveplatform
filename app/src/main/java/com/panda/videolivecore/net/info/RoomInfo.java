package com.panda.videolivecore.net.info;

import android.util.JsonReader;
import com.panda.videolivecore.net.NumericUtils;
import java.io.IOException;
import java.util.Random;

public class RoomInfo {
    public String bulletin = "";
    public String fans = "";
    public String id = "";
    public String name = "";
    public String personNum = "";
    public String type = "0";

    public void read(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String strName = reader.nextName();
            if ("id".equalsIgnoreCase(strName)) {
                this.id = reader.nextString();
            } else if ("name".equalsIgnoreCase(strName)) {
                this.name = reader.nextString();
            } else if ("type".equalsIgnoreCase(strName)) {
                this.type = reader.nextString();
            } else if ("bulletin".equalsIgnoreCase(strName)) {
                this.bulletin = reader.nextString();
            } else if ("fans".equalsIgnoreCase(strName)) {
                this.fans = reader.nextString();
            } else if ("person_num".equalsIgnoreCase(strName)) {
                this.personNum = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }

    public int getFans() {
        int nfans = 0;
        try {
            nfans = Integer.parseInt(this.fans);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nfans;
    }

    public void setFans(int nFans) {
        if (nFans < 0) {
            nFans = 0;
        }
        this.fans = getFansText(nFans);
    }

    public String getFansText() {
        return getFansText(this.fans);
    }

    public String getFansTextFormat() {
        return String.format("粉丝：%s", new Object[]{NumericUtils.getLivePeople(this.fans)});
    }

    public static String getFansText(int nFans) {
        return String.format("%d", new Object[]{Integer.valueOf(nFans)});
    }

    public static String getFansText(String strFans) {
        return String.format("%s", new Object[]{strFans});
    }

    public String getPersonNumText() {
        if (!this.personNum.isEmpty()) {
            return this.personNum;
        }
        int num = new Random().nextInt(10) + 1;
        return String.format("%d", new Object[]{Integer.valueOf(num)});
    }
}