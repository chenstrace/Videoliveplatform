package com.panda.videoliveplatform.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings.System;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.JsonReader;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.panda.videolivecore.CommonDefine;
import com.panda.videolivecore.data.EnterRoomState;
import com.panda.videolivecore.data.LiveRoomStartupInfo;
import com.panda.videolivecore.net.LiveRoomRequest;
import com.panda.videolivecore.net.NumericUtils;
import com.panda.videolivecore.net.RtmpDispatchRequest;
import com.panda.videolivecore.net.UrlConst;
import com.panda.videolivecore.net.http.IHttpRequestEvent;
import com.panda.videolivecore.net.info.BambooList;
import com.panda.videolivecore.net.info.ChatInfo;
import com.panda.videolivecore.net.info.EnterRoomInfo;
import com.panda.videolivecore.net.info.ResultMsgInfo;
import com.panda.videolivecore.net.info.RoomInfo;
import com.panda.videolivecore.net.info.RtmpDispatchInfo;
import com.panda.videolivecore.net.info.VerifyCodeInfo;
import com.panda.videolivecore.net.info.VideoInfo;
import com.panda.videolivecore.setting.SettingStorage;
//import com.panda.videolivecore.share.ShareDialog;
import com.panda.videolivecore.utils.DpUtils;
import com.panda.videolivecore.utils.ImeUtils;
import com.panda.videolivecore.utils.LogUtils;
import com.panda.videolivecore.utils.NetworkUtil;
import com.panda.videolivecore.utils.ToastUtils;
//import com.panda.videolivecore.utils.UmengStatic;
import com.panda.videolivecore.view.Danmaku.DanmakuViewWrap;
import com.panda.videolivecore.view.Danmaku.IDanmakuViewWrapCallback;
import com.panda.videoliveplatform.MyApplication;
import com.panda.videoliveplatform.R;
import com.panda.videoliveplatform.chat.ChatRoomHostInfoView;
import com.panda.videoliveplatform.chat.ChatRoomView;
import com.panda.videoliveplatform.chat.EnterRoomStateEvent;
import com.panda.videoliveplatform.chat.Message.MsgReceiverType;
import com.panda.videoliveplatform.chat.MessageDataInfo;
//import com.sina.weibo.sdk.utils.LogUtil;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
//import kd.push.KDPushManagerX;
//import kd.push.PushCallbackX;
//import kd.push.message.MessageData;
import org.json.JSONObject;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.widget.VideoView;

public class LiveRoomActivity extends FragmentActivity implements IDanmakuViewWrapCallback, IHttpRequestEvent, EnterRoomStateEvent {
    private static final int MSG_RECEIVE_CHATINFO = 272;
    public static final int PLAYING = 1;
    public static final int PLAY_ERROR = 4;
    public static final int PLAY_PAUSE = 2;
    public static final int PLAY_STOP = 3;
    public static final int PLAY_WAIT = 5;
    public static final int PRE_PLAY = 0;
    private static final String TAG = "LiveRoomActivity";
    public static final int UNDEFINE = -1;
    private static boolean mNotifyDailogShow = false;
    private static boolean mNotifyNetworkChange = true;
    private final int PAGE_INDEDX_CHAT = 0;
    private final int PAGE_INDEX_PRESENTER = 1;
    private final String REQUEST_FUNC_ENTERROOM = "EnterRoom";
    private final String REQUEST_FUNC_GETBACKUPCHATINFO = "GetBackupChatInfo";
    private final String REQUEST_FUNC_GETCHATINFO = "GetChatInfo";
    private final String REQUEST_FUNC_GETMYBAMBOOS = "GetMyBamboos";
    private final String REQUEST_FUNC_SENDBAMBOOS = "SendBamboos";
    private final String REQUEST_FUNC_SENDBAMBOOSTASK = "SendAcquireBamboosTask";
    private final String REQUEST_FUNC_SENDBAMBOOSTASKDONE = "SendBamboosTaskDone";
    private final String REQUEST_FUNC_SENDBAMBOOSTASKREWARD = "SendBamboosTaskReward";
    private final String REQUEST_FUNC_SENDGROUPMSG = "SendGroupMsg";
    //private GeetestLib geetestLib = new GeetestLib();
    boolean isLimitShowChatError = false;
    private String mAppId;
    private boolean mBambooAvailable = false;
    private ImageButton mBammbooTask;
    private ImageButton mBtnPlay;
    private Button mBtnSendMsgFS;
    private ImageButton mBtnStop;
    private Button mBtnVideoChaoqing;
    private Button mBtnVideoGaoqing;
    private Button mBtnVideoPuqing;
    private View mBufferingIndicator;
    private long mCacheStartTime = 0;
    private LinearLayout mChatLine;
    private ChatRoomHostInfoView mChatRoomHostInfoView;
    private ChatRoomView mChatRoomView;
    private BambooList mCurrentBambooList = new BambooList();
    private int mCurrentStatus = -1;
    private final DanmakuViewWrap mDanmakuViewWrap = new DanmakuViewWrap();
    private LinearLayout mDanmuFrame;
    private EditText mEditTextMsgFS;
    private boolean mEnterRoomSucess = false;
    private LinearLayout mFullControlBottomBanboo;
    private LinearLayout mFullControlBottomChat;
    private RelativeLayout mFullControlLayout;
    private boolean mFullscreenState = false;
    private boolean mGetMainChatInfo = true;
    private ImageView mImgPlay;
    private long mLastOptionTimeFullScreen = 0;
    private View mLayoutFullControlContainer;
    private View mLayoutFullControlSetting;
    private LinearLayout mLayoutPagerTitle;
    private View mLayoutSelectVideoSource;
    int mLimitShowChatErrorFlag = 0;
    private LinearLayout mLiveStatusLayout;
    private String mLocalVideoSource = RtmpDispatchInfo.STREAM_OD;
    private RelativeLayout mMiniControlLayout;
    private int mOriginalWindowAttrFlag = 0;
    private ViewPager mPager;
    private TextView mPersonNums;
    private RelativeLayout mPlayerLayout;
    private LinearLayout mPresenterLine;
    private BroadcastReceiver mReceiver = null;
    private boolean mRefreshLiveRoom = false;
    private String[] mRoomIp;
    private EnterRoomState mRoomState;
    private View mRootLayout;
    private View mSystemBarLayout;
    private int mSystemUiVisibilityOld;
    private int mTaskDelay = 0;
    private String mTaskID;
    private long mTaskStartTime = 0;
    private TASK_STATE mTaskState = TASK_STATE.PRE_GETTASKLIST;
    private Timer mTimer = new Timer();
    private TextView mTxtChat;
    private TextView mTxtFullScreenTitle;
    private TextView mTxtPresenter;
    private VideoView mVideoView;
    private LinearLayout mVideoViewMask;
    private LinearLayout mViewPageLayout;
    WifiLock mWifilock;
    private Handler m_Handler = null;
    private final RtmpDispatchRequest m_reqDispatch = new RtmpDispatchRequest(this);
    private final LiveRoomRequest m_request = new LiveRoomRequest(this);
    private LiveRoomStartupInfo m_startupInfo;
    private boolean mbIsInputFullScreen = false;
    private int oriBright = -1;
    private int oriVolume = 0;

    class FullScreenControlOnClickListener implements OnClickListener {
        FullScreenControlOnClickListener() {
        }

        public void onClick(View v) {
            if (LiveRoomActivity.this.IsFullControlContainerVisible()) {
                LiveRoomActivity.this.HideFullControlContainer();
            }
            LiveRoomActivity.this.autoHideFullScreenControl();
        }
    }

    class GtAppDlgTask extends AsyncTask<Void, Void, Boolean> {
        GtAppDlgTask() {
        }

        protected Boolean doInBackground(Void... params) {
//            return Boolean.valueOf(LiveRoomActivity.this.geetestLib.startCaptcha());
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if (result.booleanValue()) {
                //LiveRoomActivity.this.openGtTest(LiveRoomActivity.this, LiveRoomActivity.this.geetestLib.getCaptcha(), LiveRoomActivity.this.geetestLib.getChallenge(), true);
            } else {
                ToastUtils.show(LiveRoomActivity.this, LiveRoomActivity.this.getString(R.string.fail_for_network_error));
            }
        }
    }

