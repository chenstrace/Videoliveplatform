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


    public void read(JsonReader paramJsonReader)
            throws IOException {
        paramJsonReader.beginObject();
        while (paramJsonReader.hasNext()) {
            String str = paramJsonReader.nextName();
            if ("id".equalsIgnoreCase(str))
                this.id = paramJsonReader.nextString();
            else if ("name".equalsIgnoreCase(str))
                this.name = paramJsonReader.nextString();
            else if ("type".equalsIgnoreCase(str))
                this.type = paramJsonReader.nextString();
            else if ("bulletin".equalsIgnoreCase(str))
                this.bulletin = paramJsonReader.nextString();
            else if ("fans".equalsIgnoreCase(str))
                this.fans = paramJsonReader.nextString();
            else if ("person_num".equalsIgnoreCase(str))
                this.personNum = paramJsonReader.nextString();
            else
                paramJsonReader.skipValue();
        }
        paramJsonReader.endObject();
    }


}