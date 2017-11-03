package com.panda.videolivecore.net.info;

import android.util.JsonReader;
import java.io.IOException;

public class HostInfo {
    public String avatar = "";
    public String bamboos = "";
    public String name = "";
    public int rid = 0;

    public void read(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String strName = reader.nextName();
            if ("rid".equalsIgnoreCase(strName)) {
                this.rid = reader.nextInt();
            } else if ("name".equalsIgnoreCase(strName)) {
                this.name = reader.nextString();
            } else if ("avatar".equalsIgnoreCase(strName)) {
                this.avatar = reader.nextString();
            } else if ("bamboos".equalsIgnoreCase(strName)) {
                this.bamboos = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }
}