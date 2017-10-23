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
import com.panda.videolivecore.data.ColumnLiveItemInfo.Data;
import com.panda.videolivecore.data.ColumnLiveItemInfo.ResponseData;
import com.panda.videolivecore.data.GsonRequest;
import com.panda.videolivecore.net.UrlConst;
import com.panda.videoliveplatform.MyApplication;
import com.panda.videoliveplatform.R;
import com.panda.videoliveplatform.fragment.ColumnLiveListAdapter.OnColumnLiveItemListener;

public class ColumnLiveFragment extends BaseFragment implements OnColumnLiveItemListener {
    private ColumnLiveListAdapter mAdapter;
    private Context mContext;
    private boolean mIsLoadingData = false;
    private OnColumnLiveFragmentListener mListener;
    private int mPage = 1;
    private PullToRefreshGridView mPullRefreshGridView;
    private int mTotalItem = 0;

    public interface OnColumnLiveFragmentListener {
        void onColumnLiveItemClick(Data data);
    }

    public static ColumnLiveFragment newInstance(Context context) {
        ColumnLiveFragment fragment = new ColumnLiveFragment();
        fragment.setParams(context);
        return fragment;
    }

    public void setParams(Context context) {
        this.mContext = context;
    }

    public void onColumnLiveItemClick(Data data) {
        this.mListener.onColumnLiveItemClick(data);
    }

    protected void loadData(int page) {
        final boolean isRefreshFromTop = this.mPage == 1;
        this.mIsLoadingData = true;
        executeRequest(new GsonRequest(getUrl(page), ResponseData.class, new Listener<ResponseData>() {
            public void onResponse(ResponseData response) {
                if (response.errno == 0) {
                    try {
                        ColumnLiveFragment.this.mTotalItem = response.data.size();
                    } catch (NumberFormatException e) {
                    }
                    if (isRefreshFromTop) {
                        ColumnLiveFragment.this.mAdapter.deleteAllData();
                    }
                    ColumnLiveFragment.this.mPage = ColumnLiveFragment.this.mPage + 1;
                    ColumnLiveFragment.this.mAdapter.insertData(response.data);
                    ColumnLiveFragment.this.loadDataSuccess();
                    ColumnLiveFragment.this.loadCompelete(ColumnLiveFragment.this.mTotalItem);
                }
                ColumnLiveFragment.this.mPullRefreshGridView.onRefreshComplete();
                if (response.errno != 0) {
                    ColumnLiveFragment.this.loadDataError();
                }
                ColumnLiveFragment.this.mIsLoadingData = false;
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                ColumnLiveFragment.this.mPullRefreshGridView.onRefreshComplete();
                ColumnLiveFragment.this.loadDataError();
                ColumnLiveFragment.this.mIsLoadingData = false;
            }
        }));
    }

    protected String getUrl(int page) {
        return UrlConst.getColumnLiveUrl();
    }

    protected boolean reLoadData() {
        loadData(1);
        return true;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_column_live, null);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        initLoadingView(view);
        this.mPullRefreshGridView = (PullToRefreshGridView) view.findViewById(R.id.livelist);
        this.mPullRefreshGridView.setOnRefreshListener(new OnRefreshListener<GridView>() {
            public void onRefresh(PullToRefreshBase<GridView> refreshView) {
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(DateUtils.formatDateTime(MyApplication.getInstance().getApplicationContext(), System.currentTimeMillis(), 524305));
                if (!ColumnLiveFragment.this.mIsLoadingData) {
                    ColumnLiveFragment.this.mPage = 1;
                    ColumnLiveFragment.this.reLoadData();
                }
            }
        });
        this.mPullRefreshGridView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount && ColumnLiveFragment.this.mAdapter.getItemCount() < ColumnLiveFragment.this.mTotalItem && !ColumnLiveFragment.this.mIsLoadingData) {
                    ColumnLiveFragment.this.loadData(ColumnLiveFragment.this.mPage);
                }
            }
        });
        this.mPullRefreshGridView.setMode(Mode.PULL_FROM_START);
        this.mPullRefreshGridView.setScrollingWhileRefreshingEnabled(true);
        this.mAdapter = new ColumnLiveListAdapter(MyApplication.getInstance().getApplicationContext(), this);
        this.mPullRefreshGridView.setAdapter(this.mAdapter);
        this.mPullRefreshGridView.setEmptyView(view.findViewById(R.id.loadview));
        reLoadData();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnColumnLiveFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }
}
