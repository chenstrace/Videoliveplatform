package com.panda.videoliveplatform.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
//import com.panda.videolivecore.data.GsonRequest;
//import com.panda.videolivecore.data.HotLiveItemInfo;
//import com.panda.videolivecore.data.MultiCateLiveItemInfo;
//import com.panda.videolivecore.data.MultiCateLiveItemInfo.ResponseData;
//import com.panda.videolivecore.data.MultiCateLiveItemInfo.SubType;
//import com.panda.videolivecore.data.SliderItemInfo;
//import com.panda.videolivecore.data.SliderItemInfo.ResponseData;
//import com.panda.videolivecore.net.UrlConst;
import com.panda.videoliveplatform.MyApplication;
import com.panda.videoliveplatform.R;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment {
    private HomeListAdapter mAdapter;
    private Context mContext;
    private OnHomeFragmentListener mListener;
    private PullToRefreshListView mPullRefreshListView;

    private void initViews(View paramView) {
        initLoadingView(paramView);
        this.mPullRefreshListView = ((PullToRefreshListView) paramView.findViewById(R.id.livelist));

        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase refreshView) {
                String str = DateUtils.formatDateTime(MyApplication.getInstance().getApplicationContext(), System.currentTimeMillis(), 524305);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(str);
                HomeFragment.this.reLoadData();
            }
        });


        this.mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        this.mPullRefreshListView.setScrollingWhileRefreshingEnabled(true);
        this.mAdapter = new HomeListAdapter(MyApplication.getInstance().getApplicationContext(), this.mListener);
        this.mPullRefreshListView.setAdapter(this.mAdapter);
        View localView = paramView.findViewById(R.id.loadview);
        this.mPullRefreshListView.setEmptyView(localView);
        reLoadData();
    }

    public static HomeFragment newInstance(Context paramContext) {
        HomeFragment localHomeFragment = new HomeFragment();
        localHomeFragment.setParams(paramContext);
        return localHomeFragment;
    }

    protected String getHotLiveUrl() {
        //return UrlConst.getHomeHotLiveUrl();
        return "";
    }

    protected String getMultiCateUrl() {
//        return UrlConst.getHomeMultiCateUrl();
        return "";
    }

    protected String getSliderUrl() {
        //return UrlConst.getHomeSliderUrl();
        return "";
    }

    protected void loadHotLiveData() {
    }

    protected void loadMultiCateData() {
        /*
        executeRequest(new GsonRequest(getMultiCateUrl(), MultiCateLiveItemInfo.ResponseData.class, new Response.Listener() {
            public void onResponse(MultiCateLiveItemInfo.ResponseData paramAnonymousResponseData) {
                int i = 0;
                if (paramAnonymousResponseData.errno == 0)
                    if (paramAnonymousResponseData.data.size() > 0)
                        HomeFragment.this.mAdapter.updateMultiCateList(paramAnonymousResponseData.data);
                while (true) {
                    HomeFragment.this.mPullRefreshListView.onRefreshComplete();
                    if (i != 0)
                        HomeFragment.this.loadDataError();
                    return;
                    i = 1;
                    continue;
                    i = 1;
                }
            }
        }
                , new Response.ErrorListener() {
            public void onErrorResponse(VolleyError paramAnonymousVolleyError) {
                HomeFragment.this.mPullRefreshListView.onRefreshComplete();
                HomeFragment.this.loadDataError();
            }
        }));
        */
    }

    protected void loadSliderData() {
        /*
        executeRequest(new GsonRequest(getSliderUrl(), SliderItemInfo.ResponseData.class, new Response.Listener() {
            public void onResponse(SliderItemInfo.ResponseData paramAnonymousResponseData) {
                if (paramAnonymousResponseData.errno == 0) {
                    HomeFragment.this.mAdapter.updateSliderList(paramAnonymousResponseData.data);
                    HomeFragment.this.loadDataSuccess();
                }
                HomeFragment.this.mPullRefreshListView.onRefreshComplete();
                if (paramAnonymousResponseData.errno != 0)
                    HomeFragment.this.loadDataError();
            }
        }
                , new Response.ErrorListener() {
            public void onErrorResponse(VolleyError paramAnonymousVolleyError) {
                HomeFragment.this.mPullRefreshListView.onRefreshComplete();
                HomeFragment.this.loadDataError();
            }
        }));
        */
    }

    public void onAttach(Activity paramActivity) {
        super.onAttach(paramActivity);
        try {
            this.mListener = ((OnHomeFragmentListener) paramActivity);
            return;
        } catch (ClassCastException localClassCastException) {
        }
        throw new ClassCastException(paramActivity.toString() + " must implement OnFragmentInteractionListener");
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        View localView = paramLayoutInflater.inflate(R.layout.fragment_home, null);
        initViews(localView);
        return localView;
    }

    public void onDetach() {
        this.mListener = null;
        super.onDetach();
    }

    protected boolean reLoadData() {
        loadSliderData();
        loadMultiCateData();
        return true;
    }

    public void setParams(Context paramContext) {
        this.mContext = paramContext;
    }

    public static abstract interface OnHomeFragmentListener {
//    public abstract void onHomeCateItemClick(MultiCateLiveItemInfo paramMultiCateLiveItemInfo);
//
//    public abstract void onHomeHotItemClick(HotLiveItemInfo paramHotLiveItemInfo);
//
//    public abstract void onOpenSubLiveActivity(MultiCateLiveItemInfo.SubType paramSubType);
//
//    public abstract void onShowFragmentLive();
//
//    public abstract void onSliderClick(SliderItemInfo paramSliderItemInfo);
    }
}

