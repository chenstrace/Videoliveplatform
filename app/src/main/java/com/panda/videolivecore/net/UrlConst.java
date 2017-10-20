package com.panda.videolivecore.net;

import com.panda.videolivecore.CoreApplication;
import com.panda.videolivecore.net.http.HttpRequest;
//import com.panda.videolivecore.network.LoginManager;
import java.util.UUID;

public class UrlConst {
    public static final String API_DOMAIN_URL = "http://api.m.panda.tv";
    public static final String BASE_DOMAIN_URL = "panda.tv";
    public static final String DOMAIN_URL = "http://panda.tv";
    public static final String LIVE_DOMAIN_URL = "http://g.live.panda.tv";
    public static final String LOGIN_DOMAIN_URL = "http://u.panda.tv";
    public static final String M_DOMAIN_URL = "http://m.panda.tv";
    public static final String PREFIX_HTTP = "http://";
    public static final String VERIFY_DOMAIN_URL = "http://verify.panda.tv";
    public static final String WWWDOMAIN_URL = "http://www.panda.tv";

    public static String getHomeSliderUrl() {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_rmd_ads_get", new Object[]{API_DOMAIN_URL}), true);
    }

    public static String getHomeHotLiveUrl() {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_rmd_hotlives_get", new Object[]{API_DOMAIN_URL}), true);
    }

    public static String getHomeMultiCateUrl() {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_get_live_list_by_multicate?pagenum=4", new Object[]{API_DOMAIN_URL}), false);
    }

