package com.panda.videoliveplatform.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.panda.videolivecore.Interface.SearchResultListener;
import com.panda.videolivecore.data.GsonRequest;
import com.panda.videolivecore.data.SearchItemInfo;
import com.panda.videolivecore.data.SearchItemInfo.ResponseData;
import com.panda.videolivecore.net.UrlConst;
import com.panda.videolivecore.net.http.HttpRequest;
import com.panda.videolivecore.utils.CookieContiner;
import com.panda.videoliveplatform.MyApplication;
import com.panda.videoliveplatform.R;
import com.panda.videoliveplatform.fragment.SearchLiveOpenAdapter.OnSearchLiveOpenItemListener;

public class SearchLiveOpenFragment extends BaseFragment implements OnSearchLiveOpenItemListener {
    private SearchLiveOpenAdapter mAdapter;
    private Context mContext;
    private View mEmptyLoadView;
    private boolean mIsLoadingData = false;
    private OnSearchLiveOpenFragmentListener mListener;
    private int mPage = 1;
    private PullToRefreshGridView mPullRefreshGridView;
    private SearchResultListener mResultListener;
    private String mSearchKeyword;
    private int mTotalItem = 0;

    public interface OnSearchLiveOpenFragmentListener {
        void onSearchLiveOpenItemClick(SearchItemInfo searchItemInfo);
    }

    public static SearchLiveOpenFragment newInstance(Context context, SearchResultListener resultListener) {
        SearchLiveOpenFragment fragment = new SearchLiveOpenFragment();
        fragment.setParams(context, resultListener);
        return fragment;
    }

    public void setParams(Context context, SearchResultListener resultListener) {
        this.mContext = context;
        this.mResultListener = resultListener;
    }

    public void onSearchLiveOpenItemClick(SearchItemInfo info) {
        this.mListener.onSearchLiveOpenItemClick(info);
    }

    protected boolean loadData(int page) {
        if (this.mPullRefreshGridView == null) {
            return false;
        }
        if (TextUtils.isEmpty(this.mSearchKeyword)) {
            this.mPullRefreshGridView.onRefreshComplete();
            return false;
        }
        final boolean isRefreshFromTop;
        this.mPullRefreshGridView.setVisibility(0);
        this.mPullRefreshGridView.setEmptyView(this.mEmptyLoadView);
        if (this.mPage == 1) {
            isRefreshFromTop = true;
        } else {
            isRefreshFromTop = false;
        }
        this.mIsLoadingData = true;
        startLoading();
        executeRequest(new GsonRequest(0, getUrl(page), ResponseData.class, CookieContiner.getCookieHeaderMap(), new Listener<ResponseData>() {
            public void onResponse(ResponseData response) {
                try {
                    SearchLiveOpenFragment.this.mTotalItem = Integer.parseInt(response.data.total);
                } catch (NumberFormatException e) {
                }
                if (response.errno == 0) {
                    if (isRefreshFromTop) {
                        SearchLiveOpenFragment.this.mAdapter.deleteAllData();
                    }
                    SearchLiveOpenFragment.this.mPage = SearchLiveOpenFragment.this.mPage + 1;
                    SearchLiveOpenFragment.this.mAdapter.insertData(response.data.items);
                    SearchLiveOpenFragment.this.loadDataSuccess();
                    SearchLiveOpenFragment.this.loadCompelete(SearchLiveOpenFragment.this.mTotalItem);
                    SearchLiveOpenFragment.this.mIsLoadingData = false;
                }
                SearchLiveOpenFragment.this.mPullRefreshGridView.onRefreshComplete();
                SearchLiveOpenFragment.this.mResultListener.SearchResult(true);
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                SearchLiveOpenFragment.this.mPullRefreshGridView.onRefreshComplete();
                SearchLiveOpenFragment.this.loadDataError();
                SearchLiveOpenFragment.this.mIsLoadingData = false;
                SearchLiveOpenFragment.this.mResultListener.SearchResult(false);
            }
        }));
        return true;
    }

    protected String getUrl(int page) {
        return UrlConst.getSearchLiveOpenUrl(HttpRequest.URLEncoder(this.mSearchKeyword), page);
    }

    protected boolean reLoadData() {
        return loadData(1);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow_live_open, null);
        initViews(view);
        return view;
    }

    public boolean search(String keyword) {
        this.mSearchKeyword = keyword;
        this.mPage = 1;
        return reLoadData();
    }

    private void initViews(View view) {
        initLoadingView(view);
        this.mPullRefreshGridView = (PullToRefreshGridView) view.findViewById(R.id.livelist);
        this.mPullRefreshGridView.setOnRefreshListener(new OnRefreshListener<GridView>() {
            public void onRefresh(PullToRefreshBase<GridView> refreshView) {
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(DateUtils.formatDateTime(MyApplication.getInstance().getApplicationContext(), System.currentTimeMillis(), 524305));
                if (!SearchLiveOpenFragment.this.mIsLoadingData) {
                    SearchLiveOpenFragment.this.mPage = 1;
                    SearchLiveOpenFragment.this.reLoadData();
                }
            }
        });
        this.mPullRefreshGridView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount && SearchLiveOpenFragment.this.mAdapter.getItemCount() < SearchLiveOpenFragment.this.mTotalItem && !SearchLiveOpenFragment.this.mIsLoadingData) {
                    SearchLiveOpenFragment.this.loadData(SearchLiveOpenFragment.this.mPage);
                }
            }
        });
        this.mPullRefreshGridView.setMode(Mode.PULL_FROM_START);
        this.mPullRefreshGridView.setScrollingWhileRefreshingEnabled(true);
        this.mAdapter = new SearchLiveOpenAdapter(MyApplication.getInstance().getApplicationContext(), this);
        this.mPullRefreshGridView.setAdapter(this.mAdapter);
        this.mEmptyLoadView = view.findViewById(R.id.loadview);
        this.mEmptyLoadView.setVisibility(4);
        view.findViewById(R.id.emptyview).setVisibility(4);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnSearchLiveOpenFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }
}
