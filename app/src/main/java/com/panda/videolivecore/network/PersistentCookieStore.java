package com.panda.videolivecore.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import com.panda.videolivecore.utils.LogUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

public class PersistentCookieStore implements CookieStore {
    private static final String COOKIE_NAME_PREFIX = "cookie_";
    private static final String COOKIE_NAME_STORE = "names";
    private static final String COOKIE_PREFS = "CookiePrefsFile";
    private static final String LOG_TAG = "PersistentCookieStore";
    private final SharedPreferences cookiePrefs;
    private final ConcurrentHashMap<String, Cookie> cookies = new ConcurrentHashMap();
    private boolean omitNonPersistentCookies = false;

    public PersistentCookieStore(Context context) {
        this.cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
        if (this.cookiePrefs != null) {
            String storedCookieNames = this.cookiePrefs.getString(COOKIE_NAME_STORE, null);
            if (storedCookieNames != null) {
                for (String name : TextUtils.split(storedCookieNames, ",")) {
                    String encodedCookie = this.cookiePrefs.getString(COOKIE_NAME_PREFIX + name, null);
                    if (encodedCookie != null) {
                        Cookie decodedCookie = decodeCookie(encodedCookie);
                        if (decodedCookie != null) {
                            this.cookies.put(name, decodedCookie);
                        }
                    }
                }
                clearExpired(new Date());
            }
        }
    }

    public void LoadCookieString(String cookievalue, String strDomain, String strPath) {
        try {
            String[] cookieList = cookievalue.split(";");
            if (cookieList.length > 0) {
                for (String strItem : cookieList) {
                    String[] cookieKeyValue = strItem.split("=");
                    if (cookieKeyValue.length == 2) {
                        String name = cookieKeyValue[0];
                        String value = cookieKeyValue[1];
                        if (!(name == null || value == null)) {
                            name.trim();
                            value.trim();
                            BasicClientCookie cookie = new BasicClientCookie(name, value);
                            cookie.setDomain(strDomain);
                            cookie.setPath(strPath);
                            addCookie(cookie);
                        }
                    }
                }
            }
        } catch (Exception e) {
//            LogUtil.e("LoadCookieString", e.toString());
        }
    }

    public void addCookie(Cookie cookie) {
        if (!this.omitNonPersistentCookies || cookie.isPersistent()) {
            String name = cookie.getName() + cookie.getDomain();
            if (cookie.isExpired(new Date())) {
                this.cookies.remove(name);
            } else {
                this.cookies.put(name, cookie);
            }
            Editor prefsWriter = this.cookiePrefs.edit();
            prefsWriter.putString(COOKIE_NAME_STORE, TextUtils.join(",", this.cookies.keySet()));
            prefsWriter.putString(COOKIE_NAME_PREFIX + name, encodeCookie(new SerializableCookie(cookie)));
            prefsWriter.commit();
        }
    }

    public void clear() {
        Editor prefsWriter = this.cookiePrefs.edit();
        for (String name : this.cookies.keySet()) {
            prefsWriter.remove(COOKIE_NAME_PREFIX + name);
        }
        prefsWriter.remove(COOKIE_NAME_STORE);
        prefsWriter.commit();
        this.cookies.clear();
    }

    public boolean clearExpired(Date date) {
        boolean clearedAny = false;
        Editor prefsWriter = this.cookiePrefs.edit();
        for (Entry<String, Cookie> entry : this.cookies.entrySet()) {
            String name = (String) entry.getKey();
            if (((Cookie) entry.getValue()).isExpired(date)) {
                this.cookies.remove(name);
                prefsWriter.remove(COOKIE_NAME_PREFIX + name);
                clearedAny = true;
            }
        }
        if (clearedAny) {
            prefsWriter.putString(COOKIE_NAME_STORE, TextUtils.join(",", this.cookies.keySet()));
        }
        prefsWriter.commit();
        return clearedAny;
    }

    public List<Cookie> getCookies() {
        return new ArrayList(this.cookies.values());
    }

    public void setOmitNonPersistentCookies(boolean omitNonPersistentCookies) {
        this.omitNonPersistentCookies = omitNonPersistentCookies;
    }

    public void deleteCookie(Cookie cookie) {
        String name = cookie.getName() + cookie.getDomain();
        this.cookies.remove(name);
        Editor prefsWriter = this.cookiePrefs.edit();
        prefsWriter.remove(COOKIE_NAME_PREFIX + name);
        prefsWriter.commit();
    }

    protected String encodeCookie(SerializableCookie cookie) {
        if (cookie == null) {
            return null;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(os).writeObject(cookie);
            return byteArrayToHexString(os.toByteArray());
        } catch (IOException e) {
            return null;
        }
    }

    protected Cookie decodeCookie(String cookieString) {
        Cookie cookie = null;
        try {
            cookie = ((SerializableCookie) new ObjectInputStream(new ByteArrayInputStream(hexStringToByteArray(cookieString))).readObject()).getCookie();
        } catch (IOException e) {
            LogUtils.d(LOG_TAG, "IOException in decodeCookie");
        } catch (ClassNotFoundException e2) {
            LogUtils.d(LOG_TAG, "ClassNotFoundException in decodeCookie");
        }
        return cookie;
    }

    protected String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 255;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    protected byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[(len / 2)];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}