    public static String getAllLiveListUrl(int pageno) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_live_lists?pageno=%d&pagenum=10&status=2&order=person_num", new Object[]{API_DOMAIN_URL, Integer.valueOf(pageno)}), false);
    }

    public static String getSubLiveUrl(String cate, int pageno) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_get_live_list_by_cate?cate=%s&pageno=%d&pagenum=10", new Object[]{API_DOMAIN_URL, cate, Integer.valueOf(pageno)}), false);
    }

    public static String getColumnLiveUrl() {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_get_all_subcate", new Object[]{API_DOMAIN_URL}), true);
    }

    public static String getRtmpDispatchUrl(String strChannel, String strSn, int sd, long ts, int r, String strSign) {
        return String.format("%s/liveplay?stype=rtmp&channel=%s&bid=360game&sn=%s&sid=%d&_rate=xd&ts=%d&r=%d&_ostype=Android&_sign=%s&_ver=%s", new Object[]{LIVE_DOMAIN_URL, strChannel, strSn, Integer.valueOf(sd), Long.valueOf(ts), Integer.valueOf(r), strSign, CoreApplication.getInstance().version()});
    }

    public static String getLiveStreamUrl(String strNum, String roomKey, String strStreamType, String sign, String time) {
        return String.format("http://pl%s.live.%s/live_panda/%s%s.flv?sign=%s&time=%s", new Object[]{strNum, BASE_DOMAIN_URL, roomKey, strStreamType, sign, time});
    }

    public static String getEnterRoomUrl(String strRroomId) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_get_liveroom_baseinfo?roomid=%s&type=json", new Object[]{API_DOMAIN_URL, strRroomId}), false);
    }

    public static String getChatInfoUrl(String strRroomId) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_chatinfo?roomid=%s&retry=false", new Object[]{API_DOMAIN_URL, strRroomId}), false);
    }

    public static String getBackupChatInfoUrl(String strRroomId) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_chatinfo?roomid=%s&retry=true", new Object[]{API_DOMAIN_URL, strRroomId}), false);
    }

    public static String getGroupMsgUrl(String strRroomId, String strData) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_send_group_msg?roomid=%s&type=1&content=%s", new Object[]{API_DOMAIN_URL, strRroomId, HttpRequest.URLEncoder(strData)}), false);
    }

    public static String getSetFollowRoomUrl(String strRroomId) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_follow?roomid=%s", new Object[]{API_DOMAIN_URL, strRroomId}), false);
    }

    public static String getCancelFollowRoomUrl(String strRroomId) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_unfollow?roomid=%s", new Object[]{API_DOMAIN_URL, strRroomId}), false);
    }

    public static String getFollowRoomInfoUrl(String strRroomId) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_has_followed?roomid=%s", new Object[]{API_DOMAIN_URL, strRroomId}), false);
    }

    public static String getSendBamboosUrl(String strBamboosNum, String hostId, String strRroomId) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_bamboos_send?bamboos_num=%s&to=%s&roomid=%s", new Object[]{API_DOMAIN_URL, strBamboosNum, hostId, strRroomId}), false);
    }

    public static String getHostBamboosUrl(String hostId) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_host_bamboos_get?host_id=%s", new Object[]{API_DOMAIN_URL, hostId}), false);
    }

    public static String getMyBamboosUrl() {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_my_bamboos_get", new Object[]{API_DOMAIN_URL}), true);
    }

    public static String getAcqureBambooTaskUrl() {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_time_task_list", new Object[]{API_DOMAIN_URL}), true);
    }

    public static String getBambooTaskDoneUrl(String taskId) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_time_task_done?task_id=%s", new Object[]{API_DOMAIN_URL, taskId}), false);
    }

    public static String getTaskRewardUrl(String taskId, String challenge, String validate, String seccode, String sign, String timestamp) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_task_rewards_get?my_task_id=%s&appkey=pandaren_time_task&challenge=%s&validate=%s&seccode=%s&sign=%s&timestamp=%s", new Object[]{API_DOMAIN_URL, taskId, challenge, validate, seccode, sign, timestamp}), false);
    }

    public static String getCheckNickNameUrl(String nicknameencode) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_check_nickname?nickname=%s", new Object[]{LOGIN_DOMAIN_URL, nicknameencode}), false);
    }

    public static String getCheckLoginStatusUrl(String authseq) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_get_token_and_login?authseq=%s", new Object[]{API_DOMAIN_URL, authseq}), false);
    }

    public static String getMyInfoUrl(String authseq) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_get_myinfo?authseq=%s", new Object[]{API_DOMAIN_URL, authseq}), false);
    }

    public static String getSendRegisterSmsUrl(String mobile) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_send_register_sms?mobile=%s", new Object[]{LOGIN_DOMAIN_URL, mobile}), false);
    }

    public static String getAesKeyUrl() {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_aeskey", new Object[]{LOGIN_DOMAIN_URL}), true);
    }

    public static String getLoginUrl(String useridencode, String aespasssword, String authseq) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_login?account=%s&password=%s&channel=1&authseq=%s", new Object[]{LOGIN_DOMAIN_URL, useridencode, aespasssword, authseq}), false);
    }

    public static String getLogoutUrl() {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/logout", new Object[]{LOGIN_DOMAIN_URL}), true);
    }

    public static String getMobileRegisterUrl(String mobilecode, String authcode, String ninknamecode, String aespasssword) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_mobile_register?&mobile=%s&vcode=%s&nickname=%s&password=%s&channel=1", new Object[]{LOGIN_DOMAIN_URL, mobilecode, authcode, ninknamecode, aespasssword}), false);
    }

    public static String getRegisterUrl(String useridencode, String aespasssword) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_register?username=%s&password=%s&channel=1", new Object[]{LOGIN_DOMAIN_URL, useridencode, aespasssword}), false);
    }

    public static String getTokenUrl() {
        return HttpRequest.URLAppendVersionPlatNoToken(String.format("%s/ajax_get_token", new Object[]{API_DOMAIN_URL}), true);
    }

    public static String getRoomUrl(String id) {
        return String.format("%s/%s", new Object[]{WWWDOMAIN_URL, id});
    }

    public static String getVerifyGetUrl() {
        return "http://verify.panda.tv/captcha/mobile/get?app=android_panda";
    }

    public static String getVerifyCodeUrl() {
        return "http://verify.panda.tv/captcha/mobile/verifyCode?app=android_panda";
    }

    public static String getResetPasswordUrl() {
        return "http://m.panda.tv/password_reset";
    }

    public static String getWebRegistUrl() {
        return "http://m.panda.tv/mregist";
    }

    public static String getSearchRoomIdUrl(String roomId) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_search?roomid=%s", new Object[]{API_DOMAIN_URL, roomId}), false);
    }

    public static String getSearchLiveCloseUrl(String keyword, int pageno) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_search?keyword=%s&pageno=%d&pagenum=10&status=3", new Object[]{API_DOMAIN_URL, keyword, Integer.valueOf(pageno)}), false);
    }

    public static String getSearchLiveOpenUrl(String keyword, int pageno) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_search?keyword=%s&pageno=%d&pagenum=10&status=2", new Object[]{API_DOMAIN_URL, keyword, Integer.valueOf(pageno)}), false);
    }

    public static String getFollowLiveCloseUrl(int pageno) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_get_follow_rooms?pageno=%d&pagenum=10&status=3", new Object[]{API_DOMAIN_URL, Integer.valueOf(pageno)}), false);
    }

    public static String getFollowLiveOpenUrl(int pageno) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_get_follow_rooms?pageno=%d&pagenum=10&status=2", new Object[]{API_DOMAIN_URL, Integer.valueOf(pageno)}), false);
    }

    public static String getLastestVersionUrl() {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_get_lastest_version", new Object[]{API_DOMAIN_URL}), true);
    }

    public static String getUploadImgUrl() {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_upload_img", new Object[]{API_DOMAIN_URL}), true);
    }

    public static String getSetUserinfoUrl(String strNickName, String strAvatar) {
        String strUrl = String.format("%s/ajax_set_userinfo?", new Object[]{API_DOMAIN_URL});
        String strParam = "";
        if (!strNickName.isEmpty()) {
            strParam = String.format("nickname=%s", new Object[]{HttpRequest.URLEncoder(strNickName)});
            strUrl = strUrl + strParam;
        }
        if (!strAvatar.isEmpty()) {
            if (!strParam.isEmpty()) {
                strUrl = strUrl + "&";
            }
            strParam = String.format("avatar=%s", new Object[]{HttpRequest.URLEncoder(strAvatar)});
            strUrl = strUrl + strParam;
        }
        return HttpRequest.URLAppendVersionPlat(strUrl, strParam.isEmpty());
    }

    public static String getNewerTaskListUrl() {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_user_task_list", new Object[]{API_DOMAIN_URL}), true);
    }

    public static String getNewerTaskRewardUrl(String taskId, String appkey) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_task_rewards_get?my_task_id=%s&appkey=%s", new Object[]{API_DOMAIN_URL, taskId, appkey}), false);
    }

    public static String getShareTaskDoneUrl(String rid, String taskId, String sign, String authseq) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_share_task_done?rid=%s&task_id=%s&sign=%s&authseq=%s", new Object[]{API_DOMAIN_URL, rid, taskId, sign, authseq}), false);
    }

    public static String getTaskConfig(String appkey) {
        return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_task_conf?appkey=%s&authseq=%s", new Object[]{API_DOMAIN_URL, appkey, getAuthSeq()}), false);
    }

    public static String getAuthSeq() {
//        return LoginManager.getMD5(UUID.randomUUID().toString());
        return "";
    }
}
