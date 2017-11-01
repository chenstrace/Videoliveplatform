package com.panda.videolivecore.view.Danmaku;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.android.volley.DefaultRetryPolicy;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import master.flame.danmaku.controller.DrawHandler.Callback;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.Duration;
//import master.flame.danmaku.danmaku.model.android.DanmakuGlobalConfig;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
//import master.flame.danmaku.danmaku.parser.DanmakuFactory;
//import master.flame.danmaku.danmaku.parser.android.BiliDanmukuParser;

public class DanmakuViewWrap {
    private static int DOUBLE_CLICK_TIME = 300;
    private static final String XML_PARSE_LOAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><i><maxlimit>800</maxlimit></i>";
    private long mCurTime = 0;
    private IDanmakuView mDanmakuView;
    private IDanmakuViewWrapCallback mDanmakuViewWrapCallback = null;
    private float mDanmuFontSize = 20.0f;
    private int mDanmuPos = 0;
    private Duration mDuration = null;
    private long mLastTime = 0;
    private WeakReference<Activity> mParentActivity = null;
    private WeakReference<LinearLayout> mParentLayout = null;
    private BaseDanmakuParser mParser;
    private boolean mbIsVisible = false;

    public void SetDanmakuSpeed(long duration) {
        long mseconds = Math.max(24 - duration, 1) * 500;
        if (this.mDuration == null) {
            this.mDuration = new Duration(mseconds);
        } else {
            this.mDuration.setValue(mseconds);
        }
    }

    public static InputStream StringTOInputStream(String in) throws Exception {
        return new ByteArrayInputStream(in.getBytes("UTF-8"));
    }

//    public void InitView(Activity activity, LinearLayout parentLayout, int viewid, IDanmakuViewWrapCallback callback) {
//        this.mDanmakuViewWrapCallback = callback;
//        this.mParentActivity = new WeakReference(activity);
//        this.mParentLayout = new WeakReference(parentLayout);
//        this.mDuration = new Duration(6000);
//        if (this.mParentActivity.get() != null) {
//            this.mDanmakuView = (IDanmakuView) ((Activity) this.mParentActivity.get()).findViewById(viewid);
//            DanmakuGlobalConfig.DEFAULT.setDanmakuStyle(0, 0.0f).setDuplicateMergingEnabled(false).setMaximumVisibleSizeInScreen(100);
//            if (this.mDanmakuView != null) {
//                try {
//                    this.mParser = createParser(StringTOInputStream(XML_PARSE_LOAD));
//                } catch (Exception e) {
//                }
//                this.mDanmakuView.setCallback(new Callback() {
//                    public void updateTimer(DanmakuTimer timer) {
//                    }
//
//                    public void prepared() {
//                        DanmakuViewWrap.this.mDanmakuView.start();
//                    }
//                });
//                if (this.mParser != null) {
//                    this.mDanmakuView.prepare(this.mParser);
//                }
//                this.mDanmakuView.enableDanmakuDrawingCache(true);
//                OnClickListener onClickListener = new OnClickListener() {
//                    public void onClick(View view) {
//                        DanmakuViewWrap.this.mLastTime = DanmakuViewWrap.this.mCurTime;
//                        DanmakuViewWrap.this.mCurTime = System.currentTimeMillis();
//                        if (DanmakuViewWrap.this.mCurTime - DanmakuViewWrap.this.mLastTime < ((long) DanmakuViewWrap.DOUBLE_CLICK_TIME)) {
//                            if (DanmakuViewWrap.this.mDanmakuViewWrapCallback != null) {
//                                DanmakuViewWrap.this.mDanmakuViewWrapCallback.onDoubleClickDanmakuView();
//                            }
//                        } else if (DanmakuViewWrap.this.mDanmakuViewWrapCallback != null) {
//                            DanmakuViewWrap.this.mDanmakuViewWrapCallback.onClickDanmakuView();
//                        }
//                    }
//                };
//                ((View) this.mDanmakuView).setOnClickListener(onClickListener);
//                if (this.mParentLayout.get() != null) {
//                    ((LinearLayout) this.mParentLayout.get()).setOnClickListener(onClickListener);
//                }
//            }
//            Hide();
//        }
//    }

    public void Hide() {
        if (this.mDanmakuView != null) {
            this.mDanmakuView.hide();
            this.mbIsVisible = false;
        }
    }

