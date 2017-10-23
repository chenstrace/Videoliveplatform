package com.panda.videolivecore.data;

import java.util.ArrayList;

public class SearchItemInfo {
    public String name;
    public String nickname = "";
    public String person_num;
    public Pictures pictures = new Pictures();
    public String roomid = "";

    public class Data {
        public ArrayList<SearchItemInfo> items;
        public String total;
    }

    public class Pictures {
        public String img;
    }

    public class ResponseData {
        public Data data;
        public String errmsg;
        public int errno;
    }

    public SearchItemInfo(String t, String u, String f) {
        this.name = t;
        this.person_num = f;
    }
}
