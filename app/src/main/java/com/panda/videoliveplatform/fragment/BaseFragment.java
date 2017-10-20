package com.panda.videoliveplatform.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.volley.Request;
import com.panda.videolivecore.data.RequestManager;
import com.panda.videoliveplatform.R;

public abstract class BaseFragment extends Fragment {
    private View mLoadCompeleteNodataView;
    private View mLoadErrorView;
    private View mLoadingView;

    protected abstract boolean reLoadData();

    public void onDestroy() {
        super.onDestroy();
        RequestManager.cancelAll(this);
    }

    protected void executeRequest(Request request) {
        RequestManager.addRequest(request, this);
    }

    protected void initLoadingView(View view) {
        this.mLoadingView = view.findViewById(R.id.loading);
        this.mLoadErrorView = view.findViewById(R.id.loaderror);
        this.mLoadCompeleteNodataView = view.findViewById(R.id.loadsuccess_nodata);
        this.mLoadErrorView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BaseFragment.this.mLoadingView.setVisibility(View.VISIBLE);
                BaseFragment.this.mLoadErrorView.setVisibility(View.GONE);
                BaseFragment.this.mLoadCompeleteNodataView.setVisibility(View.GONE);
                BaseFragment.this.reLoadData();
            }
        });
    }

    protected void loadDataSuccess() {
    }

    protected void startLoading() {
        this.mLoadErrorView.setVisibility(8);
        this.mLoadingView.setVisibility(0);
        this.mLoadCompeleteNodataView.setVisibility(8);
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