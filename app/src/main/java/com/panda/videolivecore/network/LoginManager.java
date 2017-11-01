package com.panda.videolivecore.network;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;

import com.panda.videolivecore.net.UrlConst;
import com.panda.videolivecore.net.http.HttpRequest;
import com.panda.videolivecore.net.info.PtTokenInfo;
import com.panda.videolivecore.net.info.UserInfo;
import com.panda.videolivecore.utils.AESUtils;
import com.panda.videolivecore.utils.CookieContiner;
import com.panda.videolivecore.utils.LogUtils;
import com.panda.videoliveplatform.R;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class LoginManager {
    public static final String BROADCAST_LOGIN = "com.panda.videoliveplatform.action.LOGIN";
    public static final String BROADCAST_LOGOUT = "com.panda.videoliveplatform.action.LOGOUT";
    private static final String HTTP_PRIVATE_HEADER_KEY = "xiaozhangdepandatv";
    private static final String HTTP_PRIVATE_HEADER_VALUE = "1";
    public static final int MSG_OUT_AUTH_COMPLETE = 260;
    public static final int MSG_OUT_CHECK_NICKNAME_COMPLETE = 263;
    public static final int MSG_OUT_GET_AUTH_COMPLETE = 262;
    public static final int MSG_OUT_LOGIN_COMPLETE = 257;
    public static final int MSG_OUT_LOGOUT_COMPLETE = 258;
    public static final int MSG_OUT_MOBILE_REGISTER_COMPLETE = 261;
    public static final int MSG_OUT_REGISTER_COMPLETE = 259;
    private static final String TAG = "LoginManager";
    private WeakReference<Context> mContext = null;
    private PersistentCookieStore mCookieStore = null;
    private boolean mIsCheckNickNameWorking = false;
    private boolean mIsWorking = false;
    private HttpContext mLocalContext = null;
    private UserInfo mUserInfo = new UserInfo();
    private Handler m_MsgHandler = new Handler() {
        public void handleMessage(Message msg) {
            Context context;
            Intent intent;
            switch (msg.what) {
                case 257:
                case LoginManager.MSG_OUT_AUTH_COMPLETE /*260*/:
                    try {
                        Bundle bundle = msg.getData();
                        if (bundle != null) {
                            int err = bundle.getInt("err", 0);
                            if (err != 0) {
                                if (err != 0) {
                                    LoginManager.this.ClearUserInfo();
                                    if (err != -2) {
                                        LoginManager.this.mCookieStore.clear();
                                    }
                                }
                                if (LoginManager.this.mContext != null) {
                                    context = (Context) LoginManager.this.mContext.get();
                                    if (context != null) {
                                        intent = new Intent(LoginManager.BROADCAST_LOGOUT);
                                        intent.setPackage(context.getPackageName());
                                        context.sendBroadcast(intent);
                                        return;
                                    }
                                    return;
                                }
                                return;
                            } else if (LoginManager.this.mContext != null) {
                                context = (Context) LoginManager.this.mContext.get();
                                if (context != null) {
                                    intent = new Intent(LoginManager.BROADCAST_LOGIN);
                                    intent.setPackage(context.getPackageName());
                                    context.sendBroadcast(intent);
                                    return;
                                }
                                return;
                            } else {
                                return;
                            }
                        }
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                case LoginManager.MSG_OUT_LOGOUT_COMPLETE /*258*/:
                    try {
                        if (LoginManager.this.mContext != null) {
                            context = (Context) LoginManager.this.mContext.get();
                            if (context != null) {
                                intent = new Intent(LoginManager.BROADCAST_LOGOUT);
                                intent.setPackage(context.getPackageName());
                                context.sendBroadcast(intent);
                                return;
                            }
                            return;
                        }
                        return;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return;
                    }
                default:
                    super.handleMessage(msg);
                    return;
            }
        }
    };
    private WeakReference<Handler> m_OutCheckNickNameHandler = null;
    private WeakReference<Handler> m_Outhandler = null;

    public LoginManager(Context context) {
        this.mContext = new WeakReference(context);
        this.mCookieStore = new PersistentCookieStore(context);
        this.mLocalContext = new BasicHttpContext();
        this.mLocalContext.setAttribute("http.cookie-store", this.mCookieStore);
        Auth(this.m_MsgHandler);
        timeLoop();
    }

    public UserInfo GetUserInfo() {
        return this.mUserInfo;
    }

    public static final String getMD5(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (byte b : messageDigest) {
                String h = Integer.toHexString(b & 255);
                while (h.length() < 2) {
                    h = "0" + h;
                }
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String GetUserDisplayName() {
        if (this.mUserInfo.nickName != null && !this.mUserInfo.nickName.isEmpty()) {
            return this.mUserInfo.nickName;
        }
        return String.format("User%d", new Object[]{Integer.valueOf(this.mUserInfo.rid)});
    }

    public void CheckNickName(String nickname, Handler handler) {
        if (!this.mIsCheckNickNameWorking) {
            this.m_OutCheckNickNameHandler = new WeakReference(handler);
            doCheckNickName(nickname);
        }
    }

    public void Login(String userid, String userpsd, Handler handler) {
        if (!this.mIsWorking) {
            this.m_Outhandler = new WeakReference(handler);
            doLogin(userid, userpsd);
        }
    }

    public void Logout(Handler handler) {
        this.m_Outhandler = new WeakReference(handler);
        if (!this.mIsWorking) {
            doLogout();
        }
    }

    public void Auth(Handler handler) {
        this.m_Outhandler = new WeakReference(handler);
        if (!this.mIsWorking) {
            doAuth();
        }
    }

    public void MenuLogout() {
        SendLogoutComplete(0, "");
    }

    public void MobileRegister(String mobile, String auth, String nickname, String userpsd, Handler handler) {
        if (!this.mIsWorking) {
            this.m_Outhandler = new WeakReference(handler);
            doMobileRegister(mobile, auth, nickname, userpsd);
        }
    }

    public void MobileGetAuth(String mobile, Handler handler) {
        if (!this.mIsWorking) {
            this.m_Outhandler = new WeakReference(handler);
            doGetAuth(mobile);
        }
    }

    public void GetToken() {
        doGetToken();
    }

    public boolean IsLogin() {
        return this.mUserInfo.rid > 0;
    }

    private void ClearUserInfo() {
        this.mUserInfo.Clear();
        ClearCookies();
        ClearPtToken();
    }

    private void doCheckNickName(String nickname) {
        new AsyncTask<String, Void, Void>() {
            protected Void doInBackground(String... params) {
                LoginManager.this.realCheckNickName(params[0]);
                return null;
            }
        }.execute(new String[]{nickname});
        this.mIsCheckNickNameWorking = true;
    }

    private void doLogin(String userid, String userpsd) {
        new AsyncTask<String, Void, Void>() {
            protected Void doInBackground(String... params) {
                LoginManager.this.realLogin(params[0], params[1]);
                return null;
            }
        }.execute(new String[]{userid, userpsd});
        this.mIsWorking = true;
    }

    private void doRegister(String userid, String userpsd) {
        new AsyncTask<String, Void, Void>() {
            protected Void doInBackground(String... params) {
                LoginManager.this.realRegister(params[0], params[1]);
                return null;
            }
        }.execute(new String[]{userid, userpsd});
        this.mIsWorking = true;
    }

    private void doAuth() {
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                LoginManager.this.realAuth();
                return null;
            }
        }.execute(new Void[0]);
        this.mIsWorking = true;
    }

    private void doGetToken() {
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                LoginManager.this.realGetToken();
                return null;
            }
        }.execute(new Void[0]);
    }

    private void doLogout() {
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... voids) {
                LoginManager.this.realLogout();
                return null;
            }
        }.execute(new Void[0]);
        this.mIsWorking = true;
    }

    private void doGetAuth(String mobile) {
        new AsyncTask<String, Void, Void>() {
            protected Void doInBackground(String... params) {
                LoginManager.this.realGetAuth(params[0]);
                return null;
            }
        }.execute(new String[]{mobile});
        this.mIsWorking = true;
    }

    private void doMobileRegister(String mobile, String auth, String nickname, String userpsd) {
        new AsyncTask<String, Void, Void>() {
            protected Void doInBackground(String... params) {
                LoginManager.this.realMobileRegister(params[0], params[1], params[2], params[3]);
                return null;
            }
        }.execute(new String[]{mobile, auth, nickname, userpsd});
        this.mIsWorking = true;
    }

    public void SaveCookies() {
        CookieContiner.setCookies(this.mCookieStore);
    }

    public void SaveStringCookies(String cookie) {
        this.mCookieStore.clear();
        this.mCookieStore.LoadCookieString(cookie, UrlConst.BASE_DOMAIN_URL, "/");
        SaveCookies();
    }

    public void ClearCookies() {
        CookieContiner.clearCookies();
    }

    public void SavePtToken(PtTokenInfo info) {
        CookieContiner.setPtToken(info);
    }

    public void ClearPtToken() {
        CookieContiner.clearPtToken();
    }

    private int CheckLoginStatus() {
        int nResult = -1;
        String authseq = new String();
        try {
            authseq = getMD5(UUID.randomUUID().toString());
            HttpGet getMethod = new HttpGet(UrlConst.getCheckLoginStatusUrl(authseq));
            getMethod.setHeader(HTTP_PRIVATE_HEADER_KEY, "1");
            try {
                HttpResponse response = new DefaultHttpClient().execute(getMethod, this.mLocalContext);
                if (response.getStatusLine().getStatusCode() == 200) {
                    SaveCookies();
                    try {
                        JSONObject jsonobj = new JSONObject(EntityUtils.toString(response.getEntity(), "utf-8"));
                        if (jsonobj.getInt("errno") == 0) {
                            JSONObject jsondata;
                            String ret="";
                            PtTokenInfo info;
                            if (!jsonobj.has("authseq")) {
                                jsondata = jsonobj.getJSONObject("data");
                                if (jsondata != null) {
//                                    ret = jsondata.getString(SystemUtils.IS_LOGIN);
                                    if (ret == null || !ret.equalsIgnoreCase("true")) {
                                        nResult = -1;
                                    } else {
                                        info = new PtTokenInfo();
                                        info.strToken = jsondata.getString("token");
                                        info.strTime = jsondata.getString("time");
                                        SavePtToken(info);
                                        nResult = 0;
                                    }
                                }
                            } else if (jsonobj.getString("authseq").equals(authseq)) {
                                jsondata = jsonobj.getJSONObject("data");
                                if (jsondata != null) {
//                                    ret = jsondata.getString(SystemUtils.IS_LOGIN);
                                    if (ret == null || !ret.equalsIgnoreCase("true")) {
                                        nResult = -1;
                                    } else {
                                        info = new PtTokenInfo();
                                        info.strToken = jsondata.getString("token");
                                        info.strTime = jsondata.getString("time");
                                        SavePtToken(info);
                                        nResult = 0;
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                } else {
                    nResult = -2;
                }
            } catch (ClientProtocolException e2) {
                nResult = -2;
            } catch (IOException e3) {
                nResult = -2;
            }
            return nResult;
        } catch (Exception e4) {
            LogUtils.e(TAG, e4.toString());
            return -2;
        }
    }

    private void realCheckNickName(String nickname) {
        int nResult = 0;
        String nicknameencode = new String();
        try {
            HttpGet getMethod = new HttpGet(UrlConst.getCheckNickNameUrl(URLEncoder.encode(nickname, "UTF-8")));
            getMethod.setHeader(HTTP_PRIVATE_HEADER_KEY, "1");
            DefaultHttpClient httpClient = new DefaultHttpClient();
            String strRet = new String();
            try {
                HttpResponse response = httpClient.execute(getMethod, this.mLocalContext);
                if (response.getStatusLine().getStatusCode() == 200) {
                    try {
                        JSONObject jsonobj = new JSONObject(EntityUtils.toString(response.getEntity(), "utf-8"));
                        if (jsonobj.getInt("errno") != 0) {
                            strRet = jsonobj.getString("errmsg");
                            nResult = -1;
                        } else {
                            JSONObject jsondata = jsonobj.getJSONObject("data");
                            if (jsondata == null) {
                                nResult = -1;
                            } else if (jsondata.getString("check").equalsIgnoreCase("true")) {
                                nResult = -1;
                                Context context = (Context) this.mContext.get();
                                if (context != null) {
                                    strRet = context.getString(R.string.register_notify_nickname_exist);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (ClientProtocolException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            SendCheckNickNameComplete(nResult, strRet);
        } catch (Exception e4) {
            e4.printStackTrace();
            SendLoginComplete(-1, "");
        }
    }

    private void realGetAuth(String mobile) {
        int nResult = 0;
        String mobileencode = new String();
        try {
            mobileencode = URLEncoder.encode(mobile, "UTF-8");
            HttpGet getMethod = new HttpGet(UrlConst.getSendRegisterSmsUrl(mobile));
            getMethod.setHeader(HTTP_PRIVATE_HEADER_KEY, "1");
            DefaultHttpClient httpClient = new DefaultHttpClient();
            String strRet = new String();
            try {
                HttpResponse response = httpClient.execute(getMethod, this.mLocalContext);
                if (response.getStatusLine().getStatusCode() == 200) {
                    try {
                        JSONObject jsonobj = new JSONObject(EntityUtils.toString(response.getEntity(), "utf-8"));
                        if (jsonobj.getInt("errno") != 0) {
                            nResult = -1;
                            strRet = jsonobj.getString("errmsg");
                        } else if (!jsonobj.getString("data").equals("true")) {
                            nResult = -1;
                        }
                    } catch (Exception e) {
                        nResult = -1;
                        e.printStackTrace();
                    }
                } else {
                    nResult = -1;
                }
            } catch (ClientProtocolException e2) {
                e2.printStackTrace();
                nResult = -1;
            } catch (IOException e3) {
                e3.printStackTrace();
                nResult = -1;
            }
            SendGetMobileAuthComplete(nResult, strRet);
        } catch (Exception e4) {
            e4.printStackTrace();
            SendLoginComplete(-1, "");
        }
    }

    private void realLogout() {
        String strCookies = new String();
        List<Cookie> lstcookies = this.mCookieStore.getCookies();
        if (lstcookies != null) {
            for (Cookie o : lstcookies) {
                if (!strCookies.isEmpty()) {
                    strCookies = strCookies + ";";
                }
                String key = o.getName();
                strCookies = strCookies + key + "=" + o.getValue();
            }
        }
        if (!strCookies.isEmpty()) {
            SendLogout(strCookies);
        }
        SendLogoutComplete(0, "");
    }

    private String GetAESkey() {
        HttpGet getMethod = new HttpGet(UrlConst.getAesKeyUrl());
        getMethod.setHeader(HTTP_PRIVATE_HEADER_KEY, "1");
        HttpClient httpClient = new DefaultHttpClient();
        String aeskey = new String();
        try {
            this.mCookieStore.clear();
            HttpResponse response = httpClient.execute(getMethod, this.mLocalContext);
            if (response.getStatusLine().getStatusCode() == 200) {
                LogUtils.d(TAG, "Login key cookie is " + this.mCookieStore.toString());
                try {
                    aeskey = new JSONObject(EntityUtils.toString(response.getEntity(), "utf-8")).getString("data");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (ClientProtocolException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return aeskey;
    }

    private void realLogin(String userid, String userpsd) {
        int nResult = 0;
        String strCompleteResult = new String();
        String aeskey = GetAESkey();
        if (aeskey.isEmpty()) {
            SendLoginComplete(-1, strCompleteResult);
            return;
        }
        String aespasssword = new String();
        try {
            byte[] dataBytes = new AESUtils(aeskey).encrypt(userpsd);
            byte[] dataTempBytes = new byte[dataBytes.length];
            System.arraycopy(dataBytes, 0, dataTempBytes, 0, dataBytes.length);
            aespasssword = URLEncoder.encode(Base64.encodeToString(dataTempBytes, 0), "UTF-8");
            String useridencode = new String();
            try {
                useridencode = URLEncoder.encode(userid, "UTF-8");
                String authseq = new String();
                try {
                    authseq = getMD5(UUID.randomUUID().toString());
                    String loginUrl = UrlConst.getLoginUrl(useridencode, aespasssword, authseq);
                    LogUtils.d(TAG, "Login key string is " + aeskey + " AES password is " + aespasssword);
                    try {
                        HttpGet getloginMethod = new HttpGet(loginUrl);
                        getloginMethod.setHeader(HTTP_PRIVATE_HEADER_KEY, "1");
                        DefaultHttpClient httploginClient = new DefaultHttpClient();
                        LogUtils.d(TAG, "Login prepare cookie is " + this.mCookieStore.toString());
                        HttpResponse response = httploginClient.execute(getloginMethod, this.mLocalContext);
                        if (response.getStatusLine().getStatusCode() == 200) {
                            SaveCookies();
                            LogUtils.d(TAG, "Login end cookie is " + this.mCookieStore.toString());
                            try {
                                JSONObject jsonobj = new JSONObject(EntityUtils.toString(response.getEntity(), "utf-8"));
                                int error = jsonobj.getInt("errno");
                                if (error != 0) {
                                    String text = jsonobj.getString("errmsg");
                                    strCompleteResult = text;
                                    LogUtils.d(TAG, "Login failed,errno" + error + ",error is " + text);
                                    nResult = -1;
                                } else if (!jsonobj.has("authseq")) {
                                    JSONObject dataobj = jsonobj.getJSONObject("data");
                                    if (dataobj != null) {
                                        this.mUserInfo.rid = dataobj.getInt("");
                                        this.mUserInfo.nickName = dataobj.getString("nickName");
                                        this.mUserInfo.avatar = dataobj.getString("avatar");
                                        this.mUserInfo.loginEmail = dataobj.getString("email");
                                        this.mUserInfo.mobile = dataobj.getString("mobile");
                                        this.mUserInfo.loginTime = dataobj.getString("time");
                                        this.mUserInfo.modifyTime = dataobj.getString("modifyTime");
                                    } else {
                                        nResult = -1;
                                    }
                                } else if (jsonobj.getString("authseq").equals(authseq)) {
                                    JSONObject dataobj = jsonobj.getJSONObject("data");
                                    if (dataobj != null) {
                                        this.mUserInfo.rid = dataobj.getInt("");
                                        this.mUserInfo.nickName = dataobj.getString("nickName");
                                        this.mUserInfo.avatar = dataobj.getString("avatar");
                                        this.mUserInfo.loginEmail = dataobj.getString("email");
                                        this.mUserInfo.mobile = dataobj.getString("mobile");
                                        this.mUserInfo.loginTime = dataobj.getString("time");
                                        this.mUserInfo.modifyTime = dataobj.getString("modifyTime");
                                    } else {
                                        nResult = -1;
                                    }
                                } else {
                                    nResult = -1;
                                }
                            } catch (Exception e) {
                                nResult = -1;
                                e.printStackTrace();
                            }
                        } else {
                            nResult = -1;
                        }
                    } catch (ClientProtocolException e2) {
                        e2.printStackTrace();
                        nResult = -1;
                    } catch (IOException e3) {
                        e3.printStackTrace();
                        nResult = -1;
                    }
                    if (nResult == 0) {
                        nResult = sendPtTokenRequest();
                    }
                    SendLoginComplete(nResult, strCompleteResult);
                } catch (Exception e4) {
                    LogUtils.e(TAG, e4.toString());
                    SendLoginComplete(-1, strCompleteResult);
                }
            } catch (Exception e42) {
                e42.printStackTrace();
                SendLoginComplete(-1, strCompleteResult);
            }
        } catch (Exception e422) {
            e422.printStackTrace();
            SendLoginComplete(-1, strCompleteResult);
        }
    }

    private void realMobileRegister(String mobile, String auth, String nickname, String userpsd) {
        int nResult = 0;
        String strCompleteResult = new String();
        String aeskey = GetAESkey();
        if (aeskey.isEmpty()) {
            SendMobileRegisterComplete(-1, strCompleteResult);
            return;
        }
        String aespasssword = new String();
        try {
            byte[] dataBytes = new AESUtils(aeskey).encrypt(userpsd);
            byte[] dataTempBytes = new byte[dataBytes.length];
            System.arraycopy(dataBytes, 0, dataTempBytes, 0, dataBytes.length);
            aespasssword = URLEncoder.encode(Base64.encodeToString(dataTempBytes, 0), "UTF-8");
            String mobilecode = new String();
            try {
                mobilecode = URLEncoder.encode(mobile, "UTF-8");
                String authcode = new String();
                try {
                    authcode = URLEncoder.encode(auth, "UTF-8");
                    String ninknamecode = new String();
                    try {
                        HttpGet getloginMethod = new HttpGet(UrlConst.getMobileRegisterUrl(mobilecode, authcode, URLEncoder.encode(nickname, "UTF-8"), aespasssword));
                        getloginMethod.setHeader(HTTP_PRIVATE_HEADER_KEY, "1");
                        try {
                            HttpResponse response = new DefaultHttpClient().execute(getloginMethod, this.mLocalContext);
                            if (response.getStatusLine().getStatusCode() == 200) {
                                LogUtils.d(TAG, "register cookie is " + this.mCookieStore.toString());
                                SaveCookies();
                                try {
                                    JSONObject jsonobj = new JSONObject(EntityUtils.toString(response.getEntity(), "utf-8"));
                                    int error = jsonobj.getInt("errno");
                                    if (error == 0) {
                                        JSONObject dataobj = jsonobj.getJSONObject("data");
                                        if (dataobj != null) {
                                            this.mUserInfo.rid = dataobj.getInt("");
                                            this.mUserInfo.nickName = dataobj.getString("nickName");
                                            this.mUserInfo.avatar = dataobj.getString("avatar");
                                            this.mUserInfo.loginEmail = dataobj.getString("email");
                                            this.mUserInfo.mobile = dataobj.getString("mobile");
                                            this.mUserInfo.loginTime = dataobj.getString("time");
                                            this.mUserInfo.modifyTime = dataobj.getString("modifyTime");
                                        } else {
                                            nResult = -1;
                                        }
                                    } else {
                                        if (error == 1405) {
                                            Context context = (Context) this.mContext.get();
                                            if (context != null) {
                                                strCompleteResult = context.getString(R.string.register_mobile_notify_auth_error);
                                            } else {
                                                strCompleteResult = jsonobj.getString("errmsg");
                                            }
                                        } else {
                                            String text = jsonobj.getString("errmsg");
                                            strCompleteResult = text;
                                            LogUtils.d(TAG, "Register failed,errno" + error + ",error is " + text);
                                        }
                                        nResult = -1;
                                    }
                                } catch (Exception e) {
                                    nResult = -1;
                                    e.printStackTrace();
                                }
                            } else {
                                nResult = -1;
                            }
                        } catch (ClientProtocolException e2) {
                            e2.printStackTrace();
                            nResult = -1;
                        } catch (IOException e3) {
                            e3.printStackTrace();
                            nResult = -1;
                        }
                        SendMobileRegisterComplete(nResult, strCompleteResult);
                    } catch (Exception e4) {
                        e4.printStackTrace();
                        SendMobileRegisterComplete(-1, strCompleteResult);
                    }
                } catch (Exception e42) {
                    e42.printStackTrace();
                    SendMobileRegisterComplete(-1, strCompleteResult);
                }
            } catch (Exception e422) {
                e422.printStackTrace();
                SendMobileRegisterComplete(-1, strCompleteResult);
            }
        } catch (Exception e4222) {
            e4222.printStackTrace();
            SendMobileRegisterComplete(-1, strCompleteResult);
        }
    }

    private void realRegister(String userid, String userpsd) {
        int nResult = 0;
        String strCompleteResult = new String();
        String aeskey = GetAESkey();
        if (aeskey.isEmpty()) {
            SendRegisterComplete(-1, strCompleteResult);
            return;
        }
        String aespasssword = new String();
        try {
            byte[] dataBytes = new AESUtils(aeskey).encrypt(userpsd);
            byte[] dataTempBytes = new byte[dataBytes.length];
            System.arraycopy(dataBytes, 0, dataTempBytes, 0, dataBytes.length);
            aespasssword = URLEncoder.encode(Base64.encodeToString(dataTempBytes, 0), "UTF-8");
            String useridencode = new String();
            try {
                HttpGet getloginMethod = new HttpGet(UrlConst.getRegisterUrl(URLEncoder.encode(userid, "UTF-8"), aespasssword));
                getloginMethod.setHeader(HTTP_PRIVATE_HEADER_KEY, "1");
                try {
                    HttpResponse response = new DefaultHttpClient().execute(getloginMethod, this.mLocalContext);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        LogUtils.d(TAG, "register cookie is " + this.mCookieStore.toString());
                        SaveCookies();
                        try {
                            JSONObject jsonobj = new JSONObject(EntityUtils.toString(response.getEntity(), "utf-8"));
                            int error = jsonobj.getInt("errno");
                            if (error == 0) {
                                JSONObject dataobj = jsonobj.getJSONObject("data");
                                if (dataobj != null) {
                                    this.mUserInfo.rid = dataobj.getInt("");
                                    this.mUserInfo.nickName = dataobj.getString("nickName");
                                    this.mUserInfo.avatar = dataobj.getString("avatar");
                                    this.mUserInfo.loginEmail = dataobj.getString("email");
                                    this.mUserInfo.mobile = dataobj.getString("mobile");
                                    this.mUserInfo.loginTime = dataobj.getString("time");
                                    this.mUserInfo.modifyTime = dataobj.getString("modifyTime");
                                } else {
                                    nResult = -1;
                                }
                            } else {
                                String text = jsonobj.getString("errmsg");
                                strCompleteResult = text;
                                LogUtils.d(TAG, "Register failed,errno" + error + ",error is " + text);
                                nResult = -1;
                            }
                        } catch (Exception e) {
                            nResult = -1;
                            e.printStackTrace();
                        }
                    } else {
                        nResult = -1;
                    }
                } catch (ClientProtocolException e2) {
                    e2.printStackTrace();
                    nResult = -1;
                } catch (IOException e3) {
                    e3.printStackTrace();
                    nResult = -1;
                }
                SendRegisterComplete(nResult, strCompleteResult);
            } catch (Exception e4) {
                e4.printStackTrace();
                SendRegisterComplete(-1, strCompleteResult);
            }
        } catch (Exception e42) {
            e42.printStackTrace();
            SendRegisterComplete(-1, strCompleteResult);
        }
    }

    private void realAuth() {
        String strCompleteResult = new String();
        int nResult = CheckLoginStatus();
        if (nResult != 0) {
            SendAuthComplete(nResult, strCompleteResult);
            return;
        }
        String authseq = new String();
        try {
            authseq = getMD5(UUID.randomUUID().toString());
            try {
                HttpGet getloginMethod = new HttpGet(UrlConst.getMyInfoUrl(authseq));
                getloginMethod.setHeader(HTTP_PRIVATE_HEADER_KEY, "1");
                HttpResponse response = new DefaultHttpClient().execute(getloginMethod, this.mLocalContext);
                if (response.getStatusLine().getStatusCode() == 200) {
                    SaveCookies();
                    LogUtils.d(TAG, "Auth end cookie is " + this.mCookieStore.toString());
                    try {
                        JSONObject jsonobj = new JSONObject(EntityUtils.toString(response.getEntity(), "utf-8"));
                        int error = jsonobj.getInt("errno");
                        if (error != 0) {
                            String text = jsonobj.getString("errmsg");
                            strCompleteResult = text;
                            LogUtils.d(TAG, "Login failed,errno" + error + ",error is " + text);
                            nResult = -2;
                        } else if (!jsonobj.has("authseq")) {
                            JSONObject dataobj = jsonobj.getJSONObject("data");
                            if (dataobj != null) {
                                int rid = dataobj.getInt("");
                                if (rid > 0) {
                                    this.mUserInfo.rid = rid;
                                    this.mUserInfo.nickName = dataobj.getString("nickName");
                                    this.mUserInfo.avatar = dataobj.getString("avatar");
                                    this.mUserInfo.loginEmail = dataobj.getString("email");
                                    this.mUserInfo.mobile = dataobj.getString("mobile");
                                } else {
                                    nResult = -2;
                                }
                            } else {
                                nResult = -2;
                            }
                        } else if (jsonobj.getString("authseq").equals(authseq)) {
                            JSONObject dataobj = jsonobj.getJSONObject("data");
                            if (dataobj != null) {
                                int rid = dataobj.getInt("");
                                if (rid > 0) {
                                    this.mUserInfo.rid = rid;
                                    this.mUserInfo.nickName = dataobj.getString("nickName");
                                    this.mUserInfo.avatar = dataobj.getString("avatar");
                                    this.mUserInfo.loginEmail = dataobj.getString("email");
                                    this.mUserInfo.mobile = dataobj.getString("mobile");
                                } else {
                                    nResult = -2;
                                }
                            } else {
                                nResult = -2;
                            }
                        } else {
                            nResult = -2;
                        }
                    } catch (Exception e) {
                        nResult = -2;
                    }
                } else {
                    nResult = -2;
                }
            } catch (ClientProtocolException e2) {
                nResult = -2;
            }
            SendAuthComplete(nResult, strCompleteResult);
        } catch (Exception e4) {
            LogUtils.e(TAG, e4.toString());
            SendAuthComplete(-2, strCompleteResult);
        }
    }

    private void SendAuthComplete(int err, String msgtext) {
        if (this.m_Outhandler == null) {
            this.mIsWorking = false;
            if (err != 0) {
                ClearUserInfo();
                if (err != -2) {
                    this.mCookieStore.clear();
                    return;
                }
                return;
            }
            return;
        }
        Handler handler = (Handler) this.m_Outhandler.get();
        if (handler != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("err", err);
            bundle.putString("msg", msgtext);
            Message msg = new Message();
            msg.what = MSG_OUT_AUTH_COMPLETE;
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
        this.mIsWorking = false;
    }

    private void SendLogoutComplete(int err, String msgtext) {
        ClearUserInfo();
        this.mCookieStore.clear();
        if (this.m_Outhandler == null) {
            this.mIsWorking = false;
            return;
        }
        Handler handler = (Handler) this.m_Outhandler.get();
        if (handler != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("err", err);
            bundle.putString("msg", msgtext);
            Message msg = new Message();
            msg.what = MSG_OUT_LOGOUT_COMPLETE;
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
        if (this.m_MsgHandler != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("err", err);
            bundle.putString("msg", msgtext);
            Message msg = new Message();
            msg.what = MSG_OUT_LOGOUT_COMPLETE;
            msg.setData(bundle);
            this.m_MsgHandler.sendMessage(msg);
        }
        this.mIsWorking = false;
    }

    private void SendRegisterComplete(int err, String msgtext) {
        if (err != 0) {
            ClearUserInfo();
        }
        if (this.m_Outhandler == null) {
            this.mIsWorking = false;
            return;
        }
        Handler handler = (Handler) this.m_Outhandler.get();
        if (handler != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("err", err);
            bundle.putString("msg", msgtext);
            Message msg = new Message();
            msg.what = MSG_OUT_REGISTER_COMPLETE;
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
        this.mIsWorking = false;
    }

    private void SendLoginComplete(int err, String msgtext) {
        if (err != 0) {
            ClearUserInfo();
            this.mCookieStore.clear();
        }
        Handler handler = (Handler) this.m_Outhandler.get();
        if (handler != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("err", err);
            bundle.putString("msg", msgtext);
            Message msg = new Message();
            msg.what = 257;
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
        if (this.m_MsgHandler != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("err", err);
            bundle.putString("msg", msgtext);
            Message msg = new Message();
            msg.what = 257;
            msg.setData(bundle);
            this.m_MsgHandler.sendMessage(msg);
        }
        this.mIsWorking = false;
    }

    private void SendGetMobileAuthComplete(int err, String msgtext) {
        if (this.m_Outhandler == null) {
            this.mIsWorking = false;
            return;
        }
        Handler handler = (Handler) this.m_Outhandler.get();
        if (handler != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("err", err);
            bundle.putString("msg", msgtext);
            Message msg = new Message();
            msg.what = MSG_OUT_GET_AUTH_COMPLETE;
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
        this.mIsWorking = false;
    }

    private void SendMobileRegisterComplete(int err, String msgtext) {
        if (err != 0) {
            ClearUserInfo();
        }
        if (this.m_Outhandler == null) {
            this.mIsWorking = false;
            return;
        }
        Handler handler = (Handler) this.m_Outhandler.get();
        if (handler != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("err", err);
            bundle.putString("msg", msgtext);
            Message msg = new Message();
            msg.what = MSG_OUT_MOBILE_REGISTER_COMPLETE;
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
        if (this.m_MsgHandler != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("err", err);
            bundle.putString("msg", "");
            Message msg = new Message();
            msg.what = 257;
            msg.setData(bundle);
            this.m_MsgHandler.sendMessage(msg);
        }
        this.mIsWorking = false;
    }

    private void SendCheckNickNameComplete(int err, String msgtext) {
        if (this.m_OutCheckNickNameHandler == null) {
            this.mIsCheckNickNameWorking = false;
            return;
        }
        Handler handler = (Handler) this.m_OutCheckNickNameHandler.get();
        if (handler != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("err", err);
            bundle.putString("msg", msgtext);
            Message msg = new Message();
            msg.what = MSG_OUT_CHECK_NICKNAME_COMPLETE;
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
        this.mIsCheckNickNameWorking = false;
    }

    private int sendPtTokenRequest() {
        if (CookieContiner.getCookies().isEmpty()) {
            return -1;
        }
        String strUrl = UrlConst.getTokenUrl();
        PtTokenInfo info = new PtTokenInfo();
        StringBuffer buffer = new StringBuffer();
        if (!HttpRequest.sendSync(strUrl, buffer)) {
            return -1;
        }
        try {
            JSONObject jsonobj = new JSONObject(buffer.toString());
            if (jsonobj.getInt("errno") != 0) {
                return -1;
            }
            JSONObject dataobj = jsonobj.getJSONObject("data");
            if (dataobj == null) {
                return -1;
            }
            info.rid = dataobj.getInt("");
            if (info.rid <= 0) {
                return -1;
            }
            info.strTime = dataobj.getString("time");
            info.strToken = dataobj.getString("token");
            SavePtToken(info);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void timeLoop() {
        new Timer().schedule(new TimerTask() {
            public void run() {
                LoginManager.this.sendPtTokenRequest();
            }
        }, 3600000, 3600000);
    }

    private void realGetToken() {
        for (int trycount = 3; trycount > 0 && sendPtTokenRequest() != 0; trycount--) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
        }
    }

    public void asynSendPtTokenRequest() {
        new AsyncTask<String, Integer, Integer>() {
            protected Integer doInBackground(String... params) {
                LoginManager.this.sendPtTokenRequest();
                return Integer.valueOf(0);
            }
        }.execute(new String[0]);
    }

    private void SendLogout(final String strCookies) {
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... voids) {
                HttpGet getMethod = new HttpGet(UrlConst.getLogoutUrl());
                getMethod.setHeader(LoginManager.HTTP_PRIVATE_HEADER_KEY, "1");
                getMethod.setHeader("Cookie", strCookies);
                try {
                    HttpResponse response = new DefaultHttpClient().execute(getMethod);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        try {
                            if (new JSONObject(EntityUtils.toString(response.getEntity(), "utf-8")).getInt("errno") != 0) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (ClientProtocolException e2) {
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
                return null;
            }
        }.execute(new Void[0]);
    }
}
