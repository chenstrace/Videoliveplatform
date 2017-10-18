package com.panda.videoliveplatform;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Process;
//import com.panda.videolivecore.CoreApplication;
//import com.panda.videolivecore.network.LoginManager;
//import com.panda.videolivecore.setting.SettingStorage;
//import com.panda.videolivecore.utils.LogUtils;
//import com.sina.weibo.sdk.utils.LogUtil;
//import com.umeng.message.PushAgent;
import java.util.Iterator;
import java.util.List;

public class MyApplication extends Application {
    private static final String METAKEY_CHANNEL = "UMENG_CHANNEL";
    private static MyApplication application;
    private String mChannelId = "";
    //private PushAgent mPushAgent = null;
    private String mVersion = "";

    public MyApplication() {
        application = this;
        //CoreApplication.makeInstance(this);
    }

    private void GetVersion() {
        try {
            String str = getPackageName();
            this.mVersion = getPackageManager().getPackageInfo(str, 0).versionName;
            //label21: return;
        } catch (Exception localException) {
//      break label21;
        }
    }

    private void StartUmengPushAgent() {
    /*
    if (SettingStorage.IsPlayingNotify())
    {
      this.mPushAgent = PushAgent.getInstance(getApplicationContext());
      this.mPushAgent.enable();
      this.mPushAgent.onAppStart();
    }
    */
    }

    private void getChannelId() {
        try {
            this.mChannelId = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            //while (true)
            // LogUtil.e("MyApplication", localNameNotFoundException.toString());
        }
    }

    public static MyApplication getInstance() {
        return application;
    }

    private void loadSetting() {
        //SettingStorage.Init(getApplicationContext());
    }
/*
  public LoginManager GetLoginManager()
  {
    return CoreApplication.getInstance().GetLoginManager();
  }
  */

    public void onCreate() {
        super.onCreate();
        int i = Process.myPid();

        Iterator localIterator = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses().iterator();
        while (localIterator.hasNext()) {
            ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo = (ActivityManager.RunningAppProcessInfo) localIterator.next();
            if ((localRunningAppProcessInfo.pid == i) && (localRunningAppProcessInfo.processName.compareToIgnoreCase(getPackageName()) == 0)) {
                //LogUtils.SetDebugEnable(this);
                GetVersion();
                getChannelId();
                //CoreApplication.getInstance().newLoginManager();
            }
        }
        loadSetting();
        StartUmengPushAgent();
    }

    public String version() {
        return this.mVersion;
    }
}
