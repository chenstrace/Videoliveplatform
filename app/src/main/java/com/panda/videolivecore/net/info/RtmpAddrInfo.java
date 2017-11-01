package com.panda.videolivecore.net.info;

public class RtmpAddrInfo {
    String strAddr = "";

    public RtmpAddrInfo(String str) {
        this.strAddr = str;
    }

    public String getAddrString() {
        return this.strAddr;
    }

    public String getAddrString(String strRoomKey) {
        if (this.strAddr.isEmpty()) {
            return "";
        }
        return this.strAddr + strRoomKey;
    }
}
