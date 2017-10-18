package com.panda.videoliveplatform.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//import com.panda.videolivecore.UpdateManager;
//import com.panda.videolivecore.data.ColumnLiveItemInfo.Data;
//import com.panda.videolivecore.data.HotLiveItemInfo;
//import com.panda.videolivecore.data.LiveItemInfo;
//import com.panda.videolivecore.data.LiveItemInfo.Pictures;
//import com.panda.videolivecore.data.MultiCateLiveItemInfo;
//import com.panda.videolivecore.data.MultiCateLiveItemInfo.SubType;
//import com.panda.videolivecore.data.SliderItemInfo;
//import com.panda.videolivecore.utils.NotifyEvent;
//import com.panda.videoliveplatform.fragment.AccountFragment;
//import com.panda.videoliveplatform.fragment.AccountFragment.OnAccountFragmentListener;
//import com.panda.videoliveplatform.fragment.ColumnLiveFragment;
//import com.panda.videoliveplatform.fragment.ColumnLiveFragment.OnColumnLiveFragmentListener;
import com.panda.videoliveplatform.R;
import com.panda.videoliveplatform.fragment.FragmentAdapter;
import com.panda.videoliveplatform.fragment.HomeFragment;
import com.panda.videoliveplatform.fragment.HomeFragment.OnHomeFragmentListener;
//import com.panda.videoliveplatform.fragment.LiveFragment;
//import com.panda.videoliveplatform.fragment.LiveFragment.OnLiveFragmentListener;
//import com.panda.videoliveplatform.service.LiveService;
//import com.umeng.analytics.AnalyticsConfig;
//import com.umeng.analytics.MobclickAgent;
//import de.greenrobot.event.EventBus;
import java.util.ArrayList;

public class MainFragmentActivity extends FragmentActivity
        implements HomeFragment.OnHomeFragmentListener
        //, LiveFragment.OnLiveFragmentListener, ColumnLiveFragment.OnColumnLiveFragmentListener, AccountFragment.OnAccountFragmentListener
{
    private final int FRAGMENT_COLUMN_LIVE = 1;
    private final int FRAGMENT_HOME = 0;
    private final int FRAGMENT_LIVE = 2;
    private final int FRAGMENT_USER = 3;
    //private AccountFragment account_frag;
    private Boolean bFlag = Boolean.valueOf(false);
    //private ColumnLiveFragment column_live_frag;
    private View contentview = null;
    private int currentFragment;
    ArrayList<Fragment> fragmentArray;
    private HomeFragment home_frag;
    //private LiveFragment live_frag;
    private TextView mColumnLivebtn;
    private RelativeLayout mFrameTitleHeight;
    private TextView mHomeBtn;
    private long mLastPressBackTime = 0L;
    private TextView mLivebtn;
    private ViewPager mPager;
    private ImageView mSearchIcon;
    private ImageView mTitleIcon;
    private TextView mUserBtn;
    private boolean mUserBtnShowPoint = false;
    private PopupWindow m_popupWindow;
    private TextView txtUpdateContent;

    private void RegisterEventBus() {
        //EventBus.getDefault().register(this);
    }

    private void UnRegisterEventBus() {
        //EventBus.getDefault().unregister(this);
    }

    private void changeFragment(int paramInt) {
//        if ((paramInt < 0) || (paramInt >= this.fragmentArray.size()))
//            return;
//        if (this.currentFragment == paramInt) return;
//        for (int i = 1; ; i = 0) {
//            this.mPager.setCurrentItem(paramInt);
//            this.currentFragment = paramInt;
//            setSelect(paramInt);
//            if ((i == 0) || (2 != paramInt) || (this.live_frag == null))
//                break;
//            this.live_frag.ScrollToTop();
//            break;
//        }
    }

    private void initMembers() {
        if (this.home_frag != null) return;
        getSupportFragmentManager();
        this.home_frag = HomeFragment.newInstance(this);
        //this.live_frag = LiveFragment.newInstance(this);
        //this.column_live_frag = ColumnLiveFragment.newInstance(this);
        //this.account_frag = AccountFragment.newInstance();
        this.mFrameTitleHeight = ((RelativeLayout) findViewById(R.id.fragment_title_height));  //2131362005
        this.mTitleIcon = ((ImageView) findViewById(R.id.fragment_title_icon));
        this.mSearchIcon = ((ImageView) findViewById(R.id.fragment_title_search));
        this.mPager = ((ViewPager) findViewById(R.id.viewpager));
        this.fragmentArray = new ArrayList();
        this.fragmentArray.add(this.home_frag);
//            this.fragmentArray.add(this.column_live_frag);
        //this.fragmentArray.add(this.live_frag);
//            this.fragmentArray.add(this.account_frag);
        this.mPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), this.fragmentArray));
        this.mPager.setCurrentItem(0);
        this.mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        this.mPager.setOffscreenPageLimit(3);
        findViewById(R.id.main_home).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                MainFragmentActivity.this.changeFragment(0);
            }
        });
        findViewById(R.id.main_column_live).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                MainFragmentActivity.this.changeFragment(1);
            }
        });
        findViewById(R.id.main_live).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                MainFragmentActivity.this.changeFragment(2);
            }
        });
        findViewById(R.id.main_user).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                MainFragmentActivity.this.changeFragment(3);
            }
        });
        this.mHomeBtn = ((TextView) findViewById(R.id.main_home_btn));
        this.mColumnLivebtn = ((TextView) findViewById(R.id.main_column_live_btn));
        this.mLivebtn = ((TextView) findViewById(R.id.main_live_btn));
        this.mUserBtn = ((TextView) findViewById(R.id.main_user_btn));
        this.mSearchIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
