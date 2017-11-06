package com.panda.videolivecore.utils;

import com.panda.videolivecore.CoreApplication;
//import com.sina.weibo.sdk.utils.LogUtil;
//import de.greenrobot.event.EventBus;
import org.json.JSONObject;

public class CheckResultErrorUtils {
    private static final String TAG = "CheckResultErrorUtils";

    public static boolean CheckError(String strResult) {
        try {
            int errno = new JSONObject(strResult).getInt("errno");
            if (errno == 0) {
                return true;
            }
            if (errno != 200) {
                return false;
            }
            CoreApplication.getInstance().Logout();
            //EventBus.getDefault().post(new NotifyEvent(NotifyEvent.MSG_START_LOGIN_UI, ""));
            return false;
        } catch (Exception e) {
            //LogUtil.e(TAG, e.toString());
            return false;
        }
    }
}
