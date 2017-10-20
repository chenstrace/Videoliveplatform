package com.panda.videolivecore.data;

import java.util.ArrayList;

public class HotLiveItemInfo {
    public String bigimg;
    public String img;
    public String name;
    public String nickname;
    public String person_num;
    public String roomid = "";
    public String title;
    public String url;
    public String userid;

    public class ResponseData {
        public ArrayList<HotLiveItemInfo> data;
        public String errmsg;
        public int errno;
    }

    public HotLiveItemInfo(String t, String u, String f) {
        this.title = t;
        this.nickname = u;
        this.person_num = f;
    }
}
