package com.panda.videolivecore.net.info;

import android.util.JsonReader;
import java.io.IOException;

public class TaskConfigInfo {
    public String content = "";
    public String link = "";

    public void read(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String strName = reader.nextName();
            if ("content".equalsIgnoreCase(strName)) {
                this.content = reader.nextString();
            } else if ("link".equalsIgnoreCase(strName)) {
                this.link = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }
}