    private enum TASK_STATE {
        PRE_GETTASKLIST,
        STARTING_TASK,
        PRE_TASKDONE,
        PRE_TASKDONE_VERIFY,
        STARTING_VERIFY,
        PRE_GETREWARD,
        TASK_OVER
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        setContentView(R.layout.activity_live_room);
        this.mRootLayout = findViewById(R.id.root);
        this.mVideoView = (VideoView) findViewById(R.id.surface_view);
        this.mVideoView.setActualVideoHeight((int) getResources().getDimension(R.dimen.liveroom_miniplayer_size));
        this.mVideoViewMask = (LinearLayout) findViewById(R.id.player_background_mask);
        this.mLiveStatusLayout = (LinearLayout) findViewById(R.id.live_status_layout);
        this.mMiniControlLayout = (RelativeLayout) findViewById(R.id.mini_control_layout);
        this.mFullControlLayout = (RelativeLayout) findViewById(R.id.full_control_layout);
        this.mFullControlBottomChat = (LinearLayout) findViewById(R.id.layout_full_contol_bar_bottom_chat);
        this.mFullControlBottomBanboo = (LinearLayout) findViewById(R.id.layout_full_contol_bar_bottom_bamboo);
        this.mViewPageLayout = (LinearLayout) findViewById(R.id.viewpage_layout);
        this.mPlayerLayout = (RelativeLayout) findViewById(R.id.player_layout);
        this.mPager = (ViewPager) findViewById(R.id.vpChart);
        this.mLayoutPagerTitle = (LinearLayout) findViewById(R.id.layout_viewpager_title);
        this.mTxtChat = (TextView) findViewById(R.id.liveroom_chat_btn);
        this.mTxtPresenter = (TextView) findViewById(R.id.liveroom_presenter_btn);
        this.mChatLine = (LinearLayout) findViewById(R.id.liveroom_chat_line);
        this.mPresenterLine = (LinearLayout) findViewById(R.id.liveroom_presenter_line);
        this.mWifilock = ((WifiManager) getSystemService("wifi")).createWifiLock("WifiLock_For_LiveRoom");
        this.mWifilock.acquire();
        initIntent();
        initVideoView();
        initViewPage();
        initDanmakuView();
        initMiniControl();
        initFullScreenControl();
        refreshLiveRoom();
        initLiveStatus();
        initBroadcastReceiver();
        initHandle();
    }

    private void initBroadcastReceiver() {
        this.mReceiver = new BroadcastReceiver() {
            private static final String TAG = "BroadcastReceiver";

            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                    LogUtils.d(TAG, "network state change");
                    NetworkInfo info = ((ConnectivityManager) LiveRoomActivity.this.getSystemService("connectivity")).getActiveNetworkInfo();
                    if (info == null || !info.isAvailable()) {
                        LogUtils.d(TAG, "no network");
                    } else {
                        LiveRoomActivity.this.onNetworkChange(info);
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(this.mReceiver, filter);
    }

    protected void initHandle() {
        this.m_Handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 272:
                        try {
                            MessageDataInfo info =(MessageDataInfo) msg.obj;
                            if (info != null) {
                                LiveRoomActivity.this.onDispatchReceiveChatInfo(info);
                                return;
                            }
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    default:
                        super.handleMessage(msg);
                        return;
                }
            }
        };
    }

    private boolean initIntent() {
        this.m_startupInfo = new LiveRoomStartupInfo(getIntent().getStringExtra("idRoom"), getIntent().getStringExtra("urlRoom"), getIntent().getStringExtra("urlImage"), getIntent().getStringExtra("addrStream"));
        this.mRoomState = new EnterRoomState(this.m_startupInfo.idRoom);
        return true;
    }

