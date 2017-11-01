package com.panda.videoliveplatform.activity;

import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//import com.panda.videolivecore.UpdateManager;
//import com.panda.videolivecore.data.ColumnLiveItemInfo.Data;
import com.panda.videolivecore.data.ColumnLiveItemInfo;
import com.panda.videolivecore.data.HotLiveItemInfo;
//import com.panda.videolivecore.data.LiveItemInfo;
import com.panda.videolivecore.data.LiveItemInfo;
import com.panda.videolivecore.data.MultiCateLiveItemInfo;
import com.panda.videolivecore.data.MultiCateLiveItemInfo.SubType;
import com.panda.videolivecore.data.SliderItemInfo;
//import com.panda.videolivecore.utils.NotifyEvent;
import com.panda.videoliveplatform.R;
import com.panda.videoliveplatform.fragment.AccountFragment;
import com.panda.videoliveplatform.fragment.AccountFragment.OnAccountFragmentListener;
import com.panda.videoliveplatform.fragment.ColumnLiveFragment;
import com.panda.videoliveplatform.fragment.ColumnLiveFragment.OnColumnLiveFragmentListener;
import com.panda.videoliveplatform.fragment.FragmentAdapter;
import com.panda.videoliveplatform.fragment.HomeFragment;
import com.panda.videoliveplatform.fragment.HomeFragment.OnHomeFragmentListener;
import com.panda.videoliveplatform.fragment.LiveFragment;
import com.panda.videoliveplatform.fragment.LiveFragment.OnLiveFragmentListener;
//import com.panda.videoliveplatform.service.LiveService;
//import com.umeng.analytics.AnalyticsConfig;
//import com.umeng.analytics.MobclickAgent;
//import de.greenrobot.event.EventBus;
import java.util.ArrayList;

public class MainFragmentActivity extends FragmentActivity implements OnHomeFragmentListener, OnColumnLiveFragmentListener, OnLiveFragmentListener, OnAccountFragmentListener {
    private static final String TAG = "cjl";
    private final int FRAGMENT_COLUMN_LIVE = 1;
    private final int FRAGMENT_HOME = 0;
    private final int FRAGMENT_LIVE = 2;
    private final int FRAGMENT_USER = 3;
    private AccountFragment account_frag;
    private Boolean bFlag = Boolean.valueOf(false);
    private ColumnLiveFragment column_live_frag;
    private View contentview = null;
    private int currentFragment;
    ArrayList<Fragment> fragmentArray;
    private HomeFragment home_frag;
    private LiveFragment live_frag;
    private TextView mColumnLivebtn;
    private RelativeLayout mFrameTitleHeight;
    private TextView mHomeBtn;
    private long mLastPressBackTime = 0;
    private TextView mLivebtn;
    private ViewPager mPager;
    private ImageView mSearchIcon;
    private ImageView mTitleIcon;
    private TextView mUserBtn;
    private boolean mUserBtnShowPoint = false;
    private PopupWindow m_popupWindow;
    private TextView txtUpdateContent;

    public class MyOnPageChangeListener implements OnPageChangeListener {
        public void onPageSelected(int arg0) {
            MainFragmentActivity.this.currentFragment = arg0;
            MainFragmentActivity.this.setSelect(arg0);
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageScrollStateChanged(int arg0) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);
        //AnalyticsConfig.enableEncrypt(true);
        initMembers();
//        startService(new Intent(this, LiveService.class));
        //UpdateManager.checkForceUpdateFlag(this, findViewById(R.id.mainlayout));
        if (VERSION.SDK_INT >= 19) {
            findViewById(R.id.statusbar_dummy).setVisibility(View.VISIBLE);
            Window win = getWindow();
            LayoutParams winParams = win.getAttributes();
            winParams.flags |= 67108864;
            win.setAttributes(winParams);
        }
        RegisterEventBus();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        UnRegisterEventBus();
    }

