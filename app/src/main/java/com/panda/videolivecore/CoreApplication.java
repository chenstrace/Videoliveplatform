package com.panda.videolivecore;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
//import com.panda.videolivecore.network.LoginManager;

public class CoreApplication
{
  public static final int MSG_START_LOGIN_ACTIVITY = 257;
  private static CoreApplication g_instance = null;
  private Application mApplication = null;
  //private LoginManager mLoginManager = null;
  private String mVersion = "";

  public CoreApplication(Application paramApplication)
  {
    this.mApplication = paramApplication;
  }

  private void GetVersion()
  {
    try
    {
      String str = this.mApplication.getPackageName();
      this.mVersion = this.mApplication.getPackageManager().getPackageInfo(str, 0).versionName;
    }
    catch (Exception localException)
    {
    }
  }

  public static CoreApplication getInstance()
  {
    return g_instance;
  }

  public static void makeInstance(Application paramApplication)
  {
    if (g_instance == null)
      g_instance = new CoreApplication(paramApplication);
  }

//  public LoginManager GetLoginManager()
//  {
//    return this.mLoginManager;
//  }

//  public void Logout()
//  {
//    this.mLoginManager.MenuLogout();
//  }

//  public void SendPtTokenRequest()
//  {
//    this.mLoginManager.asynSendPtTokenRequest();
//  }

  public Application getApplication()
  {
    return this.mApplication;
  }

//  public void newLoginManager()
//  {
//    if (this.mLoginManager == null)
//      this.mLoginManager = new LoginManager(this.mApplication);
//  }

  public String version()
  {
    if (TextUtils.isEmpty(this.mVersion))
      GetVersion();
    return this.mVersion;
  }
}