    public void Show() {
        if (this.mDanmakuView != null) {
            this.mDanmakuView.show();
            this.mbIsVisible = true;
        }
    }

    public void Pause() {
        if (this.mDanmakuView != null && this.mDanmakuView.isPrepared()) {
            this.mDanmakuView.pause();
        }
    }

    public void Resume() {
        if (this.mDanmakuView != null && this.mDanmakuView.isPrepared() && this.mDanmakuView.isPaused()) {
            this.mDanmakuView.resume();
        }
    }

    public void Release() {
        if (this.mDanmakuView != null) {
            this.mDanmakuView.release();
            this.mDanmakuView = null;
        }
    }

//    private BaseDanmakuParser createParser(InputStream stream) {
//        if (stream == null) {
//            return new BaseDanmakuParser() {
//                protected Danmakus parse() {
//                    return new Danmakus();
//                }
//            };
//        }
//        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);
//        try {
//            loader.load(stream);
//        } catch (IllegalDataException e) {
//            e.printStackTrace();
//        }
//        BaseDanmakuParser parser = new BiliDanmukuParser();
//        parser.load(loader.getDataSource());
//        return parser;
//    }

//    public void addDanmaku(String msg, boolean islive) {
//        if (this.mbIsVisible) {
//            BaseDanmaku danmaku = DanmakuFactory.createDanmaku(1);
//            if (danmaku != null && this.mDanmakuView != null) {
//                danmaku.isLive = islive;
//                danmaku.text = msg;
//                danmaku.priority = (byte) 1;
//                danmaku.time = this.mDanmakuView.getCurrentTime();
//                danmaku.textSize = this.mDanmuFontSize * (this.mParser.getDisplayer().getDensity() - 0.6f);
//                danmaku.textColor = -1;
//                if (this.mDuration != null) {
//                    danmaku.setDuration(this.mDuration);
//                }
//                this.mDanmakuView.addDanmaku(danmaku);
//            }
//        }
//    }

    public boolean ChangeVisible() {
        if (this.mDanmakuView == null) {
            return false;
        }
        if (this.mDanmakuView.isShown()) {
            this.mDanmakuView.hide();
            this.mbIsVisible = false;
            return false;
        }
        this.mDanmakuView.show();
        this.mbIsVisible = true;
        return true;
    }

    public boolean IsVisible() {
        return this.mbIsVisible;
    }

    public View getView() {
        if (this.mDanmakuView != null) {
            return this.mDanmakuView.getView();
        }
        return null;
    }

    public void setDanmuPos(int pos) {
        if (pos != this.mDanmuPos) {
            View view = getView();
            if (view != null && this.mParentActivity.get() != null) {
                DisplayMetrics displayMetrics = ((Activity) this.mParentActivity.get()).getResources().getDisplayMetrics();
                int pxd = displayMetrics.widthPixels < displayMetrics.heightPixels ? displayMetrics.widthPixels : displayMetrics.heightPixels;
                int pxhalf = new Float(((double) pxd) * 0.4d).intValue();
                LayoutParams param = (LayoutParams) view.getLayoutParams();
                switch (pos) {
                    case 0:
                        param.setMargins(0, 0, 0, 0);
                        view.setLayoutParams(param);
                        break;
                    case 1:
                        param.setMargins(0, 0, 0, pxhalf);
                        view.setLayoutParams(param);
                        break;
                    case 2:
                        param.setMargins(0, pxd - pxhalf, 0, 0);
                        view.setLayoutParams(param);
                        break;
                    default:
                        return;
                }
                this.mDanmuPos = pos;
            }
        }
    }

    public void setDanmuAlpha(int progress) {
        float alpha = ((255.0f - (((float) progress) * 10.0f)) + 55.0f) / 255.0f;
        if (alpha < 0.2f) {
            alpha = 0.2f;
        }
        if (alpha > DefaultRetryPolicy.DEFAULT_BACKOFF_MULT) {
            alpha = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        }
        if (this.mParentLayout.get() != null) {
            ((LinearLayout) this.mParentLayout.get()).setAlpha(alpha);
        }
    }

    public void setDanmuFontSize(int progress) {
        float fontSize = (float) (progress + 12);
        if (fontSize < 12.0f) {
            fontSize = 12.0f;
        }
        if (fontSize > 48.0f) {
            fontSize = 48.0f;
        }
        this.mDanmuFontSize = fontSize;
    }
}
