package com.panda.videoliveplatform.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.panda.videolivecore.data.ImageCacheManager;
import com.panda.videolivecore.data.SubLiveItemInfo;
import com.panda.videolivecore.net.NumericUtils;
import com.panda.videolivecore.view.ScaleImageView;
import com.panda.videoliveplatform.R;
import java.util.ArrayList;

public class SubLiveListAdapter extends BaseAdapter {
    private Drawable mDefaultDrawable;
    private LayoutInflater mInflater;
    private ArrayList<SubLiveItemInfo> mList = new ArrayList();
    private OnSubLiveItemListener mListener;

    public interface OnSubLiveItemListener {
        void onSubLiveItemClick(SubLiveItemInfo subLiveItemInfo);
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

    SubLiveListAdapter(Context context, OnSubLiveItemListener listener) {
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

    void insertData(ArrayList<SubLiveItemInfo> list) {
        if (list != null) {
            this.mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public int getCount() {
        return this.mList.size();
    }

    public SubLiveItemInfo getItem(int position) {
        return (SubLiveItemInfo) this.mList.get(position);
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
        final SubLiveItemInfo items = getItem(position);
        holder.titleView.setText(items.name);
        holder.liveitem.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SubLiveListAdapter.this.mListener.onSubLiveItemClick(items);
            }
        });
        if (!(items == null || items.userinfo == null || items.userinfo.nickName == null)) {
            holder.nickName.setText(items.userinfo.nickName);
        }
        holder.fansCount.setText(NumericUtils.getLivePeople(items.person_num));
        if (!(items.pictures.img == null || items.pictures.img.equals(""))) {
            ImageCacheManager.loadImage(items.pictures.img, ImageCacheManager.getImageListener(holder.imgView, this.mDefaultDrawable, this.mDefaultDrawable));
        }
        return convertView;
    }
}
