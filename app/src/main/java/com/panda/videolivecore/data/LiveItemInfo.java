package com.panda.videolivecore.data;

import java.util.ArrayList;

public class LiveItemInfo {
    public String id;
    public String name;
    public String person_num;
    public Pictures pictures;
    public UserInfo userinfo = new UserInfo();

    public class Data {
        public ArrayList<LiveItemInfo> items;
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

    public class UserInfo {
        public String avatar = "";
        public String nickName = "";
        public String rid = "";
    }

    public LiveItemInfo(String t, String u, String f) {
        this.name = t;
        this.userinfo.nickName = u;
        this.person_num = f;
        this.id = "";
        this.pictures = new Pictures();
    }
}
