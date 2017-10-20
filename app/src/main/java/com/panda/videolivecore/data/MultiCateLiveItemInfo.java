package com.panda.videolivecore.data;

import java.util.ArrayList;

public class MultiCateLiveItemInfo {
    public String announcement;
    public Classification classification;
    public String classify_switch;
    public String createtime;
    public String duration;
    public String end_time;
    public String fans;
    public String hostid;
    public String id;
    public String level;
    public String name;
    public String person_num;
    public Pictures pictures;
    public String reliable;
    public String remind_content;
    public String remind_switch;
    public String room_key;
    public String schedule;
    public String start_time;
    public String status;
    public String stream_status;
    public String updatetime;
    public UserInfo userinfo;
    public String ver;

    public class Classification {
        public String cname;
        public String ename;
    }

    public class Data {
        public ArrayList<MultiCateLiveItemInfo> items;
        public String total;
        public SubType type;
    }

    public class Pictures {
        public String img;
    }

    public class ResponseData {
        public String authseq;
        public ArrayList<Data> data;
        public String errmsg;
        public int errno;
    }

    public class SubType {
        public String cname;
        public String ename;
    }

    public class UserInfo {
        public String avatar = "";
        public String nickName = "";
        public String rid = "";
        public String userName = "";
    }
}
