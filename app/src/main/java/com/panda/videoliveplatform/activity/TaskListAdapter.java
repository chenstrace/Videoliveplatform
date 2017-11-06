package com.panda.videoliveplatform.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.panda.videolivecore.data.ImageCacheManager;
import com.panda.videolivecore.net.info.NewerTaskListInfo.TaskListItem;
//import com.panda.videolivecore.share.ShareAppDialog;
import com.panda.videolivecore.share.ShareAppDialog;
import com.panda.videoliveplatform.R;
import java.util.ArrayList;
import java.util.List;

public class TaskListAdapter extends BaseAdapter {
    private Context mContext;
    private Drawable mDefaultDrawable;
    private OnTaskListItemListener mListener;
    private List<TaskListItem> mTaskListData = new ArrayList();

    public final class ItemViewHolder {
        public TextView desc;
        public View div_bottom;
        public View div_top;
        public ImageView get_rewards;
        public ImageView icon;
        public LinearLayout layout_getting;
        public LinearLayout layout_got;
        public TextView name;
        public int position;

        public void adjustDoneState(String done) {
            if (done != null && !done.isEmpty()) {
                if (done.equalsIgnoreCase("1") || done.equalsIgnoreCase("2")) {
                    this.layout_getting.setVisibility(0);
                    this.layout_got.setVisibility(8);
                } else if (done.equalsIgnoreCase("3")) {
                    this.layout_getting.setVisibility(8);
                    this.layout_got.setVisibility(0);
                }
            }
        }
    }

    public interface OnTaskListItemListener {
        void onGetTaskRewards(String str, String str2);

        void onShareTask(String str);
    }

    public TaskListAdapter(Context c, OnTaskListItemListener listener) {
        this.mContext = c;
        this.mListener = listener;
        this.mDefaultDrawable = this.mContext.getResources().getDrawable(R.drawable.ico_task);
    }

    public void updateData(List<TaskListItem> taskListData) {
        this.mTaskListData.clear();
        this.mTaskListData.addAll(taskListData);
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.mTaskListData.size();
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewHolder holder;
        if (convertView == null) {
            holder = new ItemViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_task_list_item, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.textview_task_item_name);
            holder.get_rewards = (ImageView) convertView.findViewById(R.id.imageview_task_item_get_rewards);
            holder.desc = (TextView) convertView.findViewById(R.id.textview_task_item_desc);
            holder.layout_getting = (LinearLayout) convertView.findViewById(R.id.layout_task_item_get_rewards_getting);
            holder.layout_got = (LinearLayout) convertView.findViewById(R.id.layout_task_item_get_rewards_got);
            holder.icon = (ImageView) convertView.findViewById(R.id.imageview_task_item_icon);
            holder.div_top = convertView.findViewById(R.id.view_task_item_div_top);
            holder.div_bottom = convertView.findViewById(R.id.view_task_item_div_bottom);
            if (position == getCount() - 1) {
                holder.div_bottom.setVisibility(0);
            }
            holder.position = position;
            convertView.setTag(holder);
        } else {
            holder = (ItemViewHolder) convertView.getTag();
        }
        if (holder != null) {
            holder.name.setText(((TaskListItem) this.mTaskListData.get(position)).name);
            holder.desc.setText(((TaskListItem) this.mTaskListData.get(position)).desc);
            holder.get_rewards.setTag(Integer.valueOf(position));
            holder.adjustDoneState(((TaskListItem) this.mTaskListData.get(position)).done);
            if (((TaskListItem) this.mTaskListData.get(position)).icon != null) {
                ImageCacheManager.loadImage(((TaskListItem) this.mTaskListData.get(position)).icon, ImageCacheManager.getImageListener(holder.icon, this.mDefaultDrawable, this.mDefaultDrawable));
            }
            holder.get_rewards.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    TaskListItem item = (TaskListItem) TaskListAdapter.this.mTaskListData.get(((Integer) v.getTag()).intValue());
                    if (item.appkey.equalsIgnoreCase(ShareAppDialog.PANDA_SHARE_TASK)) {
                        if (item.done.equalsIgnoreCase("1") && TaskListAdapter.this.mListener != null) {
                            TaskListAdapter.this.mListener.onShareTask(item.id);
                        } else if (item.done.equalsIgnoreCase("2")) {
                            TaskListAdapter.this.mListener.onGetTaskRewards(item.my_task_id, item.appkey);
                        }
                    } else if (item.done.equalsIgnoreCase("1")) {
                        Toast.makeText(TaskListAdapter.this.mContext, String.format("请先 %s 再领取奖励", new Object[]{item.name}), 0).show();
                    } else if (item.done.equalsIgnoreCase("2")) {
                        if (TaskListAdapter.this.mListener != null) {
                            TaskListAdapter.this.mListener.onGetTaskRewards(item.my_task_id, item.appkey);
                        }
                    } else if (!item.done.equalsIgnoreCase("3")) {
                    }
                }
            });
        }
        return convertView;
    }
}
