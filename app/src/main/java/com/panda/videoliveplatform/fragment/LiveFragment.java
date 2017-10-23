package com.panda.videoliveplatform.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.panda.videolivecore.data.GsonRequest;
import com.panda.videolivecore.data.LiveItemInfo;
import com.panda.videolivecore.data.LiveItemInfo.ResponseData;
import com.panda.videolivecore.net.UrlConst;
import com.panda.videoliveplatform.MyApplication;
import com.panda.videoliveplatform.R;
import com.panda.videoliveplatform.fragment.LiveListAdapter.OnLiveItemListener;

public class LiveFragment extends BaseFragment implements OnLiveItemListener {
    private LiveListAdapter mAdapter;
    private Context mContext;
    private boolean mIsLoadingData = false;
    private OnLiveFragmentListener mListener;
    private int mPage = 1;
    private PullToRefreshGridView mPullRefreshGridView;
    private int mTotalItem = 0;

    public interface OnLiveFragmentListener {
        void onLiveItemClick(LiveItemInfo liveItemInfo);
    }

    public static LiveFragment newInstance(Context context) {
        LiveFragment fragment = new LiveFragment();
        fragment.setParams(context);
        return fragment;
    }

    public void setParams(Context context) {
        this.mContext = context;
    }

    public void onLiveItemClick(LiveItemInfo info) {
        this.mListener.onLiveItemClick(info);
    }

    protected void loadData(int page) {
        final boolean isRefreshFromTop = this.mPage == 1;
        this.mIsLoadingData = true;
        executeRequest(new GsonRequest(getUrl(page), ResponseData.class, new Listener<ResponseData>() {
            public void onResponse(ResponseData response) {
                if (response.errno == 0) {
                    try {
                        LiveFragment.this.mTotalItem = Integer.parseInt(response.data.total);
                    } catch (NumberFormatException e) {
                    }
                    if (isRefreshFromTop) {
                        LiveFragment.this.mAdapter.deleteAllData();
                    }
                    LiveFragment.this.mPage = LiveFragment.this.mPage + 1;
                    LiveFragment.this.mAdapter.insertData(response.data.items);
                    LiveFragment.this.loadDataSuccess();
                    LiveFragment.this.loadCompelete(LiveFragment.this.mTotalItem);
                }
                LiveFragment.this.mPullRefreshGridView.onRefreshComplete();
                if (response.errno != 0) {
                    LiveFragment.this.loadDataError();
                }
                LiveFragment.this.mIsLoadingData = false;
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                LiveFragment.this.mPullRefreshGridView.onRefreshComplete();
                LiveFragment.this.loadDataError();
                LiveFragment.this.mIsLoadingData = false;
            }
        }));
    }

    protected String getUrl(int page) {
        return UrlConst.getAllLiveListUrl(page);
    }

    protected boolean reLoadData() {
        loadData(1);
        return true;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live, null);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        initLoadingView(view);
        this.mPullRefreshGridView = (PullToRefreshGridView) view.findViewById(R.id.livelist);
        this.mPullRefreshGridView.setOnRefreshListener(new OnRefreshListener<GridView>() {
            public void onRefresh(PullToRefreshBase<GridView> refreshView) {
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(DateUtils.formatDateTime(MyApplication.getInstance().getApplicationContext(), System.currentTimeMillis(), 524305));
                if (!LiveFragment.this.mIsLoadingData) {
                    LiveFragment.this.mPage = 1;
                    LiveFragment.this.reLoadData();
                }
            }
        });
        this.mPullRefreshGridView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount && LiveFragment.this.mAdapter.getItemCount() < LiveFragment.this.mTotalItem && !LiveFragment.this.mIsLoadingData) {
                    LiveFragment.this.loadData(LiveFragment.this.mPage);
                }
            }
        });
        this.mPullRefreshGridView.setMode(Mode.PULL_FROM_START);
        this.mPullRefreshGridView.setScrollingWhileRefreshingEnabled(true);
        this.mAdapter = new LiveListAdapter(MyApplication.getInstance().getApplicationContext(), this);
        this.mPullRefreshGridView.setAdapter(this.mAdapter);
        this.mPullRefreshGridView.setEmptyView(view.findViewById(R.id.loadview));
        reLoadData();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnLiveFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public void ScrollToTop() {
        if (this.mPullRefreshGridView != null) {
            this.mPullRefreshGridView.setRefreshing();
        }
    }
}
