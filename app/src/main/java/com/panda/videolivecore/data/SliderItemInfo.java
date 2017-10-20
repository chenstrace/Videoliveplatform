package com.panda.videolivecore.data;

import java.util.ArrayList;

public class SliderItemInfo {
    public String bigimg;
    public String img;
    public String name;
    public String nickname;
    public String roomid = "";
    public String title;
    public String url;

    public class ResponseData {
        public ArrayList<SliderItemInfo> data;
        public String errmsg;
        public int errno;
    }

    public SliderItemInfo(String t) {
        this.title = t;
    }
}
