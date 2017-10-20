package com.panda.videoliveplatform;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Process;
import com.panda.videolivecore.CoreApplication;
//import com.panda.videolivecore.network.LoginManager;
//import com.panda.videolivecore.setting.SettingStorage;
//import com.panda.videolivecore.utils.LogUtils;
//import com.sina.weibo.sdk.utils.LogUtil;
//import com.umeng.message.PushAgent;

public class MyApplication extends Application {
    private static final String METAKEY_CHANNEL = "UMENG_CHANNEL";
    private static MyApplication application;
    private String mChannelId = "";
    //private PushAgent mPushAgent = null;
    private String mVersion = "";

    public void onCreate() {
        super.onCreate();
        int pid = Process.myPid();
        for (RunningAppProcessInfo appProcess : ((ActivityManager) getSystemService("activity")).getRunningAppProcesses()) {
            if (appProcess.pid == pid && appProcess.processName.compareToIgnoreCase(getPackageName()) == 0) {
                //LogUtils.SetDebugEnable(this);
                GetVersion();
                getChannelId();
                //CoreApplication.getInstance().newLoginManager();
            }
        }
        loadSetting();
        StartUmengPushAgent();
    }

    private void StartUmengPushAgent() {
//        if (SettingStorage.IsPlayingNotify()) {
//            this.mPushAgent = PushAgent.getInstance(getApplicationContext());
//            this.mPushAgent.enable();
//            this.mPushAgent.onAppStart();
//        }
    }

    private void loadSetting() {
//        SettingStorage.Init(getApplicationContext());
    }

    public MyApplication() {
        application = this;
        CoreApplication.makeInstance(this);
    }

    public static MyApplication getInstance() {
        return application;
    }

//    public LoginManager GetLoginManager() {
//        return CoreApplication.getInstance().GetLoginManager();
//    }

    public String version() {
        return this.mVersion;
    }

    private void GetVersion() {
        try {
            this.mVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {
        }
    }

    private void getChannelId() {
        try {
            this.mChannelId = getPackageManager().getApplicationInfo(getPackageName(), 128).metaData.getString(METAKEY_CHANNEL);
        } catch (NameNotFoundException e) {

        }
    }
}
