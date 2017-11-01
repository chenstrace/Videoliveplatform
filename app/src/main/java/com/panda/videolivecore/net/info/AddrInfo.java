package com.panda.videolivecore.net.info;

public class AddrInfo {
    public String ip = "";
    public int port = 0;
    public String strAddr = "";

    public AddrInfo(String strAddr) {
        this.strAddr = strAddr;
        parse(strAddr);
    }

    public void parse(String strAddr) {
        String[] strTemp = strAddr.split(":");
        if (strTemp.length == 2) {
            this.ip = strTemp[0];
            this.port = Integer.parseInt(strTemp[1]);
        }
    }
}