    private void initMembers() {
        if (this.home_frag == null) {
            FragmentManager fm = getSupportFragmentManager();
            this.home_frag = HomeFragment.newInstance(this);
            this.live_frag = LiveFragment.newInstance(this);
            this.column_live_frag = ColumnLiveFragment.newInstance(this);
            this.account_frag = AccountFragment.newInstance();
            this.mFrameTitleHeight = (RelativeLayout) findViewById(R.id.fragment_title_height);
            this.mTitleIcon = (ImageView) findViewById(R.id.fragment_title_icon);
            this.mSearchIcon = (ImageView) findViewById(R.id.fragment_title_search);
            this.mPager = (ViewPager) findViewById(R.id.viewpager);
            this.fragmentArray = new ArrayList();
            this.fragmentArray.add(this.home_frag);
            this.fragmentArray.add(this.column_live_frag);
            this.fragmentArray.add(this.live_frag);
            this.fragmentArray.add(this.account_frag);
            this.mPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), this.fragmentArray));
            this.mPager.setCurrentItem(0);
            this.mPager.setOnPageChangeListener(new MyOnPageChangeListener());
            this.mPager.setOffscreenPageLimit(3);
            findViewById(R.id.main_home).setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    MainFragmentActivity.this.changeFragment(0);
                }
            });
            findViewById(R.id.main_column_live).setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    MainFragmentActivity.this.changeFragment(1);
                }
            });
            findViewById(R.id.main_live).setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    MainFragmentActivity.this.changeFragment(2);
                }
            });
            findViewById(R.id.main_user).setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    MainFragmentActivity.this.changeFragment(3);
                }
            });
            this.mHomeBtn = (TextView) findViewById(R.id.main_home_btn);
            this.mColumnLivebtn = (TextView) findViewById(R.id.main_column_live_btn);
            this.mLivebtn = (TextView) findViewById(R.id.main_live_btn);
            this.mUserBtn = (TextView) findViewById(R.id.main_user_btn);
            this.mSearchIcon.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(MainFragmentActivity.this, SearchActivity.class);
                    MainFragmentActivity.this.startActivity(intent);
                }
            });
            setSelect(0);
        }
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() - this.mLastPressBackTime > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            this.mLastPressBackTime = System.currentTimeMillis();
            return;
        }
        finish();
        System.exit(0);
    }

    public void openLiveRoom(String addrStream, String urlRoom, String urlImage, String idRoom) {
        Intent intent = new Intent();
        intent.setClass(this, LiveRoomActivity.class);
        intent.putExtra("addrStream", addrStream);
        intent.putExtra("urlRoom", urlRoom);
        intent.putExtra("urlImage", urlImage);
        intent.putExtra("idRoom", idRoom);
        startActivity(intent);
    }

    public void onHomeHotItemClick(HotLiveItemInfo info) {
        openLiveRoom("", info.url, info.img, info.roomid);
    }

    public void onHomeCateItemClick(MultiCateLiveItemInfo info) {
        Log.d(TAG, "onHomeCateItemClick: @@@@");
        //点击首页的直播间
        openLiveRoom("", "", "", info.id);
    }

    public void onSliderClick(SliderItemInfo info) {
        Log.d(TAG, "onSliderClick: @@@");
        //点击slider的直播间
        openLiveRoom("", info.url, info.img, info.roomid);
    }

    public void onShowFragmentLive() {
        Log.d(TAG, "onShowFragmentLive: 22222222");
        changeFragment(2);
    }

    public void onOpenSubLiveActivity(SubType type) {
        //点击首页TAB的更多
        Intent intent = new Intent();
        intent.setClass(this, SubLiveActivity.class);
        intent.putExtra("cname", type.cname);
        intent.putExtra("ename", type.ename);
        startActivity(intent);
    }

    public void onLiveItemClick(LiveItemInfo info) {
        //点击直播TAB里的直播间
        openLiveRoom("", "", info.pictures.img != null ? info.pictures.img : "", info.id);
    }


    public void setAccountPoint(boolean show) {
        if (this.mUserBtnShowPoint != show) {
            this.mUserBtnShowPoint = show;
            setUserBtnDrawable();
        }
    }

    private void setUserBtnDrawable() {
        if (this.currentFragment == 3) {
            if (this.mUserBtnShowPoint) {
                this.mUserBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.user_select_point, 0, 0);
            } else {
                this.mUserBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.user_select, 0, 0);
            }
        } else if (this.mUserBtnShowPoint) {
            this.mUserBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.user_point, 0, 0);
        } else {
            this.mUserBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.user, 0, 0);
        }
    }

    public void onColumnLiveItemClick(ColumnLiveItemInfo.Data data) {
        //点击栏目TAB里的某个分类
        Intent intent = new Intent();
        intent.setClass(this, SubLiveActivity.class);
        intent.putExtra("cname", data.cname);
        intent.putExtra("ename", data.ename);
        startActivity(intent);
    }

    private void changeFragment(int index) {
        if (index >= 0 && index < this.fragmentArray.size()) {
        boolean same = this.currentFragment == index;
        this.mPager.setCurrentItem(index);
        this.currentFragment = index;
        setSelect(index);
            if (same && 2 == index && this.live_frag != null) {
                this.live_frag.ScrollToTop();
            }
         }
    }

    private void setSelect(int index) {
        if (index >= 0 && index < this.fragmentArray.size()) {
            switch (index) {
                case 0:
                    this.mTitleIcon.setVisibility(View.VISIBLE);
                    this.mSearchIcon.setVisibility(View.VISIBLE);
                    this.mHomeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home_pressed, 0, 0);
                    this.mHomeBtn.setTextColor(getResources().getColor(R.color.title_color));
                    this.mColumnLivebtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.column, 0, 0);
                    this.mColumnLivebtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                    this.mLivebtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.live, 0, 0);
                    this.mLivebtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                    this.mUserBtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                    break;
                case 1:
                    this.mTitleIcon.setVisibility(View.VISIBLE);
                    this.mSearchIcon.setVisibility(View.VISIBLE);
                    this.mHomeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home, 0, 0);
                    this.mHomeBtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                    this.mColumnLivebtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.column_pressed, 0, 0);
                    this.mColumnLivebtn.setTextColor(getResources().getColor(R.color.title_color));
                    this.mLivebtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.live, 0, 0);
                    this.mLivebtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                    this.mUserBtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                    break;
                case 2:
                    this.mTitleIcon.setVisibility(View.VISIBLE);
                    this.mSearchIcon.setVisibility(View.VISIBLE);
                    this.mHomeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home, 0, 0);
                    this.mHomeBtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                    this.mColumnLivebtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.column, 0, 0);
                    this.mColumnLivebtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                    this.mLivebtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.live_pressed, 0, 0);
                    this.mLivebtn.setTextColor(getResources().getColor(R.color.title_color));
                    this.mUserBtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                    break;
                case 3:
                    this.mTitleIcon.setVisibility(View.INVISIBLE);
                    this.mSearchIcon.setVisibility(View.INVISIBLE);
                    this.mHomeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home, 0, 0);
                    this.mHomeBtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                    this.mColumnLivebtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.column, 0, 0);
                    this.mColumnLivebtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                    this.mLivebtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.live, 0, 0);
                    this.mLivebtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                    this.mUserBtn.setTextColor(getResources().getColor(R.color.title_color));
                    break;
            }
            setUserBtnDrawable();
        }
    }

    private void RegisterEventBus() {
        //EventBus.getDefault().register(this);
    }

    private void UnRegisterEventBus() {
        //EventBus.getDefault().unregister(this);
    }

//    public void onEventMainThread(NotifyEvent event) {
//        if (event.getMsg().equals(NotifyEvent.MSG_START_LOGIN_UI)) {
//            LoginActivity.showLogin(this, true);
//        }
//    }
}