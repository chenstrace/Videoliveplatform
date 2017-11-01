package com.panda.videolivecore.net.info;

import android.util.JsonReader;
import java.io.IOException;

public class VerifyCodeInfo {
    public String challenge = "";
    public String seccode = "";
    public String sign = "";
    public String timestamp = "";
    public String validate = "";

    public void read(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String strName = reader.nextName();
            if ("challenge".equalsIgnoreCase(strName)) {
                this.challenge = reader.nextString();
            } else if ("validate".equalsIgnoreCase(strName)) {
                this.validate = reader.nextString();
            } else if ("seccode".equalsIgnoreCase(strName)) {
                this.seccode = reader.nextString();
            } else if ("sign".equalsIgnoreCase(strName)) {
                this.sign = reader.nextString();
            } else if ("timestamp".equalsIgnoreCase(strName)) {
                this.timestamp = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }
}
