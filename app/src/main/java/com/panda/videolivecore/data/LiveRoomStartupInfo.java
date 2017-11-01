package com.panda.videolivecore.data;

public class LiveRoomStartupInfo {
    public String addrStream = "";
    public String idRoom = "0";
    public String urlImage = "";
    public String urlRoom = "";

    public LiveRoomStartupInfo(String id, String r, String i, String s) {
        this.idRoom = id;
        this.urlRoom = r;
        this.urlImage = i;
        this.addrStream = s;
    }
}
