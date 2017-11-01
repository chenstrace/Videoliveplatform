package com.panda.videolivecore;

import android.app.Application;
import android.text.TextUtils;

import com.panda.videolivecore.network.LoginManager;
//import com.panda.videolivecore.network.LoginManager;

public class CoreApplication {
    public static final int MSG_START_LOGIN_ACTIVITY = 257;
    private static CoreApplication g_instance = null;
    private Application mApplication = null;
      private LoginManager mLoginManager = null;
    private String mVersion = "";

    public CoreApplication(Application application) {
        this.mApplication = application;
    }

    public static void makeInstance(Application app) {
        if (g_instance == null) {
            g_instance = new CoreApplication(app);
        }
    }

    public static CoreApplication getInstance() {
        return g_instance;
    }

//  public void newLoginManager() {
//    if (this.mLoginManager == null) {
//      this.mLoginManager = new LoginManager(this.mApplication);
//    }
//  }

    public Application getApplication() {
        return this.mApplication;
    }

  public LoginManager GetLoginManager() {
    return this.mLoginManager;
  }

    public String version() {
        if (TextUtils.isEmpty(this.mVersion)) {
            GetVersion();
        }
        return this.mVersion;
    }

    private void GetVersion() {
        try {
            this.mVersion = this.mApplication.getPackageManager().getPackageInfo(this.mApplication.getPackageName(), 0).versionName;
        } catch (Exception e) {
        }
    }

    public void SendPtTokenRequest() {
//    this.mLoginManager.asynSendPtTokenRequest();
    }

    public void Logout() {
//    this.mLoginManager.MenuLogout();
    }
}