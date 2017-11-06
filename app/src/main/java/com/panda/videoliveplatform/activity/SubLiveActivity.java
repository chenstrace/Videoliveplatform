package com.panda.videoliveplatform.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.panda.videolivecore.data.GsonRequest;
import com.panda.videolivecore.data.RequestManager;
import com.panda.videolivecore.data.SubLiveItemInfo;
import com.panda.videolivecore.data.SubLiveItemInfo.ResponseData;
import com.panda.videolivecore.net.UrlConst;
import com.panda.videoliveplatform.MyApplication;
import com.panda.videoliveplatform.R;
import com.panda.videoliveplatform.activity.SubLiveListAdapter.OnSubLiveItemListener;

public class SubLiveActivity extends Activity implements OnSubLiveItemListener {
    private static final String TAG = "cjl";
    private SubLiveListAdapter mAdapter;
    private String mCname;
    private String mEname;
    private boolean mIsLoadingData = false;
    private View mLoadCompeleteNodataView;
    private View mLoadErrorView;
    private View mLoadingView;
    private int mPage = 1;
    private PullToRefreshGridView mPullRefreshGridView;
    private int mTotalItem = 0;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_sub_live);
        this.mCname = getIntent().getStringExtra("cname");
        this.mEname = getIntent().getStringExtra("ename");
        initViews();
    }

    public void onDestroy() {
        super.onDestroy();
        RequestManager.cancelAll(this);
    }

    private void initViews() {
        initLoadingView();
        if (VERSION.SDK_INT >= 19) {
            findViewById(R.id.statusbar_dummy).setVisibility(0);
            Window win = getWindow();
            LayoutParams winParams = win.getAttributes();
            winParams.flags |= 67108864;
            win.setAttributes(winParams);
        }
        ((TextView) findViewById(R.id.title_text)).setText(this.mCname);
        ((ImageButton) findViewById(R.id.image_button_back)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SubLiveActivity.this.finish();
            }
        });
        this.mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.livelist);
        this.mPullRefreshGridView.setOnRefreshListener(new OnRefreshListener<GridView>() {
            public void onRefresh(PullToRefreshBase<GridView> refreshView) {
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(DateUtils.formatDateTime(MyApplication.getInstance().getApplicationContext(), System.currentTimeMillis(), 524305));
                if (!SubLiveActivity.this.mIsLoadingData) {
                    SubLiveActivity.this.mPage = 1;
                    SubLiveActivity.this.reLoadData();
                }
            }
        });
        this.mPullRefreshGridView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount && SubLiveActivity.this.mAdapter.getItemCount() < SubLiveActivity.this.mTotalItem && !SubLiveActivity.this.mIsLoadingData) {
                    SubLiveActivity.this.loadData(SubLiveActivity.this.mPage);
                }
            }
        });
        this.mPullRefreshGridView.setMode(Mode.PULL_FROM_START);
        this.mPullRefreshGridView.setScrollingWhileRefreshingEnabled(true);
        this.mAdapter = new SubLiveListAdapter(MyApplication.getInstance().getApplicationContext(), this);
        this.mPullRefreshGridView.setAdapter(this.mAdapter);
        this.mPullRefreshGridView.setEmptyView(findViewById(R.id.loadview));
        reLoadData();
    }

    public void onSubLiveItemClick(SubLiveItemInfo info) {
        Intent intent = new Intent();
        intent.setClass(this, LiveRoomActivity.class);
        intent.putExtra("addrStream", "");
        intent.putExtra("urlRoom", "");
        intent.putExtra("urlImage", info.pictures.img != null ? info.pictures.img : "");
        intent.putExtra("idRoom", info.id);
        startActivity(intent);
    }


    protected void loadData(int page) {
        final boolean isRefreshFromTop = this.mPage == 1;
        this.mIsLoadingData = true;
        executeRequest(new GsonRequest(getUrl(page), ResponseData.class, new Listener<ResponseData>() {
            public void onResponse(ResponseData response) {
                if (response.errno == 0) {
                    try {
                        SubLiveActivity.this.mTotalItem = Integer.parseInt(response.data.total);
                    } catch (NumberFormatException e) {
                    }
                    if (isRefreshFromTop) {
                        SubLiveActivity.this.mAdapter.deleteAllData();
                    }
                    SubLiveActivity.this.mPage = SubLiveActivity.this.mPage + 1;
                    SubLiveActivity.this.mAdapter.insertData(response.data.items);
                    SubLiveActivity.this.loadDataSuccess();
                    SubLiveActivity.this.loadCompelete(SubLiveActivity.this.mTotalItem);
                }
                SubLiveActivity.this.mPullRefreshGridView.onRefreshComplete();
                if (response.errno != 0) {
                    SubLiveActivity.this.loadDataError();
                }
                SubLiveActivity.this.mIsLoadingData = false;
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                SubLiveActivity.this.mPullRefreshGridView.onRefreshComplete();
                SubLiveActivity.this.loadDataError();
                SubLiveActivity.this.mIsLoadingData = false;
            }
        }));
    }

    protected String getUrl(int page) {
        return UrlConst.getSubLiveUrl(this.mEname, page);
    }

    protected void reLoadData() {
        loadData(1);
    }

    protected void executeRequest(Request request) {
        RequestManager.addRequest(request, this);
    }

    protected void initLoadingView() {
        this.mLoadingView = findViewById(R.id.loading);
        this.mLoadErrorView = findViewById(R.id.loaderror);
        this.mLoadCompeleteNodataView = findViewById(R.id.loadsuccess_nodata);
        this.mLoadErrorView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SubLiveActivity.this.mLoadingView.setVisibility(0);
                SubLiveActivity.this.mLoadErrorView.setVisibility(8);
                SubLiveActivity.this.mLoadCompeleteNodataView.setVisibility(8);
                SubLiveActivity.this.reLoadData();
            }
        });
    }

    protected void loadDataSuccess() {
    }

    protected void loadCompelete(int num) {
        if (num <= 0) {
            this.mLoadErrorView.setVisibility(8);
            this.mLoadingView.setVisibility(8);
            this.mLoadCompeleteNodataView.setVisibility(0);
        }
    }

    protected void loadDataError() {
        this.mLoadErrorView.setVisibility(0);
        this.mLoadingView.setVisibility(8);
        this.mLoadCompeleteNodataView.setVisibility(8);
    }
}
