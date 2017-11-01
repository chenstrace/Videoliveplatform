package com.panda.videolivecore.net.info;

import android.util.JsonReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatInfo {
    public List<AddrInfo> addrs = new ArrayList();
    public String appid = "";
    public String authtype = "";
    public int rid = 0;
    public String sign = "";
    public String ts = "";

    public void read(JsonReader reader) throws IOException {
//        reader.beginObject();
//        while (reader.hasNext()) {
//            String strName = reader.nextName();
//            if (f.A.equalsIgnoreCase(strName)) {
//                this.rid = reader.nextInt();
//            } else if ("appid".equalsIgnoreCase(strName)) {
//                this.appid = reader.nextString();
//            } else if ("chat_addr_list".equalsIgnoreCase(strName)) {
//                readChatAddrList(reader);
//            } else if ("ts".equalsIgnoreCase(strName)) {
//                this.ts = reader.nextString();
//            } else if ("sign".equalsIgnoreCase(strName)) {
//                this.sign = reader.nextString();
//            } else if ("authtype".equalsIgnoreCase(strName)) {
//                this.authtype = reader.nextString();
//            } else {
//                reader.skipValue();
//            }
//        }
//        reader.endObject();
    }

    protected void readChatAddrList(JsonReader reader) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            this.addrs.add(new AddrInfo(reader.nextString()));
        }
        reader.endArray();
    }

    public String getFirstValidAddr() {
        String strReturn = "";
        for (int i = 0; i < this.addrs.size(); i++) {
            strReturn = ((AddrInfo) this.addrs.get(i)).strAddr;
            if (!strReturn.isEmpty()) {
                break;
            }
        }
        return strReturn;
    }

    public String[] getAllAddrString() {
        if (this.addrs.size() == 0) {
            return null;
        }
        String[] ret = new String[this.addrs.size()];
        for (int i = 0; i < this.addrs.size(); i++) {
            ret[i] = new String(((AddrInfo) this.addrs.get(i)).strAddr);
        }
        return ret;
    }
}
