package com.panda.videolivecore.utils;

import com.panda.videolivecore.CoreApplication;
import com.panda.videolivecore.net.info.PtTokenInfo;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class CookieContiner {
    public static String COOKIE_NAME = "Cookie";
    private static HashMap<String, String> mCookieContiner = new HashMap();
    private static CookieStore mCookieStore = new BasicCookieStore();
    private static HashMap<String, String> mPtTokenContiner = new HashMap();

    public static synchronized void clearCookies() {
        synchronized (CookieContiner.class) {
            mCookieStore.clear();
            mCookieContiner.clear();
        }
    }

    public static synchronized boolean setCookies(HttpResponse httpResponse) {
        boolean cookies;
        synchronized (CookieContiner.class) {
            cookies = setCookies(httpResponse.getHeaders("Set-Cookie"));
        }
        return cookies;
    }

    public static synchronized boolean setCookies(CookieStore cookieStore) {
        boolean z;
        synchronized (CookieContiner.class) {
            List<Cookie> lstcookies = cookieStore.getCookies();
            if (lstcookies != null) {
                for (Cookie o : lstcookies) {
                    mCookieContiner.put(o.getName(), o.getValue());
                }
                z = true;
            } else {
                z = false;
            }
        }
        return z;
    }

    public static synchronized boolean setCookies(Header[] headers) {
        boolean bReturn;
        synchronized (CookieContiner.class) {
            bReturn = false;
            if (headers != null) {
                for (Header value : headers) {
                    String[] cookievalues = value.getValue().split(";");
                    for (String split : cookievalues) {
                        String[] keyPair = split.split("=");
                        mCookieContiner.put(keyPair[0].trim(), keyPair.length > 1 ? keyPair[1].trim() : "");
                    }
                }
                bReturn = true;
            }
        }
        return bReturn;
    }

    public static synchronized boolean getCookie(String key, StringBuffer strCookie) {
        boolean bReturn;
        synchronized (CookieContiner.class) {
            bReturn = false;
            if (mCookieContiner.containsKey(key)) {
                strCookie.append((String) mCookieContiner.get(key));
                bReturn = true;
            }
        }
        return bReturn;
    }

    public static synchronized String getCookies() {
        String strCookies;
        synchronized (CookieContiner.class) {
            strCookies = "";
            for (Entry<String, String> entry : mCookieContiner.entrySet()) {
                if (!strCookies.isEmpty()) {
                    strCookies = strCookies + ";";
                }
                strCookies = strCookies + ((String) entry.getKey()) + "=" + ((String) entry.getValue());
            }
        }
        return strCookies;
    }

    public static synchronized boolean fillCookieHeader(HttpGet httpGet, String strCookieName) {
        boolean bReturn;
        synchronized (CookieContiner.class) {
            bReturn = false;
            StringBuffer strCookie = new StringBuffer();
            if (getCookie(strCookieName, strCookie)) {
                httpGet.setHeader("Cookie", strCookieName + "=" + strCookie);
                bReturn = true;
            }
        }
        return bReturn;
    }

    public static synchronized Map<String, String> getCookieHeaderMap() {
        Map<String, String> headers;
        synchronized (CookieContiner.class) {
            headers = new HashMap();
            String strCookies = "";
            for (Entry<String, String> entry : mCookieContiner.entrySet()) {
                if (!strCookies.isEmpty()) {
                    strCookies = strCookies + ";";
                }
                strCookies = strCookies + ((String) entry.getKey()) + "=" + ((String) entry.getValue());
            }
            if (!strCookies.isEmpty()) {
                headers.put(COOKIE_NAME, strCookies);
            }
        }
        return headers;
    }

    public static synchronized boolean fillCookieHeaders(HttpGet httpGet) {
        boolean z;
        synchronized (CookieContiner.class) {
            String strCookies = getCookies();
            if (strCookies.isEmpty()) {
                z = false;
            } else {
                httpGet.setHeader(COOKIE_NAME, strCookies);
                z = true;
            }
        }
        return z;
    }

    public static synchronized boolean fillCookieHeaders(HttpURLConnection urlConnection) {
        boolean z;
        synchronized (CookieContiner.class) {
            String strCookies = getCookies();
            if (strCookies.isEmpty()) {
                z = false;
            } else {
                urlConnection.setRequestProperty(COOKIE_NAME, strCookies);
                z = true;
            }
        }
        return z;
    }

    public static synchronized HttpContext getHttpContext() {
        HttpContext httpContext;
        synchronized (CookieContiner.class) {
            httpContext = new BasicHttpContext();
            httpContext.setAttribute("http.cookie-store", mCookieStore);
        }
        return httpContext;
    }

    public static synchronized boolean isLogin() {
        boolean IsLogin;
        synchronized (CookieContiner.class) {
            IsLogin = CoreApplication.getInstance().GetLoginManager().IsLogin();
        }
        return IsLogin;
    }

    public static synchronized void clearPtToken() {
        synchronized (CookieContiner.class) {
            mPtTokenContiner.clear();
        }
    }

    public static synchronized void setPtToken(PtTokenInfo info) {
        synchronized (CookieContiner.class) {
            mPtTokenContiner.put("pt_time", info.strTime);
            mPtTokenContiner.put("pt_sign", info.strToken);
        }
    }

    public static synchronized void fillTokenHeaders(HttpGet httpGet) {
        synchronized (CookieContiner.class) {
            for (Entry<String, String> entry : mPtTokenContiner.entrySet()) {
                if (!(((String) entry.getKey()).isEmpty() || ((String) entry.getValue()).isEmpty())) {
                    httpGet.setHeader((String) entry.getKey(), (String) entry.getValue());
                }
            }
        }
    }

    public static synchronized void fillTokenHeaders(HttpURLConnection urlConnection) {
        synchronized (CookieContiner.class) {
            for (Entry<String, String> entry : mPtTokenContiner.entrySet()) {
                if (!(((String) entry.getKey()).isEmpty() || ((String) entry.getValue()).isEmpty())) {
                    urlConnection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
                }
            }
        }
    }

    public static synchronized String getToken() {
        String strReturn;
        synchronized (CookieContiner.class) {
            strReturn = "";
            for (Entry<String, String> entry : mPtTokenContiner.entrySet()) {
                if (!strReturn.isEmpty()) {
                    strReturn = strReturn + "&";
                }
                strReturn = strReturn + ((String) entry.getKey()) + "=" + ((String) entry.getValue());
            }
        }
        return strReturn;
    }

    public static synchronized String getPtTime() {
        String str;
        synchronized (CookieContiner.class) {
            str = (String) mPtTokenContiner.get("pt_time");
        }
        return str;
    }

    public static synchronized String getPtSign() {
        String str;
        synchronized (CookieContiner.class) {
            str = (String) mPtTokenContiner.get("pt_sign");
        }
        return str;
    }
}