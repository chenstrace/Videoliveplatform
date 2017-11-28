package com.panda.videoliveplatform.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.panda.videolivecore.data.GsonRequest;
import com.panda.videolivecore.data.HotLiveItemInfo;
import com.panda.videolivecore.data.MultiCateLiveItemInfo;
import com.panda.videolivecore.data.MultiCateLiveItemInfo.SubType;
import com.panda.videolivecore.data.SliderItemInfo;
import com.panda.videolivecore.data.SliderItemInfo.ResponseData;
import com.panda.videolivecore.net.UrlConst;
import com.panda.videoliveplatform.MyApplication;
import com.panda.videoliveplatform.R;

public class HomeFragment extends BaseFragment {
    private HomeListAdapter mAdapter;
    private Context mContext;
    private OnHomeFragmentListener mListener;
    private PullToRefreshListView mPullRefreshListView;

    public interface OnHomeFragmentListener {
        void onHomeCateItemClick(MultiCateLiveItemInfo multiCateLiveItemInfo);

        void onHomeHotItemClick(HotLiveItemInfo hotLiveItemInfo);

        void onOpenSubLiveActivity(SubType subType);

        void onShowFragmentLive();

        void onSliderClick(SliderItemInfo sliderItemInfo);
    }

    public static HomeFragment newInstance(Context context) {
        HomeFragment fragment = new HomeFragment();
        //这里保存了上层MainFragmentActivity的this指针，可用于处理扩展和通信
        fragment.setParams(context);
        return fragment;
    }

    public void setParams(Context context) {
        this.mContext = context;
    }

    protected void loadSliderData() {
        executeRequest(new GsonRequest(getSliderUrl(), ResponseData.class, new Listener<ResponseData>() {
            public void onResponse(ResponseData response) {
                if (response.errno == 0) {
                    HomeFragment.this.mAdapter.updateSliderList(response.data);
                    HomeFragment.this.loadDataSuccess();
                }
                HomeFragment.this.mPullRefreshListView.onRefreshComplete();
                if (response.errno != 0) {
                    HomeFragment.this.loadDataError();
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                HomeFragment.this.mPullRefreshListView.onRefreshComplete();
                HomeFragment.this.loadDataError();
            }
        }));
    }

    protected void loadHotLiveData() {
    }

    protected void loadMultiCateData() {
        executeRequest(new GsonRequest(getMultiCateUrl(), MultiCateLiveItemInfo.ResponseData.class, new Listener<MultiCateLiveItemInfo.ResponseData>() {
            public void onResponse(MultiCateLiveItemInfo.ResponseData response) {
                boolean bLoadError = false;
                if (response.errno != 0) {
                    bLoadError = true;
                } else if (response.data.size() > 0) {
                    HomeFragment.this.mAdapter.updateMultiCateList(response.data);
                } else {
                    bLoadError = true;
                }
                HomeFragment.this.mPullRefreshListView.onRefreshComplete();
                if (bLoadError) {
                    HomeFragment.this.loadDataError();
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                HomeFragment.this.mPullRefreshListView.onRefreshComplete();
                HomeFragment.this.loadDataError();
            }
        }));
    }

    protected String getSliderUrl() {
        return UrlConst.getHomeSliderUrl();
    }

    protected String getHotLiveUrl() {
        return UrlConst.getHomeHotLiveUrl();
    }

    protected String getMultiCateUrl() {
        return UrlConst.getHomeMultiCateUrl();
    }

    protected boolean reLoadData() {
        loadSliderData();
        loadMultiCateData();
        return true;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        //初始化loading的视图
        initLoadingView(view);

        //初始化下拉刷新列表
        this.mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.livelist);
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(DateUtils.formatDateTime(MyApplication.getInstance().getApplicationContext(), System.currentTimeMillis(), 524305));
                HomeFragment.this.reLoadData();
            }
        });
        this.mPullRefreshListView.setMode(Mode.PULL_FROM_START);
        this.mPullRefreshListView.setScrollingWhileRefreshingEnabled(true);
        this.mAdapter = new HomeListAdapter(MyApplication.getInstance().getApplicationContext(), this.mListener);
        this.mPullRefreshListView.setAdapter(this.mAdapter);
        this.mPullRefreshListView.setEmptyView(view.findViewById(R.id.loadview));

        //发起请求，获取轮播信息以及分类信息
        reLoadData();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnHomeFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        this.mListener = null;
        super.onDetach();
    }
}