package com.panda.videoliveplatform.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderLayout.PresetIndicators;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.daimajia.slider.library.SliderTypes.BaseSliderView.ScaleType;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.panda.videolivecore.data.MultiCateLiveItemInfo;
import com.panda.videolivecore.data.MultiCateLiveItemInfo.Data;
import com.panda.videolivecore.data.MultiCateLiveItemInfo.SubType;
import com.panda.videolivecore.data.SliderItemInfo;
import com.panda.videolivecore.view.HomeGridView;
import com.panda.videoliveplatform.MyApplication;
import com.panda.videoliveplatform.R;

import java.util.ArrayList;
import java.util.List;

public class HomeListAdapter extends BaseAdapter
        implements BaseSliderView.OnSliderClickListener {
    private static final int MULTICATE_ITEM = 1;
    //  private static final int SLIDER_ITEM;
    private Context mContext;
    private Drawable mHotliveDefaultDrawable;
    private LayoutInflater mInflater;
    private HomeFragment.OnHomeFragmentListener mListener;
    private ArrayList<MultiCateLiveItemInfo.Data> mMutlCateList;
    private Drawable mSliderDefaultDrawable;
    private ArrayList<SliderItemInfo> mSliderList;
    private boolean mSliderUpdateData = true;
    // private List<SubCateListAdapter> mSubCateAdapterList;

    HomeListAdapter(Context paramContext, HomeFragment.OnHomeFragmentListener paramOnHomeFragmentListener) {
        this.mContext = paramContext;
        this.mInflater = LayoutInflater.from(paramContext);
        this.mListener = paramOnHomeFragmentListener;
        this.mHotliveDefaultDrawable = paramContext.getResources().getDrawable(R.drawable.defaultlivebg);
        this.mSliderDefaultDrawable = paramContext.getResources().getDrawable(R.drawable.defaultsliderbg);
        this.mSliderList = new ArrayList();
    }

    public int getCount() {
        if ((this.mSliderList == null) || (this.mSliderList.size() == 0) || (this.mMutlCateList == null) || (this.mMutlCateList.size() == 0))
            return 0;
        return this.mSliderList.size();
    }

    public Object getItem(int paramInt) {
        return (SliderItemInfo)this.mSliderList.get(paramInt);

    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public int getItemViewType(int paramInt) {
        return 0;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {

        ViewHolderCate localViewHolderCate = null;
        int i = getItemViewType(paramInt);
        ViewHolderSlider localViewHolderSlider;
        int k= paramInt;
        //label87: TextSliderView localTextSliderView;
        TextSliderView localTextSliderView;
        if (paramView == null)
        {
            if (i == 0) {
                localViewHolderSlider = new ViewHolderSlider();
                paramView = this.mInflater.inflate(R.layout.fragment_home_slider_item, null);
                localViewHolderSlider.slider = ((SliderLayout) paramView.findViewById(R.id.homeslideritem));
                paramView.setTag(localViewHolderSlider);
                this.mSliderUpdateData = false;
                localViewHolderSlider.slider.removeAllSliders();

                localTextSliderView = new TextSliderView(MyApplication.getInstance().getApplicationContext());
                localTextSliderView.description(((SliderItemInfo) this.mSliderList.get(k)).title).setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(this);
                if ((( this.mSliderList.get(k)).bigimg != null) && (!( this.mSliderList.get(k)).bigimg.equals("")))
                {
                    localTextSliderView.image(R.drawable.defaultsliderbg);
                }
                else
                {
                    localTextSliderView.image(( this.mSliderList.get(k)).bigimg);
                }

                localTextSliderView.bundle(new Bundle());
                localTextSliderView.getBundle().putInt("extra", k);
                localViewHolderSlider.slider.addSlider(localTextSliderView);
                localViewHolderSlider.slider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
                localViewHolderSlider.slider.setDuration(3000L);
            }
        }
        return paramView;

    }

    public int getViewTypeCount() {
        return 2;
    }

    public boolean isEnabled(int paramInt) {
        return super.isEnabled(paramInt);
    }

    public void onSliderClick(BaseSliderView paramBaseSliderView) {
        //this.mListener.onSliderClick((SliderItemInfo)this.mSliderList.get(((Integer)paramBaseSliderView.getBundle().get("extra")).intValue()));
    }

    void updateMultiCateList(ArrayList<MultiCateLiveItemInfo.Data> paramArrayList) {
    /*
    if (paramArrayList != null)
    {
      this.mMutlCateList = paramArrayList;
      this.mSubCateAdapterList = new ArrayList(paramArrayList.size());
      for (int i = 0; i < paramArrayList.size(); i++)
      {
        SubCateListAdapter localSubCateListAdapter = new SubCateListAdapter(this.mContext, this.mListener);
        this.mSubCateAdapterList.add(localSubCateListAdapter);
        localSubCateListAdapter.insertData(((MultiCateLiveItemInfo.Data)paramArrayList.get(i)).items);
      }
      notifyDataSetChanged();
    }
    */
    }

    void updateSliderList(ArrayList<SliderItemInfo> paramArrayList) {
        if (paramArrayList != null) {
            this.mSliderUpdateData = true;
            this.mSliderList = paramArrayList;
            notifyDataSetChanged();
        }
    }

    static class ViewHolderCate {
        public TextView cate_more;
        public TextView cate_name;
        public HomeGridView grid_view;
    }

    static class ViewHolderSlider {
        public SliderLayout slider;
    }
}
