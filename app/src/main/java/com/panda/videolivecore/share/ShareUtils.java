package com.panda.videolivecore.share;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import com.panda.videolivecore.CoreApplication;
import com.panda.videoliveplatform.R;
import com.panda.videolivecore.utils.LogUtils;
//import com.sina.weibo.sdk.api.ImageObject;
//import com.sina.weibo.sdk.api.TextObject;
//import com.sina.weibo.sdk.api.WeiboMultiMessage;
//import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
//import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
//import com.sina.weibo.sdk.api.share.WeiboShareSDK;
//import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.PluginIntent;
//import com.tencent.tauth.IUiListener;
//import com.tencent.tauth.Tencent;

public class ShareUtils {
    public static final String SHARE_ICON_URL = "http://i1.pdim.gs/t0163f574c81abf0473.png";
    public static final String SHARE_URL = "http://a.app.qq.com/o/simple.jsp?pkgname=com.panda.videoliveplatform";
    private Context mContext;
    private boolean mQQInstalled = false;
    private boolean mWeiboInstalled = false;
    private boolean mWeixinInstalled = false;

    public ShareUtils(Context context) {
        this.mContext = context;
        CheckShareTargetExist();
    }

    private void CheckShareTargetExist() {
//        if (appInstalledOrNot(PluginIntent.APP_PACKAGE_PATTERN)) {
//            this.mWeixinInstalled = true;
//        }
//        if (appInstalledOrNot("com.sina.weibo") || appInstalledOrNot("com.sina.weibog3")) {
//            this.mWeiboInstalled = true;
//        }
//        if (appInstalledOrNot("com.tencent.mobileqq")) {
//            this.mQQInstalled = true;
//        }
    }

    private boolean appInstalledOrNot(String uri) {
        try {
            CoreApplication.getInstance().getApplication().getPackageManager().getPackageInfo(uri, 1);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public boolean getWeixinInstalled() {
        return this.mWeixinInstalled;
    }

    public boolean getQQInstalled() {
        return this.mQQInstalled;
    }

    public boolean getWeiboInstalled() {
        return this.mWeiboInstalled;
    }

    public boolean shareToWeixinFriend(String title, String msg, String url) {
        return ShareToWeixin.share(this.mContext, title, msg, url, ((BitmapDrawable) this.mContext.getResources().getDrawable(R.drawable.share_icon)).getBitmap(), 0);
    }

    public boolean shareToWeixinTimeLine(String title, String msg, String url) {
        return ShareToWeixin.share(this.mContext, title, msg, url, ((BitmapDrawable) this.mContext.getResources().getDrawable(R.drawable.share_icon)).getBitmap(), 1);
    }

//    public void shareToQQ(boolean qqzone, String msg, String url, String thumbnailPath, IUiListener tencentUiListener) {
//        int i = 1;
//        Bundle params = new Bundle();
//        params.putInt("req_type", 1);
//        params.putString("title", this.mContext.getResources().getString(R.string.title_activity_main_fragment));
//        params.putString("summary", msg);
//        if (!TextUtils.isEmpty(url)) {
//            params.putString("targetUrl", url);
//        }
//        if (!TextUtils.isEmpty(thumbnailPath)) {
//            params.putString("imageUrl", thumbnailPath);
//        }
//        String str = "cflag";
//        if (!qqzone) {
//            i = 2;
//        }
//        params.putInt(str, i);
//        Tencent.createInstance("1104829929", this.mContext.getApplicationContext()).shareToQQ((Activity) this.mContext, params, tencentUiListener);
//    }

    public boolean shareToWeibo(String msg, String url, String thumbnailPath) {
        return false;
//        IWeiboShareAPI mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this.mContext, "1728641924");
//        if (mWeiboShareAPI.isWeiboAppInstalled()) {
//            mWeiboShareAPI.registerApp();
//        }
//        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
//        LogUtils.e("share", "content: " + msg);
//        if (!TextUtils.isEmpty(msg)) {
//            TextObject textObject = new TextObject();
//            textObject.text = msg + url;
//            textObject.actionUrl = url;
//            weiboMessage.textObject = textObject;
//        }
//        Bitmap bmp = BitmapFactory.decodeFile(thumbnailPath);
//        if (bmp != null) {
//            ImageObject imageObject = new ImageObject();
//            imageObject.setImageObject(bmp);
//            weiboMessage.imageObject = imageObject;
//        }
//        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
//        request.transaction = String.valueOf(System.currentTimeMillis());
//        request.multiMessage = weiboMessage;
//        return mWeiboShareAPI.sendRequest(request);
    }
}