//                    Intent localIntent = new Intent();
//                    localIntent.setClass(MainFragmentActivity.this, SearchActivity.class);
//                    MainFragmentActivity.this.startActivity(localIntent);
            }
        });
        setSelect(0);

    }

    private void setSelect(int paramInt) {
        if ((paramInt < 0) || (paramInt >= this.fragmentArray.size()))
            return;

        setUserBtnDrawable();
        switch (paramInt) {
            case 0:
                this.mTitleIcon.setVisibility(0);
                this.mSearchIcon.setVisibility(0);
                this.mHomeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home_pressed, 0, 0);
                this.mHomeBtn.setTextColor(getResources().getColor(R.color.title_color));
                this.mColumnLivebtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.column, 0, 0);
                this.mColumnLivebtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                this.mLivebtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.live, 0, 0);
                this.mLivebtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                this.mUserBtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                break;
            case 1:
                this.mTitleIcon.setVisibility(0);
                this.mSearchIcon.setVisibility(0);
                this.mHomeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home, 0, 0);
                this.mHomeBtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                this.mColumnLivebtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.column_pressed, 0, 0);
                this.mColumnLivebtn.setTextColor(getResources().getColor(R.color.title_color));
                this.mLivebtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.live, 0, 0);
                this.mLivebtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                this.mUserBtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                break;
            case 2:
                this.mTitleIcon.setVisibility(0);
                this.mSearchIcon.setVisibility(0);
                this.mHomeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home, 0, 0);
                this.mHomeBtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                this.mColumnLivebtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.column, 0, 0);
                this.mColumnLivebtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                this.mLivebtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.live_pressed, 0, 0);
                this.mLivebtn.setTextColor(getResources().getColor(R.color.title_color));
                this.mUserBtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                break;
            case 3:
                this.mTitleIcon.setVisibility(4);
                this.mSearchIcon.setVisibility(4);
                this.mHomeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home, 0, 0);
                this.mHomeBtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                this.mColumnLivebtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.column, 0, 0);
                this.mColumnLivebtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                this.mLivebtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.live, 0, 0);
                this.mLivebtn.setTextColor(getResources().getColor(R.color.webview_bottom_grey));
                this.mUserBtn.setTextColor(getResources().getColor(R.color.title_color));
                break;
        }
    }

    private void setUserBtnDrawable() {
        if (this.currentFragment == 3) {
            if (this.mUserBtnShowPoint)
                this.mUserBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.user_select_point, 0, 0);
            else
                this.mUserBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.user_select, 0, 0);


            if (this.mUserBtnShowPoint)
                this.mUserBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.user_point, 0, 0);
            else
                this.mUserBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.user, 0, 0);
        }
    }

    public void onAccountClickTest(int paramInt) {
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() - this.mLastPressBackTime > 2000L) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", 0).show();
            this.mLastPressBackTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }


    }

