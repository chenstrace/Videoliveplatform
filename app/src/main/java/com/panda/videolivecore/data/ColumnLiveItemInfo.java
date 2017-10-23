package com.panda.videolivecore.data;

import java.util.ArrayList;

public class ColumnLiveItemInfo {

    public class Data {
        public String cname;
        public String ename;
        public String ext;
        public String img;
    }

    public class ResponseData {
        public ArrayList<Data> data;
        public String errmsg;
        public int errno;
    }
}
