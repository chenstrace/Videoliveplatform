package com.panda.videolivecore.net.info;

import android.util.JsonReader;
import java.io.IOException;

public class EnterRoomInfo {
    public ChatInfo chatInfo = new ChatInfo();
    public HostInfo hostInfo = new HostInfo();
    public RoomInfo roomInfo = new RoomInfo();
    public UserInfo userInfo = new UserInfo();
    public VideoInfo videoInfo = new VideoInfo();

    public void read(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String strName = reader.nextName();
            if ("hostinfo".equalsIgnoreCase(strName)) {
                this.hostInfo.read(reader);
            } else if ("roominfo".equalsIgnoreCase(strName)) {
                this.roomInfo.read(reader);
            } else if ("videoinfo".equalsIgnoreCase(strName)) {
                this.videoInfo.read(reader);
            } else if ("chatinfo".equalsIgnoreCase(strName)) {
                this.chatInfo.read(reader);
            } else if ("userinfo".equalsIgnoreCase(strName)) {
                this.userInfo.read(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }

    public boolean isLogin() {
        return this.userInfo.rid > 0;
    }
}
