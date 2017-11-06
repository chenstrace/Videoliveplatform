package com.panda.videoliveplatform.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.panda.videoliveplatform.R;
import java.util.List;

public class FunctionAdapter extends BaseAdapter {
    private List<Option> data;
    private LayoutInflater inflater;
    private int size = 0;

    class ViewHolder {
        public ImageView iconImageView;
        public TextView nameTextView;

        ViewHolder() {
        }
    }

    public FunctionAdapter(Context context, List<Option> list) {
        this.inflater = LayoutInflater.from(context);
        this.data = list;
        this.size = list.size();
    }

    public int getCount() {
        return this.size;
    }

    public Object getItem(int position) {
        return this.data.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Option option = (Option) this.data.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.function_item, null);
            viewHolder.iconImageView = (ImageView) convertView.findViewById(R.id.iconImageView);
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.iconImageView.setImageDrawable(option.getIconDrawable());
        viewHolder.nameTextView.setText(option.getName());
        return convertView;
    }
}
