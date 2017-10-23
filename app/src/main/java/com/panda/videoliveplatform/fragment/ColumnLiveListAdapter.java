package com.panda.videoliveplatform.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.panda.videolivecore.data.ColumnLiveItemInfo.Data;
import com.panda.videolivecore.data.ImageCacheManager;
import com.panda.videolivecore.view.ScaleImageView;
import com.panda.videoliveplatform.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ColumnLiveListAdapter extends BaseAdapter {
    private Drawable mDefaultDrawable;
    private LayoutInflater mInflater;
    private ArrayList<Data> mList = new ArrayList();
    private OnColumnLiveItemListener mListener;

    public interface OnColumnLiveItemListener {
        void onColumnLiveItemClick(Data data);
    }

    static class ViewHolder {
        public ScaleImageView imgView;
        public View liveitem;
        public TextView titleView;

        ViewHolder() {
        }
    }

    ColumnLiveListAdapter(Context context, OnColumnLiveItemListener listener) {
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

    void insertData(ArrayList<Data> data) {
        if (data != null) {
            this.mList.addAll(data);
            Collections.sort(this.mList, new Comparator<Data>() {
                public int compare(Data lhs, Data rhs) {
                    try {
                        return Integer.parseInt(lhs.ext) - Integer.parseInt(rhs.ext);
                    } catch (Exception e) {
                        return 0;
                    }
                }
            });
            notifyDataSetChanged();
        }
    }

    public int getCount() {
        return this.mList.size();
    }

    public Data getItem(int position) {
        return (Data) this.mList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.mInflater.inflate(R.layout.fragment_column_live_item, null);
            holder.liveitem = convertView.findViewById(R.id.liveitem);
            holder.imgView = (ScaleImageView) convertView.findViewById(R.id.liveitemimg);
            holder.imgView.setScaleValue(1.383f);
            holder.titleView = (TextView) convertView.findViewById(R.id.liveitemroomname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Data items = getItem(position);
        holder.titleView.setText(items.cname);
        holder.liveitem.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ColumnLiveListAdapter.this.mListener.onColumnLiveItemClick(items);
            }
        });
        if (!(items.img == null || items.img.equals(""))) {
            ImageCacheManager.loadImage(items.img, ImageCacheManager.getImageListener(holder.imgView, this.mDefaultDrawable, this.mDefaultDrawable));
        }
        return convertView;
    }
}
