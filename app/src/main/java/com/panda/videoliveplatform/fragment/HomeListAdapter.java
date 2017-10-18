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
        this.mHotliveDefaultDrawable = paramContext.getResources().getDrawable(2130837587);
        this.mSliderDefaultDrawable = paramContext.getResources().getDrawable(2130837588);
        this.mSliderList = new ArrayList();
    }

    public int getCount() {
        if ((this.mSliderList == null) || (this.mSliderList.size() == 0) || (this.mMutlCateList == null) || (this.mMutlCateList.size() == 0))
            ;
        for (int i = 0; ; i = 1 + this.mMutlCateList.size())
            return i;
    }

    public Object getItem(int paramInt) {
        return Integer.valueOf(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public int getItemViewType(int paramInt) {
        if (paramInt == 0) ;
        for (int i = 0; ; i = 1)
            return i;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {

        return paramView;

//    ViewHolderCate localViewHolderCate = null;
//    int i = getItemViewType(paramInt);
//    ViewHolderSlider localViewHolderSlider;
//    int k;
//    //label87: TextSliderView localTextSliderView;
//    TextSliderView localTextSliderView;
//    if (paramView == null)
//      if (i == 0)
//      {
//        localViewHolderSlider = new ViewHolderSlider();
//        paramView = this.mInflater.inflate(2130903074, null);
//        localViewHolderSlider.slider = ((SliderLayout)paramView.findViewById(2131362002));
//        paramView.setTag(localViewHolderSlider);
//        if ((i != 0) || (!this.mSliderUpdateData))
//          break label416;
//        this.mSliderUpdateData = false;
//        localViewHolderSlider.slider.removeAllSliders();
//        k = 0;
//        if (k >= this.mSliderList.size())
//          break label392;
//        localTextSliderView = new TextSliderView(MyApplication.getInstance().getApplicationContext());
//        localTextSliderView.description(((SliderItemInfo)this.mSliderList.get(k)).title).setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(this);
//        if ((((SliderItemInfo)this.mSliderList.get(k)).bigimg != null) && (!((SliderItemInfo)this.mSliderList.get(k)).bigimg.equals("")))
//          break label368;
//        localTextSliderView.image(2130837588);
//      }
//    while (true)
//    {
//      localTextSliderView.bundle(new Bundle());
//      localTextSliderView.getBundle().putInt("extra", k);
//      localViewHolderSlider.slider.addSlider(localTextSliderView);
//      k++;
//      break label87;
//      localViewHolderCate = null;
//      localViewHolderSlider = null;
//      if (i != 1)
//        break;
//      localViewHolderCate = new ViewHolderCate();
//      paramView = this.mInflater.inflate(2130903073, null);
//      localViewHolderCate.cate_name = ((TextView)paramView.findViewById(2131361999));
//      localViewHolderCate.cate_more = ((TextView)paramView.findViewById(2131362000));
//      localViewHolderCate.grid_view = ((HomeGridView)paramView.findViewById(2131362001));
//      paramView.setTag(localViewHolderCate);
//      localViewHolderSlider = null;
//      break;
//      if (i == 0)
//      {
//        localViewHolderSlider = (ViewHolderSlider)paramView.getTag();
//        localViewHolderCate = null;
//        break;
//      }
//      localViewHolderCate = null;
//      localViewHolderSlider = null;
//      if (i != 1)
//        break;
//      localViewHolderCate = (ViewHolderCate)paramView.getTag();
//      localViewHolderSlider = null;
//      break;
//      label368: localTextSliderView.image(((SliderItemInfo)this.mSliderList.get(k)).bigimg);
//    }
//    label392: localViewHolderSlider.slider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
//    localViewHolderSlider.slider.setDuration(3000L);
//    while (true)
//    {
//      return paramView;
//      label416: if (i == 1)
//      {
//        int j = paramInt - 1;
//        final MultiCateLiveItemInfo.Data localData = (MultiCateLiveItemInfo.Data)this.mMutlCateList.get(j);
//        localViewHolderCate.cate_name.setText(localData.type.cname);
//        localViewHolderCate.cate_more.setOnClickListener(new View.OnClickListener()
//        {
//          public void onClick(View paramAnonymousView)
//          {
//            if (HomeListAdapter.this.mListener != null)
//              HomeListAdapter.this.mListener.onOpenSubLiveActivity(localData.type);
//          }
//        });
//        localViewHolderCate.grid_view.setAdapter((ListAdapter)this.mSubCateAdapterList.get(j));
//      }
//    }
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
