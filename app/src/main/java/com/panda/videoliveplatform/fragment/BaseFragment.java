package com.panda.videoliveplatform.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import com.android.volley.Request;
import com.panda.videoliveplatform.R;
import com.panda.videolivecore.data.RequestManager;

public abstract class BaseFragment extends Fragment
{
  private View mLoadCompeleteNodataView;
  private View mLoadErrorView;
  private View mLoadingView;

  protected void executeRequest(Request paramRequest)
  {
    RequestManager.addRequest(paramRequest, this);
  }

  protected void initLoadingView(View paramView)
  {
    this.mLoadingView = paramView.findViewById(R.id.loading);
    this.mLoadErrorView = paramView.findViewById(R.id.loaderror);
    this.mLoadCompeleteNodataView = paramView.findViewById(R.id.loadsuccess_nodata);
    this.mLoadErrorView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        BaseFragment.this.mLoadingView.setVisibility(0);
        BaseFragment.this.mLoadErrorView.setVisibility(8);
        BaseFragment.this.mLoadCompeleteNodataView.setVisibility(8);
        BaseFragment.this.reLoadData();
      }
    });
  }

  protected void loadCompelete(int paramInt)
  {
    if (paramInt <= 0)
    {
      this.mLoadErrorView.setVisibility(8);
      this.mLoadingView.setVisibility(8);
      this.mLoadCompeleteNodataView.setVisibility(0);
    }
  }

  protected void loadDataError()
  {
    this.mLoadErrorView.setVisibility(0);
    this.mLoadingView.setVisibility(8);
    this.mLoadCompeleteNodataView.setVisibility(8);
  }

  protected void loadDataSuccess()
  {
  }

  public void onDestroy()
  {
    super.onDestroy();
    RequestManager.cancelAll(this);
  }

  protected abstract boolean reLoadData();

  protected void startLoading()
  {
    this.mLoadErrorView.setVisibility(8);
    this.mLoadingView.setVisibility(0);
    this.mLoadCompeleteNodataView.setVisibility(8);
  }
}

