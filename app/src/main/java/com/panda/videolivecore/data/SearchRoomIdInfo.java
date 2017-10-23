package com.panda.videolivecore.data;

import java.util.ArrayList;

public class SearchRoomIdInfo {
    public String bamboos;
    public String classification;
    public String content;
    public String fans;
    public String hostid;
    public String md5;
    public String name;
    public String nickname;
    public String person_num;
    public Pictures pictures;
    public String roomid;
    public Se se;
    public String status;
    public String style;
    public String updatetime;
    public String url_footprint;

    public class Data {
        public ArrayList<SearchRoomIdInfo> items;
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

    public class Se {
        public int docId = 0;
        public String prefix = "";
        public int sort0 = 0;
    }
}
