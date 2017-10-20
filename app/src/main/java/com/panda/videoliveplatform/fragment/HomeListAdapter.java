package com.panda.videoliveplatform.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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
import com.panda.videolivecore.data.MultiCateLiveItemInfo.Data;
import com.panda.videolivecore.data.SliderItemInfo;
import com.panda.videolivecore.view.HomeGridView;
import com.panda.videoliveplatform.MyApplication;
import com.panda.videoliveplatform.R;
import com.panda.videoliveplatform.fragment.HomeFragment.OnHomeFragmentListener;

import java.util.ArrayList;
import java.util.List;


public class HomeListAdapter extends BaseAdapter implements OnSliderClickListener {
    private static final int MULTICATE_ITEM = 1;
    private static final int SLIDER_ITEM = 0;
    private Context mContext;
    private Drawable mHotliveDefaultDrawable;
    private LayoutInflater mInflater;
    private OnHomeFragmentListener mListener;
    private ArrayList<Data> mMutlCateList;
    private Drawable mSliderDefaultDrawable;
    private ArrayList<SliderItemInfo> mSliderList;
    private boolean mSliderUpdateData = true;
    private List<SubCateListAdapter> mSubCateAdapterList;

    static class ViewHolderCate {
        public TextView cate_more;
        public TextView cate_name;
        public HomeGridView grid_view;

        ViewHolderCate() {
        }
    }

    static class ViewHolderSlider {
        public SliderLayout slider;

        ViewHolderSlider() {
        }
    }

    HomeListAdapter(Context context, OnHomeFragmentListener listener) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mListener = listener;
        this.mHotliveDefaultDrawable = context.getResources().getDrawable(R.drawable.defaultlivebg);
        this.mSliderDefaultDrawable = context.getResources().getDrawable(R.drawable.defaultsliderbg);
        this.mSliderList = new ArrayList();
    }

    void updateSliderList(ArrayList<SliderItemInfo> list) {
        if (list != null) {
            this.mSliderUpdateData = true;
            this.mSliderList = list;
            notifyDataSetChanged();
        }
    }

    void updateMultiCateList(ArrayList<Data> list) {
        if (list != null) {
            this.mMutlCateList = list;
            this.mSubCateAdapterList = new ArrayList(list.size());
            for (int i = 0; i < list.size(); i++) {
                SubCateListAdapter adapter = new SubCateListAdapter(this.mContext, this.mListener);
                this.mSubCateAdapterList.add(adapter);
                adapter.insertData(((Data) list.get(i)).items);
            }
            notifyDataSetChanged();
        }
    }

    public void onSliderClick(BaseSliderView slider) {
        this.mListener.onSliderClick((SliderItemInfo) this.mSliderList.get(((Integer) slider.getBundle().get("extra")).intValue()));
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderCate holder = null;
        ViewHolderSlider holderSlider = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            if (type == 0) {
                holderSlider = new ViewHolderSlider();
                convertView = this.mInflater.inflate(R.layout.fragment_home_slider_item, null);
                holderSlider.slider = (SliderLayout) convertView.findViewById(R.id.homeslideritem);
                convertView.setTag(holderSlider);
            } else if (type == 1) {
                holder = new ViewHolderCate();
                convertView = this.mInflater.inflate(R.layout.fragment_home_cate, null);
                holder.cate_name = (TextView) convertView.findViewById(R.id.cate_name);
                holder.cate_more = (TextView) convertView.findViewById(R.id.cate_more);
                holder.grid_view = (HomeGridView) convertView.findViewById(R.id.grid_view);
                convertView.setTag(holder);
            }
        } else if (type == 0) {
            holderSlider = (ViewHolderSlider) convertView.getTag();
        } else if (type == 1) {
            holder = (ViewHolderCate) convertView.getTag();
        }
        if (type == 0 && this.mSliderUpdateData) {
            this.mSliderUpdateData = false;
            holderSlider.slider.removeAllSliders();
            int i = 0;
            while (i < this.mSliderList.size()) {
                TextSliderView textSliderView = new TextSliderView(MyApplication.getInstance().getApplicationContext());
                textSliderView.description(((SliderItemInfo) this.mSliderList.get(i)).title).setScaleType(ScaleType.Fit).setOnSliderClickListener(this);
                if (((SliderItemInfo) this.mSliderList.get(i)).bigimg == null || ((SliderItemInfo) this.mSliderList.get(i)).bigimg.equals("")) {
                    textSliderView.image((int) R.drawable.defaultsliderbg);
                } else {
                    textSliderView.image(((SliderItemInfo) this.mSliderList.get(i)).bigimg);
                }
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putInt("extra", i);
                holderSlider.slider.addSlider(textSliderView);
                i++;
            }
            holderSlider.slider.setPresetIndicator(PresetIndicators.Right_Bottom);
            holderSlider.slider.setDuration(3000L);
        } else if (type == 1) {
            int index = position - 1;
            final Data cate_data = (Data) this.mMutlCateList.get(index);
            holder.cate_name.setText(cate_data.type.cname);
            holder.cate_more.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (HomeListAdapter.this.mListener != null) {
                        //HomeListAdapter.this.mListener.onOpenSubLiveActivity(cate_data.type);
                    }
                }
            });
            holder.grid_view.setAdapter((ListAdapter) this.mSubCateAdapterList.get(index));
        }
        return convertView;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public int getViewTypeCount() {
        return 2;
    }

    public int getCount() {
        //这里的逻辑是slider和下面的分类列表都不为0，才能显示首页
        if (this.mSliderList == null) {
            return 0;
        }

        if (this.mSliderList.size() == 0) {
            return 0;
        }
        if (null == mMutlCateList) {
            return 0;
        }
        if (mMutlCateList.size() == 0) {
            return 0;
        }
        //整个slider算做一个item，下面的每个分类算做一个item，返回是分类的数量加1
        return mMutlCateList.size() + 1;

    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }
}