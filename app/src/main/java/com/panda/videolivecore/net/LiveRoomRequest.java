package com.panda.videolivecore.net;

import android.util.JsonReader;
import com.panda.videolivecore.net.http.HttpRequest;
import com.panda.videolivecore.net.http.IHttpRequestEvent;
import com.panda.videolivecore.net.info.BambooList;
import com.panda.videolivecore.net.info.ChatInfo;
import com.panda.videolivecore.net.info.EnterRoomInfo;
import com.panda.videolivecore.net.info.ResultMsgInfo;
import com.panda.videolivecore.net.info.VerifyCodeInfo;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

public class LiveRoomRequest {
    private HttpRequest m_request = null;

    public LiveRoomRequest(IHttpRequestEvent event) {
        this.m_request = new HttpRequest(event);
    }

    public HttpRequest get() {
        return this.m_request;
    }

    public void sendEnterRoom(String strRroomId, String strContext) {
        this.m_request.send(UrlConst.getEnterRoomUrl(strRroomId), true, strContext);
    }

    public void sendGetChatInfo(String strRroomId, String strContext) {
        this.m_request.send(UrlConst.getChatInfoUrl(strRroomId), true, strContext);
    }

    public void sendGetBackupChatInfo(String strRroomId, String strContext) {
        this.m_request.send(UrlConst.getBackupChatInfoUrl(strRroomId), true, strContext);
    }

    public void sendGroupMsg(String strRroomId, String strData, String strContext) {
        this.m_request.send(UrlConst.getGroupMsgUrl(strRroomId, strData), true, strContext);
    }

    public void sendSetFollowRoom(String strRroomId, String strContext) {
        this.m_request.send(UrlConst.getSetFollowRoomUrl(strRroomId), true, strContext);
    }

    public void sendCancelFollowRoom(String strRroomId, String strContext) {
        this.m_request.send(UrlConst.getCancelFollowRoomUrl(strRroomId), true, strContext);
    }

    public void sendGetFollowInfo(String strRroomId, String strContext) {
        this.m_request.send(UrlConst.getFollowRoomInfoUrl(strRroomId), true, strContext);
    }

    public void sendBamboos(String strBamboosNum, String hostId, String strRroomId, String strContext) {
        this.m_request.post(UrlConst.getSendBamboosUrl(strBamboosNum, hostId, strRroomId), "", strContext);
    }

    public void sendGetHostBamboos(String hostId, String strContext) {
        this.m_request.post(UrlConst.getHostBamboosUrl(hostId), "", strContext);
    }

    public void sendMyBamboos(String strContext) {
        this.m_request.post(UrlConst.getMyBamboosUrl(), "", strContext);
    }

    public static boolean readResultMsgInfo(String strContent, ResultMsgInfo info) {
        try {
            info.read(strContent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean readEnterRoomInfo(String strContent, ResultMsgInfo info, EnterRoomInfo infoExtend) {
//        if (info.read(strContent) == null || info.error != 0) {
//            return false;
//        }
//        try {
//            JsonReader reader = new JsonReader(new InputStreamReader(new ByteArrayInputStream(new String(info.data).getBytes(HttpRequest.CHARSET_NAME_UTF8)), HttpRequest.CHARSET_NAME_UTF8));
//            reader.beginObject();
//            while (reader.hasNext()) {
//                if (aY.d.equalsIgnoreCase(reader.nextName())) {
//                    infoExtend.read(reader);
//                } else {
//                    reader.skipValue();
//                }
//            }
//            reader.endObject();
//            reader.close();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
    }

    public static boolean readGroupMsg(String strContent, ResultMsgInfo info) {
        return info.readDataBoolean(strContent);
    }

    public static String readBamboos(String strContent, ResultMsgInfo info) {
        return info.readDataString(strContent);
    }

    public static boolean readChatInfo(String strContent, ResultMsgInfo info, ChatInfo chat_info) {
        if (info.read(strContent) == null || info.error != 0) {
            return false;
        }
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(new ByteArrayInputStream(new String(info.data).getBytes(HttpRequest.CHARSET_NAME_UTF8)), HttpRequest.CHARSET_NAME_UTF8));
            chat_info.read(reader);
            reader.close();
            if (chat_info.getAllAddrString() != null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendAcqureBambooTask(String strContext) {
        this.m_request.send(UrlConst.getAcqureBambooTaskUrl(), true, strContext);
    }

    public static boolean readAcqureBambooTask(String strContent, ResultMsgInfo info, BambooList bamboo_list) {
        if (info.read(strContent) == null || info.error != 0) {
            return false;
        }
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(new ByteArrayInputStream(new String(info.data).getBytes(HttpRequest.CHARSET_NAME_UTF8)), HttpRequest.CHARSET_NAME_UTF8));
            bamboo_list.read(reader);
            reader.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendBambooTaskDone(String strContext, String taskId) {
        this.m_request.send(UrlConst.getBambooTaskDoneUrl(taskId), true, strContext);
    }

    public void sendGetTaskReward(String strContext, String taskId, String challenge, String validate, String seccode, String sign, String timestamp) {
        this.m_request.post(UrlConst.getTaskRewardUrl(taskId, challenge, validate, seccode, sign, timestamp), "", strContext);
    }

    public static String readGetTaskReward(String strContent, ResultMsgInfo info) {
        String strData = "";
        if (info.read(strContent) == null || info.error != 0) {
            return strData;
        }
        try {
            return new String(info.data);
        } catch (Exception e) {
            e.printStackTrace();
            return strData;
        }
    }

    public static boolean readVerifyCode(String strContent, ResultMsgInfo info, VerifyCodeInfo verify_code) {
        if (info.read(strContent) == null || info.error != 0) {
            return false;
        }
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(new ByteArrayInputStream(new String(info.data).getBytes(HttpRequest.CHARSET_NAME_UTF8)), HttpRequest.CHARSET_NAME_UTF8));
            verify_code.read(reader);
            reader.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