//  public void onColumnLiveItemClick(ColumnLiveItemInfo.Data paramData)
//  {
//    Intent localIntent = new Intent();
//    localIntent.setClass(this, SubLiveActivity.class);
//    localIntent.putExtra("cname", paramData.cname);
//    localIntent.putExtra("ename", paramData.ename);
//    startActivity(localIntent);
//  }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_main_fragment);
        //AnalyticsConfig.enableEncrypt(true);
        initMembers();
        //startService(new Intent(this, LiveService.class));
        //UpdateManager.checkForceUpdateFlag(this, findViewById(2131361860));
        if (Build.VERSION.SDK_INT >= 19) {
            findViewById(R.id.statusbar_dummy).setVisibility(0);
            Window localWindow = getWindow();
            WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
            localLayoutParams.flags = (0x4000000 | localLayoutParams.flags);
            localWindow.setAttributes(localLayoutParams);
        }
        RegisterEventBus();
    }

    protected void onDestroy() {
        super.onDestroy();
        UnRegisterEventBus();
    }

//  public void onEventMainThread(NotifyEvent paramNotifyEvent)
//  {
//    if (paramNotifyEvent.getMsg().equals("MAS_START_LOGIN_UI"))
//      LoginActivity.showLogin(this, true);
//  }

/*
  public void onHomeCateItemClick(MultiCateLiveItemInfo paramMultiCateLiveItemInfo)
  {
    openLiveRoom("", "", "", paramMultiCateLiveItemInfo.id);
  }
*/
/*

  public void onHomeHotItemClick(HotLiveItemInfo paramHotLiveItemInfo)
  {
    openLiveRoom("", paramHotLiveItemInfo.url, paramHotLiveItemInfo.img, paramHotLiveItemInfo.roomid);
  }
*/


//  public void onLiveItemClick(LiveItemInfo paramLiveItemInfo)
//  {
//    if (paramLiveItemInfo.pictures.img != null);
//    for (String str = paramLiveItemInfo.pictures.img; ; str = "")
//    {
//      openLiveRoom("", "", str, paramLiveItemInfo.id);
//      return;
//    }
//  }

//  public void onOpenSubLiveActivity(MultiCateLiveItemInfo.SubType paramSubType)
//  {
//    Intent localIntent = new Intent();
//    localIntent.setClass(this, SubLiveActivity.class);
//    localIntent.putExtra("cname", paramSubType.cname);
//    localIntent.putExtra("ename", paramSubType.ename);
//    startActivity(localIntent);
//  }

    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }

    protected void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }

    public void onShowFragmentLive() {
        changeFragment(2);
    }

//  public void onSliderClick(SliderItemInfo paramSliderItemInfo)
//  {
//    openLiveRoom("", paramSliderItemInfo.url, paramSliderItemInfo.img, paramSliderItemInfo.roomid);
//  }

    public void openLiveRoom(String paramString1, String paramString2, String paramString3, String paramString4) {
//    Intent localIntent = new Intent();
//    localIntent.setClass(this, LiveRoomActivity.class);
//    localIntent.putExtra("addrStream", paramString1);
//    localIntent.putExtra("urlRoom", paramString2);
//    localIntent.putExtra("urlImage", paramString3);
//    localIntent.putExtra("idRoom", paramString4);
//    startActivity(localIntent);
    }

    public void setAccountPoint(boolean paramBoolean) {
        if (this.mUserBtnShowPoint != paramBoolean) {
            this.mUserBtnShowPoint = paramBoolean;
            setUserBtnDrawable();
        }
    }

    public class MyOnPageChangeListener
            implements ViewPager.OnPageChangeListener {
        public MyOnPageChangeListener() {
        }

        public void onPageScrollStateChanged(int paramInt) {
        }

        public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
        }

        public void onPageSelected(int paramInt) {
            //MainFragmentActivity.access$102(MainFragmentActivity.this, paramInt);
            //MainFragmentActivity.this.setSelect(paramInt);
        }
    }
}