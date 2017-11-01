package com.panda.videolivecore.net.info;

import com.panda.videolivecore.CoreApplication;
//import com.panda.videolivecore.utils.NotifyEvent;
//import de.greenrobot.event.EventBus;
import org.json.JSONObject;
//import tv.danmaku.ijk.media.player.IMediaPlayer;

public class ResultMsgInfo {
    public String data = "";
    public String errmsg = "";
    public int error = 0;

    public JSONObject read(String strContent) {
        Exception e;
        JSONObject obj = null;
        try {
            JSONObject obj2 = new JSONObject(strContent);
            try {
                this.error = obj2.getInt("errno");
                this.errmsg = obj2.getString("errmsg");
                this.data = obj2.getString("data");
                checkError(this.error);
                return obj2;
            } catch (Exception e2) {
                e = e2;
                obj = obj2;
                e.printStackTrace();
                return obj;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return obj;
        }
    }

    public boolean readDataBoolean(String strContent) {
        try {
            return read(strContent).getBoolean("data");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int readDataInt(String strContent) {
        try {
            return read(strContent).getInt("data");
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String readDataString(String strContent) {
        try {
            return read(strContent).getString("data");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void checkError(int nError) {
//        if (nError == IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE) {
//            CoreApplication.getInstance().SendPtTokenRequest();
//        } else if (nError == 200) {
//            CoreApplication.getInstance().Logout();
//            EventBus.getDefault().post(new NotifyEvent(NotifyEvent.MSG_START_LOGIN_UI, ""));
//        }
    }
}