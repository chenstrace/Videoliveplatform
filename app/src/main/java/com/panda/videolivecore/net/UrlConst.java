package com.panda.videolivecore.net;

import com.panda.videolivecore.CoreApplication;
import com.panda.videolivecore.net.http.HttpRequest;
import com.panda.videolivecore.network.LoginManager;
import java.util.UUID;

public class UrlConst
{
  public static final String API_DOMAIN_URL = "http://api.m.panda.tv";
  public static final String BASE_DOMAIN_URL = "panda.tv";
  public static final String DOMAIN_URL = "http://panda.tv";
  public static final String LIVE_DOMAIN_URL = "http://g.live.panda.tv";
  public static final String LOGIN_DOMAIN_URL = "http://u.panda.tv";
  public static final String M_DOMAIN_URL = "http://m.panda.tv";
  public static final String PREFIX_HTTP = "http://";
  public static final String VERIFY_DOMAIN_URL = "http://verify.panda.tv";
  public static final String WWWDOMAIN_URL = "http://www.panda.tv";

  public static String getAcqureBambooTaskUrl()
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_time_task_list", new Object[] { "http://api.m.panda.tv" }), true);
  }

  public static String getAesKeyUrl()
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_aeskey", new Object[] { "http://u.panda.tv" }), true);
  }

  public static String getAllLiveListUrl(int paramInt)
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = "http://api.m.panda.tv";
    arrayOfObject[1] = Integer.valueOf(paramInt);
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_live_lists?pageno=%d&pagenum=10&status=2&order=person_num", arrayOfObject), false);
  }

  public static String getAuthSeq()
  {
    //return LoginManager.getMD5(UUID.randomUUID().toString());
    return "";
  }

  public static String getBackupChatInfoUrl(String paramString)
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_chatinfo?roomid=%s&retry=true", new Object[] { "http://api.m.panda.tv", paramString }), false);
  }

  public static String getBambooTaskDoneUrl(String paramString)
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_time_task_done?task_id=%s", new Object[] { "http://api.m.panda.tv", paramString }), false);
  }

  public static String getCancelFollowRoomUrl(String paramString)
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_unfollow?roomid=%s", new Object[] { "http://api.m.panda.tv", paramString }), false);
  }

  public static String getChatInfoUrl(String paramString)
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_chatinfo?roomid=%s&retry=false", new Object[] { "http://api.m.panda.tv", paramString }), false);
  }

  public static String getCheckLoginStatusUrl(String paramString)
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_get_token_and_login?authseq=%s", new Object[] { "http://api.m.panda.tv", paramString }), false);
  }

  public static String getCheckNickNameUrl(String paramString)
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_check_nickname?nickname=%s", new Object[] { "http://u.panda.tv", paramString }), false);
  }

  public static String getColumnLiveUrl()
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_get_all_subcate", new Object[] { "http://api.m.panda.tv" }), true);
  }

  public static String getEnterRoomUrl(String paramString)
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_get_liveroom_baseinfo?roomid=%s&type=json", new Object[] { "http://api.m.panda.tv", paramString }), false);
  }

  public static String getFollowLiveCloseUrl(int paramInt)
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = "http://api.m.panda.tv";
    arrayOfObject[1] = Integer.valueOf(paramInt);
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_get_follow_rooms?pageno=%d&pagenum=10&status=3", arrayOfObject), false);
  }

  public static String getFollowLiveOpenUrl(int paramInt)
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = "http://api.m.panda.tv";
    arrayOfObject[1] = Integer.valueOf(paramInt);
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_get_follow_rooms?pageno=%d&pagenum=10&status=2", arrayOfObject), false);
  }

  public static String getFollowRoomInfoUrl(String paramString)
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_has_followed?roomid=%s", new Object[] { "http://api.m.panda.tv", paramString }), false);
  }

  public static String getGroupMsgUrl(String paramString1, String paramString2)
  {
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = "http://api.m.panda.tv";
    arrayOfObject[1] = paramString1;
    arrayOfObject[2] = HttpRequest.URLEncoder(paramString2);
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_send_group_msg?roomid=%s&type=1&content=%s", arrayOfObject), false);
  }

  public static String getHomeHotLiveUrl()
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_rmd_hotlives_get", new Object[] { "http://api.m.panda.tv" }), true);
  }

  public static String getHomeMultiCateUrl()
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_get_live_list_by_multicate?pagenum=4", new Object[] { "http://api.m.panda.tv" }), false);
  }

  public static String getHomeSliderUrl()
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_rmd_ads_get", new Object[] { "http://api.m.panda.tv" }), true);
  }

  public static String getHostBamboosUrl(String paramString)
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_host_bamboos_get?host_id=%s", new Object[] { "http://api.m.panda.tv", paramString }), false);
  }

  public static String getLastestVersionUrl()
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_get_lastest_version", new Object[] { "http://api.m.panda.tv" }), true);
  }

  public static String getLiveStreamUrl(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    return String.format("http://pl%s.live.%s/live_panda/%s%s.flv?sign=%s&time=%s", new Object[] { paramString1, "panda.tv", paramString2, paramString3, paramString4, paramString5 });
  }

  public static String getLoginUrl(String paramString1, String paramString2, String paramString3)
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_login?account=%s&password=%s&channel=1&authseq=%s", new Object[] { "http://u.panda.tv", paramString1, paramString2, paramString3 }), false);
  }

  public static String getLogoutUrl()
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/logout", new Object[] { "http://u.panda.tv" }), true);
  }

  public static String getMobileRegisterUrl(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_mobile_register?&mobile=%s&vcode=%s&nickname=%s&password=%s&channel=1", new Object[] { "http://u.panda.tv", paramString1, paramString2, paramString3, paramString4 }), false);
  }

  public static String getMyBamboosUrl()
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_my_bamboos_get", new Object[] { "http://api.m.panda.tv" }), true);
  }

  public static String getMyInfoUrl(String paramString)
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_get_myinfo?authseq=%s", new Object[] { "http://api.m.panda.tv", paramString }), false);
  }

  public static String getNewerTaskListUrl()
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_user_task_list", new Object[] { "http://api.m.panda.tv" }), true);
  }

  public static String getNewerTaskRewardUrl(String paramString1, String paramString2)
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_task_rewards_get?my_task_id=%s&appkey=%s", new Object[] { "http://api.m.panda.tv", paramString1, paramString2 }), false);
  }

  public static String getRegisterUrl(String paramString1, String paramString2)
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_register?username=%s&password=%s&channel=1", new Object[] { "http://u.panda.tv", paramString1, paramString2 }), false);
  }

  public static String getResetPasswordUrl()
  {
    return "http://m.panda.tv/password_reset";
  }

  public static String getRoomUrl(String paramString)
  {
    return String.format("%s/%s", new Object[] { "http://www.panda.tv", paramString });
  }

  public static String getRtmpDispatchUrl(String paramString1, String paramString2, int paramInt1, long paramLong, int paramInt2, String paramString3)
  {
//    Object[] arrayOfObject = new Object[8];
//    arrayOfObject[0] = "http://g.live.panda.tv";
//    arrayOfObject[1] = paramString1;
//    arrayOfObject[2] = paramString2;
//    arrayOfObject[3] = Integer.valueOf(paramInt1);
//    arrayOfObject[4] = Long.valueOf(paramLong);
//    arrayOfObject[5] = Integer.valueOf(paramInt2);
//    arrayOfObject[6] = paramString3;
//    arrayOfObject[7] = CoreApplication.getInstance().version();
//    return String.format("%s/liveplay?stype=rtmp&channel=%s&bid=360game&sn=%s&sid=%d&_rate=xd&ts=%d&r=%d&_ostype=Android&_sign=%s&_ver=%s", arrayOfObject);
    return "";
  }

  public static String getSearchLiveCloseUrl(String paramString, int paramInt)
  {
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = "http://api.m.panda.tv";
    arrayOfObject[1] = paramString;
    arrayOfObject[2] = Integer.valueOf(paramInt);
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_search?keyword=%s&pageno=%d&pagenum=10&status=3", arrayOfObject), false);
  }

  public static String getSearchLiveOpenUrl(String paramString, int paramInt)
  {
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = "http://api.m.panda.tv";
    arrayOfObject[1] = paramString;
    arrayOfObject[2] = Integer.valueOf(paramInt);
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_search?keyword=%s&pageno=%d&pagenum=10&status=2", arrayOfObject), false);
  }

  public static String getSearchRoomIdUrl(String paramString)
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_search?roomid=%s", new Object[] { "http://api.m.panda.tv", paramString }), false);
  }

  public static String getSendBamboosUrl(String paramString1, String paramString2, String paramString3)
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_bamboos_send?bamboos_num=%s&to=%s&roomid=%s", new Object[] { "http://api.m.panda.tv", paramString1, paramString2, paramString3 }), false);
  }

  public static String getSendRegisterSmsUrl(String paramString)
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_send_register_sms?mobile=%s", new Object[] { "http://u.panda.tv", paramString }), false);
  }

  public static String getSetFollowRoomUrl(String paramString)
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_follow?roomid=%s", new Object[] { "http://api.m.panda.tv", paramString }), false);
  }

  public static String getSetUserinfoUrl(String paramString1, String paramString2)
  {
    String str1 = String.format("%s/ajax_set_userinfo?", new Object[] { "http://api.m.panda.tv" });
    String str2 = "";
    if (!paramString1.isEmpty())
    {
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = HttpRequest.URLEncoder(paramString1);
      str2 = String.format("nickname=%s", arrayOfObject2);
      str1 = str1 + str2;
    }
    if (!paramString2.isEmpty())
    {
      if (!str2.isEmpty())
        str1 = str1 + "&";
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = HttpRequest.URLEncoder(paramString2);
      str2 = String.format("avatar=%s", arrayOfObject1);
      str1 = str1 + str2;
    }
    return HttpRequest.URLAppendVersionPlat(str1, str2.isEmpty());
  }

  public static String getShareTaskDoneUrl(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_share_task_done?rid=%s&task_id=%s&sign=%s&authseq=%s", new Object[] { "http://api.m.panda.tv", paramString1, paramString2, paramString3, paramString4 }), false);
  }

  public static String getSubLiveUrl(String paramString, int paramInt)
  {
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = "http://api.m.panda.tv";
    arrayOfObject[1] = paramString;
    arrayOfObject[2] = Integer.valueOf(paramInt);
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_get_live_list_by_cate?cate=%s&pageno=%d&pagenum=10", arrayOfObject), false);
  }

  public static String getTaskConfig(String paramString)
  {
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = "http://api.m.panda.tv";
    arrayOfObject[1] = paramString;
    arrayOfObject[2] = getAuthSeq();
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_task_conf?appkey=%s&authseq=%s", arrayOfObject), false);
  }

  public static String getTaskRewardUrl(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6)
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_task_rewards_get?my_task_id=%s&appkey=pandaren_time_task&challenge=%s&validate=%s&seccode=%s&sign=%s&timestamp=%s", new Object[] { "http://api.m.panda.tv", paramString1, paramString2, paramString3, paramString4, paramString5, paramString6 }), false);
  }

  public static String getTokenUrl()
  {
    return HttpRequest.URLAppendVersionPlatNoToken(String.format("%s/ajax_get_token", new Object[] { "http://api.m.panda.tv" }), true);
  }

  public static String getUploadImgUrl()
  {
    return HttpRequest.URLAppendVersionPlat(String.format("%s/ajax_upload_img", new Object[] { "http://api.m.panda.tv" }), true);
  }

  public static String getVerifyCodeUrl()
  {
    return "http://verify.panda.tv/captcha/mobile/verifyCode?app=android_panda";
  }

  public static String getVerifyGetUrl()
  {
    return "http://verify.panda.tv/captcha/mobile/get?app=android_panda";
  }

  public static String getWebRegistUrl()
  {
    return "http://m.panda.tv/mregist";
  }
}
