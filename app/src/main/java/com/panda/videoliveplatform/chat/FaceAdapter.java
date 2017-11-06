package com.panda.videoliveplatform.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.panda.videolivecore.utils.BitmapUtils;
import com.panda.videoliveplatform.R;
import java.util.List;

public class FaceAdapter extends BaseAdapter {
    private Context context;
    private List<String> data;
    private LayoutInflater inflater;
    private int size = 0;

    class ViewHolder {
        public ImageView bigFace;

        ViewHolder() {
        }
    }

    public FaceAdapter(Context context, List<String> list) {
        this.context = context;
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
        String emoji = (String) this.data.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.face_item, null);
            viewHolder.bigFace = (ImageView) convertView.findViewById(R.id.itemImage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.bigFace.setImageBitmap(BitmapUtils.getImageFromAssetsFile(this.context, emoji));
        return convertView;
    }
}
