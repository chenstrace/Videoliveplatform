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
import com.panda.videoliveplatform.fragment.SearchLiveCloseAdapter.OnSearchLiveCloseItemListener;

public class SearchLiveCloseFragment extends BaseFragment implements OnSearchLiveCloseItemListener {
    private SearchLiveCloseAdapter mAdapter;
    private Context mContext;
    private View mEmptyLoadView;
    private boolean mIsLoadingData = false;
    private OnSearchLiveCloseFragmentListener mListener;
    private int mPage = 1;
    private PullToRefreshGridView mPullRefreshGridView;
    private SearchResultListener mResultListener;
    private String mSearchKeyword;
    private int mTotalItem = 0;

    public interface OnSearchLiveCloseFragmentListener {
        void onSearchLiveCloseItemClick(SearchItemInfo searchItemInfo);
    }

    public static SearchLiveCloseFragment newInstance(Context context, SearchResultListener resultListener) {
        SearchLiveCloseFragment fragment = new SearchLiveCloseFragment();
        fragment.setParams(context, resultListener);
        return fragment;
    }

    public void setParams(Context context, SearchResultListener resultListener) {
        this.mContext = context;
        this.mResultListener = resultListener;
    }

    public void onSearchLiveCloseItemClick(SearchItemInfo info) {
        this.mListener.onSearchLiveCloseItemClick(info);
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
                if (response.errno == 0) {
                    try {
                        SearchLiveCloseFragment.this.mTotalItem = Integer.parseInt(response.data.total);
                    } catch (NumberFormatException e) {
                    }
                    if (isRefreshFromTop) {
                        SearchLiveCloseFragment.this.mAdapter.deleteAllData();
                    }
                    SearchLiveCloseFragment.this.mPage = SearchLiveCloseFragment.this.mPage + 1;
                    SearchLiveCloseFragment.this.mAdapter.insertData(response.data.items);
                    SearchLiveCloseFragment.this.loadDataSuccess();
                    SearchLiveCloseFragment.this.loadCompelete(SearchLiveCloseFragment.this.mTotalItem);
                    SearchLiveCloseFragment.this.mIsLoadingData = false;
                }
                SearchLiveCloseFragment.this.mPullRefreshGridView.onRefreshComplete();
                SearchLiveCloseFragment.this.mResultListener.SearchResult(true);
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                SearchLiveCloseFragment.this.mPullRefreshGridView.onRefreshComplete();
                SearchLiveCloseFragment.this.loadDataError();
                SearchLiveCloseFragment.this.mIsLoadingData = false;
                SearchLiveCloseFragment.this.mResultListener.SearchResult(false);
            }
        }));
        return true;
    }

    protected String getUrl(int page) {
        return UrlConst.getSearchLiveCloseUrl(HttpRequest.URLEncoder(this.mSearchKeyword), page);
    }

    protected boolean reLoadData() {
        return loadData(1);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow_live_close, null);
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
                if (!SearchLiveCloseFragment.this.mIsLoadingData) {
                    SearchLiveCloseFragment.this.mPage = 1;
                    SearchLiveCloseFragment.this.reLoadData();
                }
            }
        });
        this.mPullRefreshGridView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount && SearchLiveCloseFragment.this.mAdapter.getItemCount() < SearchLiveCloseFragment.this.mTotalItem && !SearchLiveCloseFragment.this.mIsLoadingData) {
                    SearchLiveCloseFragment.this.loadData(SearchLiveCloseFragment.this.mPage);
                }
            }
        });
        this.mPullRefreshGridView.setMode(Mode.PULL_FROM_START);
        this.mPullRefreshGridView.setScrollingWhileRefreshingEnabled(true);
        this.mAdapter = new SearchLiveCloseAdapter(MyApplication.getInstance().getApplicationContext(), this);
        this.mPullRefreshGridView.setAdapter(this.mAdapter);
        this.mEmptyLoadView = view.findViewById(R.id.loadview);
        this.mEmptyLoadView.setVisibility(4);
        view.findViewById(R.id.emptyview).setVisibility(4);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnSearchLiveCloseFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }
}