    private boolean initVideoView() {
        this.mBufferingIndicator = findViewById(R.id.buffering_indicator);
        this.mVideoView.setMediaBufferingIndicator(this.mBufferingIndicator);
        this.mVideoView.requestFocus();
        this.mVideoView.setOnErrorListener(new OnErrorListener() {
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                LiveRoomActivity.this.liveStatusFailure();
                LiveRoomActivity.this.mBtnStop.setVisibility(8);
                LiveRoomActivity.this.mBtnPlay.setVisibility(0);
                LiveRoomActivity.this.mBufferingIndicator.setVisibility(8);
                LiveRoomActivity.this.mCurrentStatus = 4;
                LiveRoomActivity.this.detectStartPlaying();
                UmengStatic.StaticVideoBreak(LiveRoomActivity.this);
                return false;
            }
        });
        this.mVideoView.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(IMediaPlayer mp) {
                LiveRoomActivity.this.stopPlay();
                LiveRoomActivity.this.liveStatusFailure();
                LiveRoomActivity.this.mCurrentStatus = 4;
                LiveRoomActivity.this.detectStartPlaying();
                LiveRoomActivity.this.startPlayServer();
            }
        });
        this.mVideoView.setOnInfoListener(new OnInfoListener() {
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    if (LiveRoomActivity.this.mBufferingIndicator != null) {
                        LiveRoomActivity.this.mBufferingIndicator.setVisibility(0);
                    }
                    LiveRoomActivity.this.mCacheStartTime = System.currentTimeMillis();
                } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
                    if (LiveRoomActivity.this.mBufferingIndicator != null) {
                        LiveRoomActivity.this.mBufferingIndicator.setVisibility(8);
                    }
                }
                return false;
            }
        });
        setVideoViewEvent();
        startPlayServer();
        return true;
    }

    private void RestartVideoView(String strAddr) {
        this.m_startupInfo.addrStream = strAddr;
        if (!TextUtils.isEmpty(this.m_startupInfo.addrStream)) {
            try {
                if (NetworkUtil.CheckWifiNetworkState(this)) {
                    liveStatusHide();
                    this.mVideoView.setVideoPath(this.m_startupInfo.addrStream);
                    sendAcqureBambooTask();
                } else if (!mNotifyNetworkChange) {
                    this.mVideoView.setVideoPath(this.m_startupInfo.addrStream);
                    sendAcqureBambooTask();
                } else if (!mNotifyDailogShow) {
                    final AlertDialog dlg = new AlertDialog(this, getResources().getString(R.string.network_change_message_text), getResources().getString(R.string.button_continue), getResources().getString(R.string.button_cancel), DEFAULT_BTN.DEFAULT_YES);
                    dlg.setOnDismissListener(new OnDismissListener() {
                        public void onDismiss(DialogInterface dialog) {
                            switch (dlg.GetClickType()) {
                                case R.id.button_cancel:
                                    LiveRoomActivity.this.finish();
                                    break;
                                case R.id.button_continue:
                                    LiveRoomActivity.mNotifyNetworkChange = false;
                                    LiveRoomActivity.this.mVideoView.setVideoPath(LiveRoomActivity.this.m_startupInfo.addrStream);
                                    LiveRoomActivity.this.sendAcqureBambooTask();
                                    break;
                            }
                            LiveRoomActivity.mNotifyDailogShow = false;
                        }
                    });
                    dlg.show();
                    mNotifyDailogShow = true;
                }
            } catch (Exception e) {
                //LogUtil.e(TAG, e.toString());
            }
        }
    }

    private int getCurrentScreenLight() {
        int bright = (int) (getWindow().getAttributes().screenBrightness * 255.0f);
        if (bright < 0) {
            return System.getInt(getContentResolver(), "screen_brightness", 255);
        }
        return bright;
    }

    private void setScreenLight(int bright) {
        LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = ((float) bright) / 255.0f;
        getWindow().setAttributes(lp);
    }

    private void setVideoViewEvent() {
        final AudioManager mAudioManager = (AudioManager) getSystemService("audio");
        final int maxVolume = mAudioManager.getStreamMaxVolume(3);
        this.mVideoView.setOnTouchListener(new OnTouchListener() {
            private final int MOVE_DELTA = 2;
            private boolean mTouchStop = false;
            private float mTouchX;
            private float mTouchY;
            private VideoInfoWindow mWindow = null;

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    this.mTouchStop = false;
                    this.mTouchX = event.getX();
                    this.mTouchY = event.getY();
                    LiveRoomActivity.this.oriVolume = mAudioManager.getStreamVolume(3);
                    LiveRoomActivity.this.oriBright = LiveRoomActivity.this.getCurrentScreenLight();
                    return true;
                } else if (event.getAction() == 1) {
                    this.mTouchStop = true;
                    if (this.mWindow != null) {
                        this.mWindow.CloseWindow();
                        this.mWindow = null;
                    }
                    if (Math.abs(event.getX() - this.mTouchX) >= 2.0f || Math.abs(event.getY() - this.mTouchY) >= 2.0f) {
                        return true;
                    }
                    LiveRoomActivity.this.mChatRoomView.HideInputBox();
                    return true;
                } else if (event.getAction() != 2) {
                    return false;
                } else {
                    if (Math.abs(event.getY() - this.mTouchY) < 2.0f) {
                        return true;
                    }
                    int canvas_width = LiveRoomActivity.this.mVideoView.getWidth();
                    int canvas_height = LiveRoomActivity.this.mVideoView.getHeight();
                    String percent;
                    if (event.getX() < ((float) (canvas_width / 5))) {
                        int newBright = Math.max(Math.min(255, LiveRoomActivity.this.oriBright - ((int) ((event.getY() - this.mTouchY) / ((((float) canvas_height) / 2.0f) / 255.0f)))), 0);
                        if (newBright != LiveRoomActivity.this.oriBright) {
                            LiveRoomActivity.this.setScreenLight(newBright);
                        }
                        percent = ((newBright * 100) / 255) + "%";
                        if (this.mWindow == null) {
                            this.mWindow = new VideoInfoWindow(LiveRoomActivity.this, LiveRoomActivity.this.mVideoView, R.drawable.brightness, percent);
                            this.mWindow.Show();
                            return true;
                        }
                        this.mWindow.SetText(percent);
                        return true;
                    } else if (event.getX() <= ((float) ((canvas_width * 4) / 5))) {
                        return true;
                    } else {
                        int newVolume = Math.max(Math.min(maxVolume, LiveRoomActivity.this.oriVolume - ((int) ((event.getY() - this.mTouchY) / ((((float) canvas_height) / 2.0f) / ((float) maxVolume))))), 0);
                        if (newVolume != LiveRoomActivity.this.oriVolume) {
                            mAudioManager.setStreamVolume(3, newVolume, 0);
                        }
                        percent = ((newVolume * 100) / maxVolume) + "%";
                        if (this.mWindow == null) {
                            this.mWindow = new VideoInfoWindow(LiveRoomActivity.this, LiveRoomActivity.this.mVideoView, R.drawable.vol, percent);
                            this.mWindow.Show();
                            return true;
                        }
                        this.mWindow.SetText(percent);
                        return true;
                    }
                }
            }
        });
    }

    protected void onPause() {
        super.onPause();

        if (this.mCurrentStatus == 0 || this.mCurrentStatus == 1) {
            this.mCurrentStatus = 2;
            stopPlay();
        }
        this.isLimitShowChatError = true;
        this.mLimitShowChatErrorFlag = 0;
    }

    protected void onResume() {
        super.onResume();
        if (this.mCurrentStatus == 2) {
            restartPlay();
        }
        //KDPushManagerX.getInstance().checkConnection();
        this.isLimitShowChatError = false;
        this.mLimitShowChatErrorFlag = 0;
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mReceiver != null) {
            unregisterReceiver(this.mReceiver);
        }
        this.mDanmakuViewWrap.Release();
        //KDPushManagerX.getInstance().stop();
        this.mCurrentStatus = -1;
        this.mWifilock.release();
    }

    public void onBackPressed() {
        if (this.mFullscreenState) {
            changFullscreen();
        } else {
            finish();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (this.mVideoView != null) {
            this.mVideoView.setVideoLayout(1);
        }
        super.onConfigurationChanged(newConfig);
    }

    public void onClickDanmakuView() {
        if (!this.mFullscreenState) {
            this.mChatRoomView.HideInputBox();
            switchMiniControl();
        } else if (IsFullControlContainerVisible()) {
            HideFullControlContainer();
        } else {
            int nVisible = this.mFullControlLayout.getVisibility();
            if (nVisible == 8) {
                showFullScreenControl();
                this.mFullControlBottomChat.setVisibility(0);
                this.mFullControlBottomBanboo.setVisibility(8);
                setFullScreenState(false);
                autoHideFullScreenControl();
            } else if (nVisible == 0) {
                hideFullScreenControl();
                setFullScreenState(true);
            }
            ImeUtils.hideSoftInputBox(this);
        }
    }

    private void onNetworkChange(NetworkInfo ni) {
        if (ni.getState() != State.CONNECTED) {
            return;
        }
        if (this.mEnterRoomSucess) {
            restartPlay();
            detectStartPlaying();
            return;
        }
        refreshLiveRoom();
    }

    public void onDoubleClickDanmakuView() {
        changFullscreen();
    }

    private boolean changeDanmakuViewVisible(boolean bShow) {
        if (bShow) {
            this.mDanmakuViewWrap.Show();
        } else {
            this.mDanmakuViewWrap.Hide();
        }
        return bShow;
    }

    protected void initViewPage() {
        LayoutInflater inflater = getLayoutInflater();
        this.mChatRoomView = new ChatRoomView(this, this.mRoomState);
        this.mChatRoomHostInfoView = new ChatRoomHostInfoView(this, this.mRoomState, this);
        List<View> views = new ArrayList();
        views.add(this.mChatRoomView);
        views.add(this.mChatRoomHostInfoView);
        this.mPager.setAdapter(new LiveRoomChartAdapter(views));
        this.mPager.setOnPageChangeListener(new OnPageChangeListener() {
            private int m_nCurrentFragment = -1;

            public void onPageSelected(int i) {
                this.m_nCurrentFragment = i;
                changePage(i);
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }

            private void changePage(int nIndex) {
                ViewGroup.LayoutParams linearParams;
                switch (nIndex) {
                    case 0:
                        LiveRoomActivity.this.mTxtChat.setBackgroundColor(LiveRoomActivity.this.getResources().getColor(R.color.liveroom_text_background));
                        LiveRoomActivity.this.mTxtChat.setTextColor(LiveRoomActivity.this.getResources().getColor(R.color.liveroom_text_color));
                        LiveRoomActivity.this.mTxtPresenter.setBackgroundColor(LiveRoomActivity.this.getResources().getColor(R.color.liveroom_text_background_disable));
                        LiveRoomActivity.this.mTxtPresenter.setTextColor(LiveRoomActivity.this.getResources().getColor(R.color.liveroom_text_color_disable));
                        LiveRoomActivity.this.mChatLine.setBackgroundColor(LiveRoomActivity.this.getResources().getColor(R.color.title_color));
                        linearParams = LiveRoomActivity.this.mChatLine.getLayoutParams();
                        linearParams.height = (int) LiveRoomActivity.this.getResources().getDimension(R.dimen.liveroom_underline_size);
                        LiveRoomActivity.this.mChatLine.setLayoutParams(linearParams);
                        LiveRoomActivity.this.mPresenterLine.setBackgroundColor(LiveRoomActivity.this.getResources().getColor(R.color.liveroom_underline_color_disable));
                        linearParams = LiveRoomActivity.this.mPresenterLine.getLayoutParams();
                        linearParams.height = (int) LiveRoomActivity.this.getResources().getDimension(R.dimen.liveroom_underline_size_disable);
                        LiveRoomActivity.this.mPresenterLine.setLayoutParams(linearParams);
                        break;
                    case 1:
                        LiveRoomActivity.this.mTxtChat.setBackgroundColor(LiveRoomActivity.this.getResources().getColor(R.color.liveroom_text_background_disable));
                        LiveRoomActivity.this.mTxtChat.setTextColor(LiveRoomActivity.this.getResources().getColor(R.color.liveroom_text_color_disable));
                        LiveRoomActivity.this.mTxtPresenter.setBackgroundColor(LiveRoomActivity.this.getResources().getColor(R.color.liveroom_text_background));
                        LiveRoomActivity.this.mTxtPresenter.setTextColor(LiveRoomActivity.this.getResources().getColor(R.color.liveroom_text_color));
                        LiveRoomActivity.this.mChatLine.setBackgroundColor(LiveRoomActivity.this.getResources().getColor(R.color.liveroom_underline_color_disable));
                        linearParams = LiveRoomActivity.this.mChatLine.getLayoutParams();
                        linearParams.height = (int) LiveRoomActivity.this.getResources().getDimension(R.dimen.liveroom_underline_size_disable);
                        LiveRoomActivity.this.mChatLine.setLayoutParams(linearParams);
                        LiveRoomActivity.this.mPresenterLine.setBackgroundColor(LiveRoomActivity.this.getResources().getColor(R.color.title_color));
                        linearParams = LiveRoomActivity.this.mPresenterLine.getLayoutParams();
                        linearParams.height = (int) LiveRoomActivity.this.getResources().getDimension(R.dimen.liveroom_underline_size);
                        LiveRoomActivity.this.mPresenterLine.setLayoutParams(linearParams);
                        break;
                }
                LiveRoomActivity.this.mLayoutPagerTitle.requestLayout();
            }
        });
        this.mPager.setCurrentItem(0);
    }

    private void showFullScreenControl() {
        this.mFullControlLayout.setVisibility(0);
        this.mSystemBarLayout.setVisibility(0);
    }

    private void hideFullScreenControl() {
        this.mFullControlLayout.setVisibility(8);
        this.mSystemBarLayout.setVisibility(8);
    }

    private void autoHideFullScreenControl() {
        Handler handle = new Handler(Looper.getMainLooper());
        Runnable check_runable = new Runnable() {
            public void run() {
                if (java.lang.System.currentTimeMillis() - LiveRoomActivity.this.mLastOptionTimeFullScreen >= 5000 && LiveRoomActivity.this.mFullControlLayout.getVisibility() == 0 && !LiveRoomActivity.this.mbIsInputFullScreen) {
                    if (LiveRoomActivity.this.IsFullControlContainerVisible()) {
                        LiveRoomActivity.this.HideFullControlContainer();
                    }
                    if (LiveRoomActivity.this.mFullControlLayout.getVisibility() == 0) {
                        LiveRoomActivity.this.hideFullScreenControl();
                        LiveRoomActivity.this.setFullScreenState(true);
                    }
                    ImeUtils.hideSoftInputBox(LiveRoomActivity.this);
                }
            }
        };
        this.mLastOptionTimeFullScreen = java.lang.System.currentTimeMillis();
        handle.postDelayed(check_runable, 5000);
    }

    private void changFullscreen() {
        ViewGroup.LayoutParams layoutParams;
        if (this.mFullscreenState) {
            this.mChatRoomView.setVisibility(0);
            setRequestedOrientation(1);
            this.mFullscreenState = false;
            this.mDanmakuViewWrap.Hide();
            hideFullScreenControl();
            this.mViewPageLayout.setVisibility(0);
            layoutParams = this.mPlayerLayout.getLayoutParams();
            layoutParams.height = (int) getResources().getDimension(R.dimen.liveroom_miniplayer_size);
            this.mPlayerLayout.setLayoutParams(layoutParams);
            setFullScreenState(false);
            leaveFullScreenState();
            getWindow().getDecorView().setSystemUiVisibility(this.mSystemUiVisibilityOld);
            this.mDanmakuViewWrap.setDanmuPos(0);
        } else {
            this.mChatRoomView.setVisibility(8);
            setRequestedOrientation(6);
            this.mFullscreenState = true;
            if (SettingStorage.IsDanmuOpen()) {
                this.mDanmakuViewWrap.Show();
            }
            showMiniControl(false);
            showFullScreenControl();
            this.mFullControlBottomChat.setVisibility(0);
            this.mFullControlBottomBanboo.setVisibility(8);
            this.mViewPageLayout.setVisibility(8);
            layoutParams = this.mPlayerLayout.getLayoutParams();
            layoutParams.height = -1;
            this.mPlayerLayout.setLayoutParams(layoutParams);
            HideFullControlContainer();
            enterFullScreenState();
            setFullScreenState(false);
            this.mDanmakuViewWrap.setDanmuPos(SettingStorage.GetDanmuPos());
            autoHideFullScreenControl();
        }
        this.mRootLayout.requestLayout();
    }

    private void setFullScreenState(boolean bFullScreen) {
        LayoutParams attr;
        if (bFullScreen) {
            attr = getWindow().getAttributes();
            attr.flags |= 1024;
            getWindow().setAttributes(attr);
            getWindow().addFlags(512);
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | 5894);
        } else {
            attr = getWindow().getAttributes();
            attr.flags &= -1025;
            getWindow().setAttributes(attr);
            getWindow().clearFlags(512);
            getWindow().getDecorView().setSystemUiVisibility(1792);
        }
        if (VERSION.SDK_INT >= 19) {
            Window win = getWindow();
            LayoutParams winParams = win.getAttributes();
            winParams.flags |= 67108864;
            win.setAttributes(winParams);
        }
    }

    private void enterFullScreenState() {
        LayoutParams attr = getWindow().getAttributes();
        this.mOriginalWindowAttrFlag = attr.flags;
        attr.flags |= 1024;
        getWindow().setAttributes(attr);
    }

    private void leaveFullScreenState() {
        LayoutParams attr = getWindow().getAttributes();
        attr.flags = this.mOriginalWindowAttrFlag;
        getWindow().setAttributes(attr);
    }

    private void initDanmakuView() {
//        this.mDanmuFrame = (LinearLayout) findViewById(R.id.sv_danmaku_frame);
//        this.mDanmakuViewWrap.InitView(this, this.mDanmuFrame, R.id.sv_danmaku, this);
//        if (!SettingStorage.IsDanmuOpen()) {
//            this.mDanmakuViewWrap.Hide();
//        }
    }

    public void onClickBack(View view) {
        finish();
    }

    public void onClickShare(View view) {
//        String name = this.mRoomState.mInfoExtend.roomInfo.name;
//        String id = this.mRoomState.mInfoExtend.roomInfo.id;
//        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(id)) {
//            ShareDialog shareDialog = new ShareDialog(this, R.style.simple_bubble_message_dialog);
//            shareDialog.setRoomName(name);
//            shareDialog.setRoomUrl(UrlConst.getRoomUrl(id));
//            Window win = shareDialog.getWindow();
//            win.setGravity(51);
//            LayoutParams params = new LayoutParams();
//            params.copyFrom(win.getAttributes());
//            Rect frame = new Rect();
//            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//            params.width = frame.width();
//            params.height = frame.height();
//            win.setAttributes(params);
//            win.setWindowAnimations(0);
//            shareDialog.show();
//        }
    }

    public void onClickFullscreen(View view) {
        changFullscreen();
    }

    public void onClickPlay(View view) {
        showMiniControl(false);
        restartPlay();
    }

    private void stopPlay() {
        this.mVideoView.stopPlayback();
        this.mVideoView.setUriNull();
        this.mBtnStop.setVisibility(8);
        this.mBtnPlay.setVisibility(0);
    }

    private void restartPlay() {
        if (this.mVideoView != null) {
            stopPlay();
            if (this.mRoomState.mInfoExtend.videoInfo.IsIniting()) {
                liveStatusWaiting();
                return;
            }
            startPlayServer();
            initLiveStatus();
            if (this.mFullscreenState && SettingStorage.IsDanmuOpen()) {
                this.mDanmakuViewWrap.Show();
            }
        }
    }

    private void startPlayServer() {
        RestartVideoView(this.m_startupInfo.addrStream);
    }

    private void initMiniControl() {
        this.mPersonNums = (TextView) findViewById(R.id.txt_personnum);
        this.mImgPlay = (ImageView) findViewById(R.id.img_play);
    }

    private void showToast(int resId) {
        Toast.makeText(this, resId, 0).show();
    }

    private void sendMessageFS() {
        if ("".equals(this.mEditTextMsgFS.getText().toString().trim())) {
            Toast.makeText(this, R.string.chat_send_empty_message, 0).show();
        } else if (!MyApplication.getInstance().GetLoginManager().IsLogin()) {
            showToast(R.string.live_notify_chat_login);
        } else if (this.mChatRoomView.mOnOperationListener != null) {
            this.mChatRoomView.mOnOperationListener.send(this.mEditTextMsgFS.getText().toString().trim());
            this.mEditTextMsgFS.setText("");
            ImeUtils.hideSoftInputBox(this);
            autoHideFullScreenControl();
        }
    }

    private void ShowVideoSourceSelect() {
        this.mLayoutFullControlContainer.setVisibility(0);
        this.mLayoutFullControlSetting.setVisibility(8);
        this.mLayoutSelectVideoSource.setVisibility(0);
    }

    private boolean IsVideoSourceSelectVisible() {
        return this.mLayoutFullControlContainer.getVisibility() == 0 && this.mLayoutSelectVideoSource.getVisibility() == 0;
    }

    private void ShowFullControlSetting() {
        this.mLayoutFullControlContainer.setVisibility(0);
        this.mLayoutFullControlSetting.setVisibility(0);
        this.mLayoutSelectVideoSource.setVisibility(8);
    }

    private boolean IsFullControlSettingVisible() {
        return this.mLayoutFullControlContainer.getVisibility() == 0 && this.mLayoutFullControlSetting.getVisibility() == 0;
    }

    private void HideFullControlContainer() {
        this.mLayoutFullControlContainer.setVisibility(8);
        this.mLayoutSelectVideoSource.setVisibility(8);
        this.mLayoutFullControlSetting.setVisibility(8);
    }

    private boolean IsFullControlContainerVisible() {
        return this.mLayoutFullControlContainer.getVisibility() == 0 || this.mLayoutSelectVideoSource.getVisibility() == 0 || this.mLayoutFullControlSetting.getVisibility() == 0;
    }

    private void initFullScreenControl() {
        this.mSystemUiVisibilityOld = getWindow().getDecorView().getSystemUiVisibility();
        this.mSystemBarLayout = new View(this);
        ViewGroup.LayoutParams system_bar_lp = new ViewGroup.LayoutParams(-1, getStatusBarHeight());
        system_bar_lp.height = getStatusBarHeight();
        this.mSystemBarLayout.setLayoutParams(system_bar_lp);
        this.mSystemBarLayout.setBackgroundColor(getResources().getColor(R.color.play_control_bar));
        this.mSystemBarLayout.setVisibility(8);
        ((ViewGroup) getWindow().getDecorView()).addView(this.mSystemBarLayout);
        this.mBtnStop = (ImageButton) findViewById(R.id.button_full_control_stop);
        this.mBtnPlay = (ImageButton) findViewById(R.id.button_full_control_play);
        this.mTxtFullScreenTitle = (TextView) findViewById(R.id.textview_full_control_title);
        this.mLayoutFullControlContainer = findViewById(R.id.layout_full_contol_container);
        this.mLayoutSelectVideoSource = findViewById(R.id.layout_select_video_source);
        this.mLayoutFullControlSetting = findViewById(R.id.layout_full_control_setting);
        this.mBtnStop.setOnClickListener(new FullScreenControlOnClickListener() {
            public void onClick(View v) {
                super.onClick(v);
                LiveRoomActivity.this.stopPlay();
                LiveRoomActivity.this.mCurrentStatus = 3;
            }
        });
        this.mBtnPlay.setOnClickListener(new FullScreenControlOnClickListener() {
            public void onClick(View v) {
                super.onClick(v);
                LiveRoomActivity.this.restartPlay();
            }
        });
        ((ImageButton) findViewById(R.id.button_full_control_refresh)).setOnClickListener(new FullScreenControlOnClickListener() {
            public void onClick(View v) {
                super.onClick(v);
                if (NetworkUtil.isNetConnected(LiveRoomActivity.this.getApplicationContext())) {
                    LiveRoomActivity.this.mBtnPlay.setVisibility(8);
                    LiveRoomActivity.this.mBtnStop.setVisibility(0);
                    LiveRoomActivity.this.mBufferingIndicator.setVisibility(8);
                    LiveRoomActivity.this.mCurrentStatus = 0;
                    LiveRoomActivity.this.liveStatusLoading();
                    LiveRoomActivity.this.refreshLiveRoom();
                    return;
                }
                LiveRoomActivity.this.mCurrentStatus = 4;
                LiveRoomActivity.this.liveStatusFailure();
            }
        });
        this.mEditTextMsgFS = (EditText) findViewById(R.id.textview_full_control_text_msg);
        this.mEditTextMsgFS.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event == null) {
                    LiveRoomActivity.this.sendMessageFS();
                } else if (!event.isShiftPressed()) {
                    LiveRoomActivity.this.sendMessageFS();
                }
                return false;
            }
        });
        this.mEditTextMsgFS.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (LiveRoomActivity.this.IsFullControlContainerVisible()) {
                    LiveRoomActivity.this.HideFullControlContainer();
                }
                LiveRoomActivity.this.mbIsInputFullScreen = true;
                return false;
            }
        });
        this.mBtnSendMsgFS = (Button) findViewById(R.id.button_full_control_danmu_send);
        this.mBtnSendMsgFS.setOnClickListener(new FullScreenControlOnClickListener() {
            public void onClick(View v) {
                super.onClick(v);
                LiveRoomActivity.this.sendMessageFS();
            }
        });
        final Button btnFlag = (Button) findViewById(R.id.button_full_control_danmu_flag);
        if (!SettingStorage.IsDanmuOpen()) {
            btnFlag.setText(R.string.danmu_flag_open);
        }
        btnFlag.setOnClickListener(new FullScreenControlOnClickListener() {
            public void onClick(View v) {
                super.onClick(v);
                if (SettingStorage.IsDanmuOpen()) {
                    LiveRoomActivity.this.changeDanmakuViewVisible(false);
                    btnFlag.setText(R.string.danmu_flag_open);
                    SettingStorage.SetDanmuOpen(false);
                    return;
                }
                LiveRoomActivity.this.changeDanmakuViewVisible(true);
                btnFlag.setText(R.string.danmu_flag_close);
                SettingStorage.SetDanmuOpen(true);
            }
        });
        final TextView bambooNum = (TextView) findViewById(R.id.textview_full_contol_bamboo_num);
        bambooNum.setText(MyApplication.getInstance().GetLoginManager().GetUserInfo().bamboos);
        ((Button) findViewById(R.id.button_full_control_send_bamboo_pre)).setOnClickListener(new FullScreenControlOnClickListener() {
            public void onClick(View v) {
                super.onClick(v);
                if (MyApplication.getInstance().GetLoginManager().IsLogin()) {
                    LiveRoomActivity.this.mFullControlBottomChat.setVisibility(8);
                    LiveRoomActivity.this.mFullControlBottomBanboo.setVisibility(0);
                    bambooNum.setText(MyApplication.getInstance().GetLoginManager().GetUserInfo().bamboos);
                    return;
                }
                LiveRoomActivity.this.showToast(R.string.live_notify_please_login);
            }
        });
        ((Button) findViewById(R.id.button_full_control_send_bamboo)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LiveRoomActivity.this.m_request.sendBamboos("100", String.valueOf(LiveRoomActivity.this.mRoomState.mInfoExtend.hostInfo.rid), LiveRoomActivity.this.mRoomState.mRoomId, "SendBamboos");
            }
        });
        final Button btnSelectVideoSrc = (Button) findViewById(R.id.button_full_control_src_select);
        btnSelectVideoSrc.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (LiveRoomActivity.this.IsVideoSourceSelectVisible()) {
                    LiveRoomActivity.this.HideFullControlContainer();
                } else {
                    LiveRoomActivity.this.HideFullControlContainer();
                    LiveRoomActivity.this.ShowVideoSourceSelect();
                }
                LiveRoomActivity.this.autoHideFullScreenControl();
            }
        });
        this.mBtnVideoChaoqing = (Button) findViewById(R.id.button_chaoqing);
        this.mBtnVideoGaoqing = (Button) findViewById(R.id.button_gaoqing);
        this.mBtnVideoPuqing = (Button) findViewById(R.id.button_puqing);
        if (this.mLocalVideoSource.equalsIgnoreCase(RtmpDispatchInfo.STREAM_SD)) {
            this.mBtnVideoChaoqing.setTextColor(getResources().getColor(R.color.title_color));
        } else if (this.mLocalVideoSource.equalsIgnoreCase(RtmpDispatchInfo.STREAM_HD)) {
            this.mBtnVideoGaoqing.setTextColor(getResources().getColor(R.color.title_color));
        } else {
            this.mBtnVideoPuqing.setTextColor(getResources().getColor(R.color.title_color));
        }
        OnClickListener BtnVideoSourceOnClickListener = new FullScreenControlOnClickListener() {
            public void onClick(View v) {
                super.onClick(v);
                if (v == LiveRoomActivity.this.mBtnVideoChaoqing) {
                    btnSelectVideoSrc.setText(R.string.video_chaoqing);
                    LiveRoomActivity.this.mBtnVideoChaoqing.setTextColor(LiveRoomActivity.this.getResources().getColor(R.color.title_color));
                    LiveRoomActivity.this.mBtnVideoGaoqing.setTextColor(LiveRoomActivity.this.getResources().getColor(R.color.fullscreen_text_normal));
                    LiveRoomActivity.this.mBtnVideoPuqing.setTextColor(LiveRoomActivity.this.getResources().getColor(R.color.fullscreen_text_normal));
                    LiveRoomActivity.this.mLocalVideoSource = RtmpDispatchInfo.STREAM_SD;
                } else if (v == LiveRoomActivity.this.mBtnVideoGaoqing) {
                    btnSelectVideoSrc.setText(R.string.video_gaoqing);
                    LiveRoomActivity.this.mBtnVideoChaoqing.setTextColor(LiveRoomActivity.this.getResources().getColor(R.color.fullscreen_text_normal));
                    LiveRoomActivity.this.mBtnVideoGaoqing.setTextColor(LiveRoomActivity.this.getResources().getColor(R.color.title_color));
                    LiveRoomActivity.this.mBtnVideoPuqing.setTextColor(LiveRoomActivity.this.getResources().getColor(R.color.fullscreen_text_normal));
                    LiveRoomActivity.this.mLocalVideoSource = RtmpDispatchInfo.STREAM_HD;
                } else if (v == LiveRoomActivity.this.mBtnVideoPuqing) {
                    btnSelectVideoSrc.setText(R.string.video_puqing);
                    LiveRoomActivity.this.mBtnVideoChaoqing.setTextColor(LiveRoomActivity.this.getResources().getColor(R.color.fullscreen_text_normal));
                    LiveRoomActivity.this.mBtnVideoGaoqing.setTextColor(LiveRoomActivity.this.getResources().getColor(R.color.fullscreen_text_normal));
                    LiveRoomActivity.this.mBtnVideoPuqing.setTextColor(LiveRoomActivity.this.getResources().getColor(R.color.title_color));
                    LiveRoomActivity.this.mLocalVideoSource = RtmpDispatchInfo.STREAM_OD;
                } else {
                    return;
                }
                LiveRoomActivity.this.switchVideoStream();
            }
        };
        this.mBtnVideoChaoqing.setOnClickListener(BtnVideoSourceOnClickListener);
        this.mBtnVideoGaoqing.setOnClickListener(BtnVideoSourceOnClickListener);
        this.mBtnVideoPuqing.setOnClickListener(BtnVideoSourceOnClickListener);
        ((ImageButton) findViewById(R.id.button_full_control_setting)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (LiveRoomActivity.this.IsFullControlSettingVisible()) {
                    LiveRoomActivity.this.HideFullControlContainer();
                } else {
                    LiveRoomActivity.this.HideFullControlContainer();
                    LiveRoomActivity.this.ShowFullControlSetting();
                }
                LiveRoomActivity.this.autoHideFullScreenControl();
            }
        });
        final ImageButton btnDanmuTop = (ImageButton) findViewById(R.id.button_danmu_pos_top);
        final ImageButton btnDanmuBottom = (ImageButton) findViewById(R.id.button_danmu_pos_bottom);
        final ImageButton btnDanmuFull = (ImageButton) findViewById(R.id.button_danmu_pos_full);
        btnDanmuTop.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LiveRoomActivity.this.mDanmakuViewWrap.setDanmuPos(1);
                SettingStorage.SetDanmuPos(1);
                btnDanmuTop.setActivated(true);
                btnDanmuBottom.setActivated(false);
                btnDanmuFull.setActivated(false);
                LiveRoomActivity.this.autoHideFullScreenControl();
            }
        });
        btnDanmuBottom.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LiveRoomActivity.this.mDanmakuViewWrap.setDanmuPos(2);
                SettingStorage.SetDanmuPos(2);
                btnDanmuTop.setActivated(false);
                btnDanmuBottom.setActivated(true);
                btnDanmuFull.setActivated(false);
                LiveRoomActivity.this.autoHideFullScreenControl();
            }
        });
        btnDanmuFull.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LiveRoomActivity.this.mDanmakuViewWrap.setDanmuPos(0);
                SettingStorage.SetDanmuPos(0);
                btnDanmuTop.setActivated(false);
                btnDanmuBottom.setActivated(false);
                btnDanmuFull.setActivated(true);
                LiveRoomActivity.this.autoHideFullScreenControl();
            }
        });
        int danmuPos = SettingStorage.GetDanmuPos();
        if (danmuPos == 0) {
            btnDanmuFull.setActivated(true);
        } else if (danmuPos == 1) {
            btnDanmuTop.setActivated(true);
        } else if (danmuPos == 2) {
            btnDanmuBottom.setActivated(true);
        }
        SeekBar seekBarDanmuTransparency = (SeekBar) findViewById(R.id.seekbar_danmu_transparency);
        seekBarDanmuTransparency.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LiveRoomActivity.this.mDanmakuViewWrap.setDanmuAlpha(progress);
                SettingStorage.SetDanmuTransparency(progress);
                LiveRoomActivity.this.autoHideFullScreenControl();
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBarDanmuTransparency.setProgress(SettingStorage.GetDanmuTransparency());
        if (this.mDanmakuViewWrap != null) {
            this.mDanmakuViewWrap.setDanmuAlpha(SettingStorage.GetDanmuTransparency());
        }
        SeekBar seekBarDanmuFontSize = (SeekBar) findViewById(R.id.seekbar_danmu_font_size);
        seekBarDanmuFontSize.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LiveRoomActivity.this.mDanmakuViewWrap.setDanmuFontSize(progress);
                SettingStorage.SetDanmuFontSize(progress);
                LiveRoomActivity.this.autoHideFullScreenControl();
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBarDanmuFontSize.setProgress(SettingStorage.GetDanmuFontSize());
        SeekBar seekBarDanmuSpeed = (SeekBar) findViewById(R.id.seekbar_danmu_speed);
        seekBarDanmuSpeed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LiveRoomActivity.this.mDanmakuViewWrap.SetDanmakuSpeed((long) progress);
                SettingStorage.SetDanmuSpeed(progress);
                LiveRoomActivity.this.autoHideFullScreenControl();
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBarDanmuSpeed.setProgress(SettingStorage.GetDanmuSpeed());
        SeekBar seekBarScreenLight = (SeekBar) findViewById(R.id.seekbar_screen_light);
        seekBarScreenLight.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser || SettingStorage.GetScreenLight() != -1) {
                    LiveRoomActivity.this.setScreenLight(progress);
                    SettingStorage.SetScreenLight(progress);
                    LiveRoomActivity.this.autoHideFullScreenControl();
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        int bright = SettingStorage.GetScreenLight();
        if (bright == -1) {
            bright = getCurrentScreenLight();
        }
        seekBarScreenLight.setProgress(bright);
        this.mBammbooTask = (ImageButton) findViewById(R.id.bammboo_task);
        this.mBammbooTask.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (LiveRoomActivity.this.mTaskState == TASK_STATE.TASK_OVER) {
                    ToastUtils.show(LiveRoomActivity.this, LiveRoomActivity.this.getString(R.string.acquire_bamboo_over));
                } else if (LiveRoomActivity.this.mBambooAvailable) {
                    if (LiveRoomActivity.this.mTaskState == TASK_STATE.PRE_TASKDONE || LiveRoomActivity.this.mTaskState == TASK_STATE.PRE_TASKDONE_VERIFY) {
                        LiveRoomActivity.this.m_request.sendBambooTaskDone("SendBamboosTaskDone", LiveRoomActivity.this.mCurrentBambooList.id);
                    } else {
                        LiveRoomActivity.this.openVerifyDialog();
                    }
                } else if (LiveRoomActivity.this.mTaskStartTime > 0 && LiveRoomActivity.this.mTaskDelay > 0) {
                    long needTime = Math.min(Math.max(((long) LiveRoomActivity.this.mTaskDelay) - ((java.lang.System.currentTimeMillis() - LiveRoomActivity.this.mTaskStartTime) / 60000), 1), (long) LiveRoomActivity.this.mTaskDelay);
                    ToastUtils.show(LiveRoomActivity.this, String.format(LiveRoomActivity.this.getString(R.string.acquire_bamboo_time), new Object[]{String.valueOf(needTime)}));
                }
            }
        });
        this.mLastOptionTimeFullScreen = java.lang.System.currentTimeMillis();

    }

    private void refreshLiveRoom() {
        if (!this.mRefreshLiveRoom) {
            this.mRefreshLiveRoom = true;
            this.m_request.sendEnterRoom(this.mRoomState.mRoomId, "EnterRoom");
        }
    }

    private void initLiveStatus() {
        ((Button) this.mLiveStatusLayout.findViewById(R.id.status_start)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (LiveRoomActivity.this.mEnterRoomSucess) {
                    LiveRoomActivity.this.restartPlay();
                } else {
                    LiveRoomActivity.this.refreshLiveRoom();
                }
            }
        });
        if (NetworkUtil.isNetConnected(getApplicationContext())) {
            this.mCurrentStatus = 0;
            liveStatusLoading();
        } else {
            this.mCurrentStatus = 4;
            liveStatusFailure();
        }
        detectStartPlaying();
    }

    private void liveStatusWaiting() {
        this.mVideoViewMask.setVisibility(0);
        this.mLiveStatusLayout.setVisibility(0);
        this.mLiveStatusLayout.findViewById(R.id.status_start).setVisibility(8);
        TextView text = (TextView) this.mLiveStatusLayout.findViewById(R.id.status_text);
        text.setVisibility(0);
        text.setText(getText(R.string.live_status_waiting));
    }

    private void liveStatusLoading() {
        if (this.mBufferingIndicator.getVisibility() != 0) {
            this.mVideoViewMask.setVisibility(0);
            this.mLiveStatusLayout.setVisibility(0);
            this.mLiveStatusLayout.findViewById(R.id.status_start).setVisibility(8);
            TextView text = (TextView) this.mLiveStatusLayout.findViewById(R.id.status_text);
            text.setVisibility(0);
            text.setText(getText(R.string.live_status_loading));
        }
    }

    private void liveStatusFailure() {
        this.mVideoViewMask.setVisibility(0);
        this.mLiveStatusLayout.setVisibility(0);
        this.mLiveStatusLayout.findViewById(R.id.status_start).setVisibility(0);
        TextView text = (TextView) this.mLiveStatusLayout.findViewById(R.id.status_text);
        text.setVisibility(0);
        text.setText(getText(R.string.live_status_failure));
    }

    private void liveStatusEnterRoomFailure() {
        this.mVideoViewMask.setVisibility(0);
        this.mLiveStatusLayout.setVisibility(0);
        this.mLiveStatusLayout.findViewById(R.id.status_start).setVisibility(0);
        TextView text = (TextView) this.mLiveStatusLayout.findViewById(R.id.status_text);
        text.setVisibility(0);
        text.setText(getText(R.string.live_status_info_failure));
    }

    private void liveStatusHide() {
        this.mVideoViewMask.setVisibility(8);
        this.mLiveStatusLayout.setVisibility(8);
    }

    private void updatePersonNumText() {
        this.mPersonNums.setText(NumericUtils.getLivePeople(this.mRoomState.mInfoExtend.roomInfo.getPersonNumText()));
    }

    private void detectStartPlaying() {
        final Handler handle = new Handler(Looper.getMainLooper());
        handle.postDelayed(new Runnable() {
            int tryTime = 300;

            public void run() {
                if (this.tryTime > 0) {
                    if (LiveRoomActivity.this.mVideoView.isPlaying()) {
                        LiveRoomActivity.this.liveStatusHide();
                        LiveRoomActivity.this.mBtnStop.setVisibility(0);
                        LiveRoomActivity.this.mBtnPlay.setVisibility(8);
                        LiveRoomActivity.this.mVideoView.setBackgroundDrawable(null);
                        LiveRoomActivity.this.mCurrentStatus = 1;
                    } else {
                        handle.postDelayed(this, 200);
                    }
                    this.tryTime--;
                }
            }
        }, 200);
    }

    private void onEnterRoom(boolean bResult, ResultMsgInfo info, EnterRoomInfo infoExtend) {
        this.mRoomState.update(bResult, info, infoExtend);
        this.mChatRoomView.updateRoomInfo(this.mRoomState);
        this.mChatRoomHostInfoView.updateRoomInfo(this.mRoomState);
        if (infoExtend.videoInfo.IsIniting()) {
            liveStatusWaiting();
            this.mCurrentStatus = 5;
        } else {
            switchVideoStream();
        }
        updatePersonNumText();
        switchMiniControl();
        this.m_request.sendGetChatInfo(this.mRoomState.mRoomId, "GetChatInfo");
        this.mTxtFullScreenTitle.setText(this.mRoomState.mInfoExtend.roomInfo.name);
    }

    private void onChatInfo(ChatInfo chat_info) {
        this.mRoomState.mInfoExtend.chatInfo = chat_info;
        this.mAppId = chat_info.appid;
        this.mRoomIp = chat_info.getAllAddrString();
        try {
//            KDPushManagerX.getInstance().start(this, String.valueOf(chat_info.rid), this.mAppId, this.mRoomIp, chat_info.ts, chat_info.sign, chat_info.authtype, this);
        } catch (Exception e) {

        }
    }

    private void onBackupChatInfo(ChatInfo chat_info) {
        if (this.mGetMainChatInfo) {
            try {
                //KDPushManagerX.getInstance().AddRoomIp(chat_info.getAllAddrString());
                return;
            } catch (Exception e) {
                //LogUtil.e(TAG, e.toString());
                return;
            }
        }
        try {
            //KDPushManagerX.getInstance().stop();
        } catch (Exception e2) {
            //LogUtil.e(TAG, e2.toString());
        }
        onChatInfo(chat_info);
    }

    private String getAdjustLocalVideoSource() {
        String strLocalVideoSource = "";
        if (RtmpDispatchInfo.STREAM_SD == this.mLocalVideoSource) {
            if (this.mRoomState.mInfoExtend.videoInfo.getStreamStatus(VideoInfo.STREAM_SD)) {
                return RtmpDispatchInfo.STREAM_SD;
            }
            if (this.mRoomState.mInfoExtend.videoInfo.getStreamStatus(VideoInfo.STREAM_HD)) {
                return RtmpDispatchInfo.STREAM_HD;
            }
            if (this.mRoomState.mInfoExtend.videoInfo.getStreamStatus(VideoInfo.STREAM_OD)) {
                return RtmpDispatchInfo.STREAM_OD;
            }
            return "";
        } else if (RtmpDispatchInfo.STREAM_HD == this.mLocalVideoSource) {
            if (this.mRoomState.mInfoExtend.videoInfo.getStreamStatus(VideoInfo.STREAM_HD)) {
                return RtmpDispatchInfo.STREAM_HD;
            }
            if (this.mRoomState.mInfoExtend.videoInfo.getStreamStatus(VideoInfo.STREAM_OD)) {
                return RtmpDispatchInfo.STREAM_OD;
            }
            if (this.mRoomState.mInfoExtend.videoInfo.getStreamStatus(VideoInfo.STREAM_SD)) {
                return RtmpDispatchInfo.STREAM_SD;
            }
            return "";
        } else if (RtmpDispatchInfo.STREAM_OD != this.mLocalVideoSource) {
            return strLocalVideoSource;
        } else {
            if (this.mRoomState.mInfoExtend.videoInfo.getStreamStatus(VideoInfo.STREAM_OD)) {
                return RtmpDispatchInfo.STREAM_OD;
            }
            if (this.mRoomState.mInfoExtend.videoInfo.getStreamStatus(VideoInfo.STREAM_HD)) {
                return RtmpDispatchInfo.STREAM_HD;
            }
            if (this.mRoomState.mInfoExtend.videoInfo.getStreamStatus(VideoInfo.STREAM_SD)) {
                return RtmpDispatchInfo.STREAM_SD;
            }
            return "";
        }
    }

    private void switchVideoStream() {
        String strLocalVideoSource = getAdjustLocalVideoSource();
        if (!strLocalVideoSource.isEmpty()) {
            StringBuffer streamAddress = new StringBuffer();
            if (this.mRoomState.mInfoExtend.videoInfo.getStreamAddress(Rtmp2VideoType(strLocalVideoSource), streamAddress, this.mRoomState.mInfoExtend.videoInfo.sign, this.mRoomState.mInfoExtend.videoInfo.ts)) {
                String str = streamAddress.toString();
                if (str.isEmpty()) {
                    this.m_reqDispatch.sendGetStreamAddr(this, this.mRoomState.mInfoExtend.videoInfo.roomKey, strLocalVideoSource, strLocalVideoSource);
                    return;
                }
                RestartVideoView(str);
                detectStartPlaying();
            }
        }
    }

    private String Rtmp2VideoType(String type) {
        String strReturn = "";
        if (RtmpDispatchInfo.STREAM_HD.compareToIgnoreCase(type) == 0) {
            strReturn = VideoInfo.STREAM_TYPE_HD;
        }
        if (RtmpDispatchInfo.STREAM_OD.compareToIgnoreCase(type) == 0) {
            strReturn = VideoInfo.STREAM_TYPE_OD;
        }
        if (RtmpDispatchInfo.STREAM_SD.compareToIgnoreCase(type) == 0) {
            return VideoInfo.STREAM_TYPE_SD;
        }
        return strReturn;
    }

    private void onRtmpDispatch(Boolean bResult, String strVideoSource, RtmpDispatchInfo info) {
        if (bResult.booleanValue() && !this.mRoomState.mInfoExtend.videoInfo.IsIniting()) {
            RestartVideoView(info.getStreamAddress(this.mRoomState.mInfoExtend.videoInfo.sign, this.mRoomState.mInfoExtend.videoInfo.ts));
            detectStartPlaying();
        }
    }

    public void onClickChat(View view) {
        this.mPager.setCurrentItem(0);
    }

    public void onClickPresenter(View view) {
        this.mPager.setCurrentItem(1);
    }

    private void switchMiniControl() {
        if (this.mMiniControlLayout.getVisibility() == 0) {
            showMiniControl(false);
            return;
        }
        if (!this.mFullscreenState) {
            showMiniControl(true);
        }
        if (2 == this.mCurrentStatus || 3 == this.mCurrentStatus) {
            this.mImgPlay.setVisibility(0);
        } else {
            this.mImgPlay.setVisibility(8);
        }
        autoHideMiniControl();
    }

    private void autoHideMiniControl() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            public void run() {
                if (LiveRoomActivity.this.mMiniControlLayout.getVisibility() == 0) {
                    LiveRoomActivity.this.showMiniControl(false);
                }
            }
        }, 5000);
    }

    private void showMiniControl(boolean bShow) {
        if (bShow) {
            this.mMiniControlLayout.setVisibility(0);
        } else {
            this.mMiniControlLayout.setVisibility(8);
        }
    }

    private void SendBackupChatInfo() {
        this.m_request.sendGetBackupChatInfo(this.mRoomState.mRoomId, "GetBackupChatInfo");
    }

    public boolean onResponse(boolean bResult, String strReponse, String strContext) {
        ResultMsgInfo info;
        if ("EnterRoom" == strContext) {
            if (bResult) {
                info = new ResultMsgInfo();
                EnterRoomInfo infoExtend = new EnterRoomInfo();
                if (LiveRoomRequest.readEnterRoomInfo(strReponse, info, infoExtend)) {
                    onEnterRoom(bResult, info, infoExtend);
                    this.mEnterRoomSucess = true;
                } else {
                    liveStatusEnterRoomFailure();
                    this.mEnterRoomSucess = false;
                }
            } else {
                liveStatusEnterRoomFailure();
                this.mEnterRoomSucess = false;
            }
            this.mRefreshLiveRoom = false;
        } else if ("GetChatInfo" == strContext) {
            info = new ResultMsgInfo();
            ChatInfo chat_info = new ChatInfo();
            if (LiveRoomRequest.readChatInfo(strReponse, info, chat_info)) {
                this.mGetMainChatInfo = true;
                onChatInfo(chat_info);
            } else {
                this.mGetMainChatInfo = false;
                this.mChatRoomView.BroadcastMessage("弹幕服务器连接失败！");
            }
            SendBackupChatInfo();
        } else if ("GetBackupChatInfo" == strContext) {
            info = new ResultMsgInfo();
            ChatInfo chat_info = new ChatInfo();
            if (LiveRoomRequest.readChatInfo(strReponse, info, chat_info)) {
                onBackupChatInfo(chat_info);
            }
        } else if ("SendGroupMsg" == strContext) {
            LiveRoomRequest.readGroupMsg(strReponse, new ResultMsgInfo());
        } else if (RtmpDispatchInfo.STREAM_HD == strContext || RtmpDispatchInfo.STREAM_OD == strContext || RtmpDispatchInfo.STREAM_SD == strContext) {
            RtmpDispatchInfo info2 = new RtmpDispatchInfo();
            onRtmpDispatch(Boolean.valueOf(RtmpDispatchRequest.onGetStreamAddr(strReponse, info2)), strContext, info2);
        } else if ("SendBamboos" == strContext) {
            if (bResult) {
                info = new ResultMsgInfo();
                String consume = LiveRoomRequest.readBamboos(strReponse, info);
                try {
                    if (!TextUtils.isEmpty(consume) && Integer.parseInt(consume) > 0) {
                        refreshBambooNum();
                    } else if (!TextUtils.isEmpty(info.errmsg)) {
                        Toast.makeText(this, info.errmsg, 0).show();
                    }
                } catch (Exception e) {
                }
            } else {
                showToast(R.string.fail_for_network_error);
            }
        } else if ("GetMyBamboos" == strContext) {
            String strBamboos = "0";
            if (bResult) {
                info = new ResultMsgInfo();
                strBamboos = LiveRoomRequest.readBamboos(strReponse, info);
                if (strBamboos == null || strBamboos.isEmpty()) {
                    strBamboos = "0";
                }
            }
            MyApplication.getInstance().GetLoginManager().GetUserInfo().bamboos = strBamboos;
            ((TextView) findViewById(R.id.textview_full_contol_bamboo_num)).setText(strBamboos);
            ((TextView) findViewById(R.id.my_bamboo_num)).setText(strBamboos);
        } else if ("SendAcquireBamboosTask" == strContext) {
            if (bResult) {
                info = new ResultMsgInfo();
                this.mCurrentBambooList.resetAllData();
                LiveRoomRequest.readAcqureBambooTask(strReponse, info, this.mCurrentBambooList);
                if (info.error == 0) {
                    this.mBammbooTask.setVisibility(0);
                    SetBammbooImageButtonState(false);
                }
                if (this.mCurrentBambooList.interval > 0) {
                    this.mTaskID = this.mCurrentBambooList.my_task_id;
                    if (this.mCurrentBambooList.done.equalsIgnoreCase("2")) {
                        TimeTaskFinish();
                    } else {
                        StartTimeForTask(this.mCurrentBambooList.interval);
                    }
                } else {
                    this.mTaskState = TASK_STATE.TASK_OVER;
                }
            }
        } else if ("SendBamboosTaskDone" == strContext) {
            if (bResult) {
                this.mTaskState = TASK_STATE.STARTING_VERIFY;
                if (this.mTaskState == TASK_STATE.PRE_TASKDONE_VERIFY) {
                    openVerifyDialog();
                }
            } else {
                this.mTaskState = TASK_STATE.PRE_TASKDONE_VERIFY;
                ToastUtils.show(this, getString(R.string.fail_for_network_error));
            }
        } else if ("SendBamboosTaskReward" == strContext) {
            if (bResult) {
                info = new ResultMsgInfo();
                String strData = LiveRoomRequest.readGetTaskReward(strReponse, info);
                if (info.error == 601) {
                    ToastUtils.show(this, getString(R.string.acquire_bamboo_by_others));
                } else if (TextUtils.isEmpty(strData)) {
                    ToastUtils.show(this, getString(R.string.fail_for_network_busy));
                } else {
                    refreshBambooNum();
                    ToastUtils.show(this, String.format(getString(R.string.acquire_bamboo_num), new Object[]{strData}));
                }
                this.mTaskState = TASK_STATE.PRE_GETTASKLIST;
                SetBammbooImageButtonState(false);
                sendAcqureBambooTask();
            } else {
                ToastUtils.show(this, getString(R.string.fail_for_network_error));
            }
        }
        return false;
    }

    public void connectionEstablished(String userId) {
        if (userId.endsWith(this.mAppId)) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (!LiveRoomActivity.this.isLimitShowChatError) {
                        LiveRoomActivity.this.mChatRoomView.BroadcastMessage("服务器已连接");
                    } else if (LiveRoomActivity.this.mLimitShowChatErrorFlag < 2) {
                        LiveRoomActivity.this.mChatRoomView.BroadcastMessage("服务器已连接");
                        LiveRoomActivity.this.mLimitShowChatErrorFlag = 2;
                    }
                }
            });
        }
    }

    public void connectionFailed(String userId) {
    }

    public void connectionLost(String userId, Throwable cause) {
        if (userId.endsWith(this.mAppId)) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (!LiveRoomActivity.this.isLimitShowChatError) {
                        LiveRoomActivity.this.mChatRoomView.BroadcastMessage("服务器连接中断");
                    } else if (LiveRoomActivity.this.mLimitShowChatErrorFlag < 1) {
                        LiveRoomActivity.this.mChatRoomView.BroadcastMessage("服务器连接中断");
                        LiveRoomActivity.this.mLimitShowChatErrorFlag = 1;
                    }
                }
            });
        }
    }


    private void onDispatchReceiveChatInfo(MessageDataInfo info) {
        if (!info.toRoomId.equalsIgnoreCase(this.mRoomState.mRoomId)) {
            return;
        }
        if (info.dataType.equalsIgnoreCase("1")) {
            if (!info.recvId.equalsIgnoreCase(info.currentId)) {
                this.mChatRoomView.ReceiveMessage(info.fromUserNick, info.contentData, info.userType);
            }
//            this.mDanmakuViewWrap.addDanmaku(info.contentData, false);
        } else if (info.dataType.equalsIgnoreCase("206")) {
            this.mChatRoomView.ReceiveBambooMessage(info.fromUserNick, info.contentData, info.userType);
        } else if (info.dataType.equalsIgnoreCase("207")) {
            refreshHostWatchNum(info.contentData);
        } else if (info.dataType.equalsIgnoreCase("208")) {
            refreshHostBambooNum(info.contentData);
        } else if (info.dataType.equalsIgnoreCase("10")) {
            onServerStreamBreak();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 272) {
            if (resultCode == 256) {
                this.mChatRoomHostInfoView.refreshFollowState();
                this.mChatRoomView.refreshBamboo();
                try {
                    //KDPushManagerX.getInstance().stop();
                } catch (Exception e) {
                    //LogUtil.e(TAG, e.toString());
                }
                this.m_request.sendGetChatInfo(this.mRoomState.mRoomId, "GetChatInfo");
            }
        }
// else if (requestCode == TaskListActivity.TASK_LIST_REQUEST_CODE) {
//            this.mChatRoomView.refreshBamboo();
//        }
    }

    public void onChangeRoomInfo(RoomInfo info) {
        this.mRoomState.mInfoExtend.roomInfo = info;
        updatePersonNumText();
    }

    private void openVerifyDialog() {
        //this.geetestLib.setCaptchaURL(UrlConst.getVerifyGetUrl());
        //this.geetestLib.setValidateURL(UrlConst.getVerifyCodeUrl());
        //new GtAppDlgTask().execute(new Void[0]);
    }

    private void refreshHostWatchNum(String watchnum) {
        if (watchnum != null && !watchnum.isEmpty()) {
            this.mRoomState.mInfoExtend.roomInfo.personNum = watchnum;
            updatePersonNumText();
        }
    }

    private void refreshHostBambooNum(String bamboonum) {
        if (this.mChatRoomHostInfoView != null) {
            this.mChatRoomHostInfoView.updateHostBamboo(bamboonum);
        }
    }

    private void onServerStreamBreak() {
        stopPlay();
    }

    private void refreshBambooNum() {
        this.m_request.sendMyBamboos("GetMyBamboos");
        Intent intent = new Intent(CommonDefine.BROADCAST_BAMBOOS_CHANGED);
        intent.setPackage(getPackageName());
        sendBroadcast(intent);
    }

    private void openGtTest(Context ctx, String captcha, String challenge, boolean success) {
//        GtDialog dialog = new GtDialog(ctx, captcha, challenge, Boolean.valueOf(success));
//        dialog.setGtListener(new GtListener() {
//            public void gtResult(boolean success, String result) {
//                if (success) {
//                    try {
//                        JSONObject res_json = new JSONObject(result);
//                        Map<String, String> params = new HashMap();
//                        params.put("challenge", res_json.getString("geetest_challenge"));
//                        params.put("validate", res_json.getString("geetest_validate"));
//                        params.put("seccode", res_json.getString("geetest_seccode"));
//                        String response = LiveRoomActivity.this.geetestLib.submitPostData(params, "utf-8");
//                        ResultMsgInfo info = new ResultMsgInfo();
//                        VerifyCodeInfo code_info = new VerifyCodeInfo();
//                        if (LiveRoomRequest.readVerifyCode(response, info, code_info)) {
//                            LiveRoomActivity.this.mTaskState = TASK_STATE.PRE_GETREWARD;
//                            LiveRoomActivity.this.m_request.sendGetTaskReward("SendBamboosTaskReward", LiveRoomActivity.this.mTaskID, code_info.challenge, code_info.validate, code_info.seccode, code_info.sign, code_info.timestamp);
//                            return;
//                        }
//                        ToastUtils.show(LiveRoomActivity.this, LiveRoomActivity.this.getString(R.string.fail_for_network_error));
//                        return;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        return;
//                    }
//                }
//                ToastUtils.show(LiveRoomActivity.this, LiveRoomActivity.this.getString(R.string.acquire_bamboo_verify_error));
//            }
//
//            public void closeGt() {
//            }
//        });
//        dialog.show();
    }

    private void sendAcqureBambooTask() {
    }

    private void StartTimeForTask(int interval) {
        if (TASK_STATE.STARTING_TASK != this.mTaskState) {
            this.mTaskStartTime = java.lang.System.currentTimeMillis();
            this.mTaskDelay = interval;
            this.mTaskState = TASK_STATE.STARTING_TASK;
            this.mTimer.schedule(new TimerTask() {
                public void run() {
                    LiveRoomActivity.this.TimeTaskFinish();
                }
            }, (long) ((interval * 1000) * 1));
        }
    }

    private void TimeTaskFinish() {
        this.mTaskState = TASK_STATE.PRE_TASKDONE;
        this.m_request.sendBambooTaskDone("SendBamboosTaskDone", this.mCurrentBambooList.id);
        EnableTaskButton();
    }

    private void EnableTaskButton() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                LiveRoomActivity.this.SetBammbooImageButtonState(true);
                LiveRoomActivity.this.mTaskStartTime = 0;
                LiveRoomActivity.this.mTaskDelay = 0;
            }
        });
    }

    private void SetBammbooImageButtonState(boolean available) {
        this.mBambooAvailable = available;
        if (available) {
            this.mBammbooTask.setImageResource(R.drawable.bamboo_task_available);
        } else {
            this.mBammbooTask.setImageResource(R.drawable.bamboo_task_unavailable);
        }
    }

    private int getStatusBarHeight() {
        int result = 0;
        try {
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "");
            if (resourceId > 0) {
                result = getResources().getDimensionPixelSize(resourceId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result == 0) {
            return DpUtils.Dp2Px(this, 25.0f);
        }
        return result;
    }
}
