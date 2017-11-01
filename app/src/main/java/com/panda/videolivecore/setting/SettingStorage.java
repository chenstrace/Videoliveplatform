package com.panda.videolivecore.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingStorage {
    private static String mCurrentDate = "";
    private static int mDanmuFontSize = 8;
    private static boolean mDanmuIsOpen = true;
    private static int mDanmuPos = 0;
    private static int mDanmuSpeed = 11;
    private static int mDanmuTransparency = 10;
    private static boolean mPlayingNotify = true;
    private static int mScreenLight = -1;
    private static Context m_Content = null;

    public static void Init(Context context) {
        m_Content = context;
        ReadSharedPreferences();
    }

    private static void ReadSharedPreferences() {
        if (m_Content != null) {
            SharedPreferences setting = m_Content.getSharedPreferences("setting_info", 0);
            if (setting != null) {
                mDanmuIsOpen = setting.getBoolean("DANMUISOPEN", true);
                mPlayingNotify = setting.getBoolean("PLAYINGNOTIFY", true);
                mCurrentDate = setting.getString("CURRENTDATE", "1973-00-00");
                mDanmuTransparency = setting.getInt("DANMUTRANSPARENCY", 10);
                mDanmuFontSize = setting.getInt("DANMUFONTSIZE", 8);
                mDanmuPos = setting.getInt("DANMUPOS", 0);
                mDanmuSpeed = setting.getInt("DANMUSPEED", 11);
                mScreenLight = setting.getInt("SCREENLIGHT", -1);
            }
        }
    }

    private static void WriteSharedPreferences(String name, int value) {
        if (m_Content != null) {
            Editor edit = m_Content.getSharedPreferences("setting_info", 0).edit();
            edit.putInt(name, value);
            edit.commit();
        }
    }

    private static void WriteSharedPreferences(String name, boolean value) {
        if (m_Content != null) {
            Editor edit = m_Content.getSharedPreferences("setting_info", 0).edit();
            edit.putBoolean(name, value);
            edit.commit();
        }
    }

    private static void WriteSharedPreferences(String name, String value) {
        if (m_Content != null) {
            Editor edit = m_Content.getSharedPreferences("setting_info", 0).edit();
            edit.putString(name, value);
            edit.commit();
        }
    }

    public static boolean IsPlayingNotify() {
        return mPlayingNotify;
    }

    public static void SetPlayNotify(boolean bOn) {
        mPlayingNotify = bOn;
        WriteSharedPreferences("PLAYINGNOTIFY", mPlayingNotify);
    }

    public static String IsCurrentDate() {
        return mCurrentDate;
    }

    public static void SetCurrentDate(String strDate) {
        mCurrentDate = strDate;
        WriteSharedPreferences("CURRENTDATE", mCurrentDate);
    }

    public static boolean IsDanmuOpen() {
        return mDanmuIsOpen;
    }

    public static void SetDanmuOpen(boolean bOn) {
        mDanmuIsOpen = bOn;
        WriteSharedPreferences("DANMUISOPEN", mDanmuIsOpen);
    }

    public static int GetDanmuPos() {
        return mDanmuPos;
    }

    public static void SetDanmuPos(int pos) {
        mDanmuPos = pos;
        WriteSharedPreferences("DANMUPOS", mDanmuPos);
    }

    public static int GetDanmuTransparency() {
        return mDanmuTransparency;
    }

    public static void SetDanmuTransparency(int n) {
        mDanmuTransparency = n;
        WriteSharedPreferences("DANMUTRANSPARENCY", mDanmuTransparency);
    }

    public static int GetDanmuFontSize() {
        return mDanmuFontSize;
    }

    public static void SetDanmuFontSize(int n) {
        mDanmuFontSize = n;
        WriteSharedPreferences("DANMUFONTSIZE", mDanmuFontSize);
    }

    public static int GetDanmuSpeed() {
        return mDanmuSpeed;
    }

    public static void SetDanmuSpeed(int n) {
        mDanmuSpeed = n;
        WriteSharedPreferences("DANMUSPEED", mDanmuSpeed);
    }

    public static int GetScreenLight() {
        return mScreenLight;
    }

    public static void SetScreenLight(int n) {
        mScreenLight = n;
        WriteSharedPreferences("SCREENLIGHT", mScreenLight);
    }
}
