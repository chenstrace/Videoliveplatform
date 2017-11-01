package com.panda.videolivecore.net;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Base64;
import com.panda.videolivecore.net.http.HttpRequest;
import com.panda.videolivecore.net.http.IHttpRequestEvent;
import com.panda.videolivecore.net.info.RtmpDispatchInfo;
import java.util.Random;
import org.json.JSONObject;

public class RtmpDispatchRequest {
    private HttpRequest m_request = null;

    public RtmpDispatchRequest(IHttpRequestEvent event) {
        this.m_request = new HttpRequest(event);
    }

    public HttpRequest get() {
        return this.m_request;
    }

    public void sendGetStreamAddr(Context context, String strRoomKey, String strType, String strContext) {
        this.m_request.send(UrlConst.getRtmpDispatchUrl(strType, strRoomKey, new Random().nextInt(), System.currentTimeMillis(), new Random().nextInt(), ((TelephonyManager) context.getSystemService("phone")).getDeviceId()), true, strContext);
    }

    public static boolean onGetStreamAddr(String strContent, RtmpDispatchInfo info) {
        if (strContent.length() <= 6) {
            return false;
        }
        String str1 = strContent.substring(0, 3);
        try {
            info.read(new JSONObject(new String(Base64.decode(str1 + strContent.substring(6, strContent.length()), 0), HttpRequest.CHARSET_NAME_UTF8)));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
