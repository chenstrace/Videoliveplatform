package com.panda.videolivecore.net.info;

import android.util.JsonReader;

import java.io.IOException;

public class UserInfo {
    public String avatar = "";
    public String bamboos = "0";
    public String isFollowed = "";
    public String loginEmail = "";
    public String loginTime = "";
    public String mobile = "";
    public String modifyTime = "";
    public String nickName = "";
    public int rid = 0;
    public String userName = "";

    public void Clear() {
        this.rid = 0;
        this.userName = "";
        this.nickName = "";
        this.loginEmail = "";
        this.mobile = "";
        this.avatar = "";
        this.loginTime = "";
        this.modifyTime = "";
        this.isFollowed = "";
        this.bamboos = "0";
    }

    public void read(JsonReader paramJsonReader)
            throws IOException {
        paramJsonReader.beginObject();
        while (paramJsonReader.hasNext()) {
            String str = paramJsonReader.nextName();
            if ("rid".equalsIgnoreCase(str))
                this.rid = paramJsonReader.nextInt();
            else if ("userName".equalsIgnoreCase(str))
                this.userName = paramJsonReader.nextString();
            else if ("nickName".equalsIgnoreCase(str))
                this.nickName = paramJsonReader.nextString();
            else if ("loginEmail".equalsIgnoreCase(str))
                this.loginEmail = paramJsonReader.nextString();
            else if ("mobile".equalsIgnoreCase(str))
                this.mobile = paramJsonReader.nextString();
            else if ("avatar".equalsIgnoreCase(str))
                this.avatar = paramJsonReader.nextString();
            else if ("loginTime".equalsIgnoreCase(str))
                this.loginTime = paramJsonReader.nextString();
            else if ("modifyTime".equalsIgnoreCase(str))
                this.modifyTime = paramJsonReader.nextString();
            else if ("is_followed".equalsIgnoreCase(str))
                this.isFollowed = paramJsonReader.nextString();
            else if ("bamboos".equalsIgnoreCase(str))
                this.bamboos = paramJsonReader.nextString();
            else
                paramJsonReader.skipValue();
        }
        paramJsonReader.endObject();
    }
}