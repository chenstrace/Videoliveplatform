package com.panda.videoliveplatform.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.panda.videolivecore.data.ImageCacheManager;
import com.panda.videolivecore.data.SearchItemInfo;
import com.panda.videolivecore.net.NumericUtils;
import com.panda.videolivecore.view.ScaleImageView;
import com.panda.videoliveplatform.R;
import java.util.ArrayList;

public class SearchLiveCloseAdapter extends BaseAdapter {
    private Drawable mDefaultDrawable;
    private LayoutInflater mInflater;
    private ArrayList<SearchItemInfo> mList = new ArrayList();
    private OnSearchLiveCloseItemListener mListener;

    public interface OnSearchLiveCloseItemListener {
        void onSearchLiveCloseItemClick(SearchItemInfo searchItemInfo);
    }

    static class ViewHolder {
        public TextView fansCount;
        public ScaleImageView imgView;
        public View liveitem;
        public TextView nickName;
        public TextView titleView;

        ViewHolder() {
        }
    }

    SearchLiveCloseAdapter(Context context, OnSearchLiveCloseItemListener listener) {
        this.mListener = listener;
        this.mInflater = LayoutInflater.from(context);
        this.mDefaultDrawable = context.getResources().getDrawable(R.drawable.defaultlivebg);
    }

    public int getItemCount() {
        return this.mList.size();
    }

    void deleteAllData() {
        this.mList.clear();
    }

    void insertData(ArrayList<SearchItemInfo> list) {
        if (list != null) {
            this.mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public int getCount() {
        return this.mList.size();
    }

    public SearchItemInfo getItem(int position) {
        return (SearchItemInfo) this.mList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.mInflater.inflate(R.layout.fragment_live_item, null);
            holder.liveitem = convertView.findViewById(R.id.liveitem);
            holder.imgView = (ScaleImageView) convertView.findViewById(R.id.liveitemimg);
            holder.imgView.setScaleValue(0.5636f);
            holder.titleView = (TextView) convertView.findViewById(R.id.liveitemroomname);
            holder.nickName = (TextView) convertView.findViewById(R.id.liveitemusername);
            holder.fansCount = (TextView) convertView.findViewById(R.id.liveitemfanscount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SearchItemInfo items = getItem(position);
        holder.titleView.setText(items.name);
        holder.liveitem.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SearchLiveCloseAdapter.this.mListener.onSearchLiveCloseItemClick(items);
            }
        });
        if (items.nickname != null) {
            holder.nickName.setText(items.nickname);
        }
        holder.fansCount.setText(NumericUtils.getLivePeople(items.person_num));
        if (items.pictures.img != null) {
            ImageCacheManager.loadImage(items.pictures.img, ImageCacheManager.getImageListener(holder.imgView, this.mDefaultDrawable, this.mDefaultDrawable));
        }
        return convertView;
    }
}
