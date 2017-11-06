package com.panda.videoliveplatform.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.panda.videoliveplatform.R;
import com.panda.videoliveplatform.chat.Message.MsgReceiverType;
import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends BaseAdapter {
    private Context context;
    private List<Message> data = null;
    private ArrayList<EmoticonBean> mEmoticonBeanList = null;

    static class ViewHolder {
        public ImageView faceImageView;
        public TextView msg_owner;
        public ImageView photoImageView;
        public TextView textTextView;
        public TextView userNameTextView;

        ViewHolder() {
        }
    }

    public MessageAdapter(Context context, List<Message> list, ArrayList<EmoticonBean> emoticonBeanList) {
        this.context = context;
        this.data = list;
        this.mEmoticonBeanList = emoticonBeanList;
    }

    public int getCount() {
        return this.data != null ? this.data.size() : 0;
    }

    public Object getItem(int position) {
        return this.data.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemViewType(int position) {
        return 1;
    }

    public int getViewTypeCount() {
        return 2;
    }

    @SuppressLint({"InflateParams"})
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Message message = (Message) this.data.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.msg_item, null);
            viewHolder.msg_owner = (TextView) convertView.findViewById(R.id.msg_owner);
            viewHolder.userNameTextView = (TextView) convertView.findViewById(R.id.userNameTextView);
            viewHolder.textTextView = (TextView) convertView.findViewById(R.id.textTextView);
            viewHolder.photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
            viewHolder.faceImageView = (ImageView) convertView.findViewById(R.id.faceImageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.userNameTextView.setText(message.getUserName());
        viewHolder.userNameTextView.setTextColor(Color.parseColor(message.getNameColor()));
        switch (message.getType().intValue()) {
            case 0:
                viewHolder.textTextView.setText(message.getContentText());
                viewHolder.textTextView.setVisibility(0);
                viewHolder.photoImageView.setVisibility(8);
                viewHolder.faceImageView.setVisibility(8);
                SpannableStringBuilder style = new SpannableStringBuilder(message.getContentText());
                style.clearSpans();
                if (EmoticonsRexgexUtils.setTextFace(this.context, this.mEmoticonBeanList, viewHolder.textTextView, message.getContentText(), style, -2, -2)) {
                    viewHolder.textTextView.setText(style);
                }
                MsgReceiverType receiver_type = message.getMsgOwner();
                if (receiver_type != MsgReceiverType.MSG_RECEIVER_ROOM_ADMIN) {
                    if (receiver_type != MsgReceiverType.MSG_RECEIVER_ROOM_SUPER_ADMIN) {
                        if (receiver_type != MsgReceiverType.MSG_RECEIVER_ROOM_OWNER) {
                            if (receiver_type != MsgReceiverType.MSG_RECEIVER_HEADER_MASTER) {
                                viewHolder.msg_owner.setVisibility(8);
                                break;
                            }
                            viewHolder.msg_owner.setText(this.context.getResources().getText(R.string.room_headermaster));
                            viewHolder.msg_owner.setBackgroundColor(this.context.getResources().getColor(R.color.room_headermaster));
                            viewHolder.msg_owner.setVisibility(0);
                            break;
                        }
                        viewHolder.msg_owner.setText(this.context.getResources().getText(R.string.room_owner));
                        viewHolder.msg_owner.setBackgroundColor(this.context.getResources().getColor(R.color.room_owner));
                        viewHolder.msg_owner.setVisibility(0);
                        break;
                    }
                    viewHolder.msg_owner.setText(this.context.getResources().getText(R.string.room_super_admin));
                    viewHolder.msg_owner.setBackgroundColor(this.context.getResources().getColor(R.color.room_super_admin));
                    viewHolder.msg_owner.setVisibility(0);
                    break;
                }
                viewHolder.msg_owner.setText(this.context.getResources().getText(R.string.room_admin));
                viewHolder.msg_owner.setBackgroundColor(this.context.getResources().getColor(R.color.room_admin));
                viewHolder.msg_owner.setVisibility(0);
                break;
            case 1:
                SpannableStringBuilder content_builder;
                if (message.getContentText().isEmpty()) {
                    content_builder = new SpannableStringBuilder(this.context.getString(R.string.send_bamboo_default));
                } else {
                    String format_content = this.context.getString(R.string.send_num_bamboo);
                    int first = format_content.indexOf("%1$s");
                    content_builder = new SpannableStringBuilder(String.format(format_content, new Object[]{message.getContentText()}));
                    content_builder.setSpan(new ForegroundColorSpan(this.context.getResources().getColor(R.color.send_bamboom)), first, message.getContentText().length() + first, 34);
                }
                viewHolder.userNameTextView.setTextColor(this.context.getResources().getColor(R.color.send_bamboom));
                viewHolder.textTextView.setText(content_builder);
                viewHolder.textTextView.setVisibility(0);
                viewHolder.photoImageView.setVisibility(8);
                viewHolder.faceImageView.setVisibility(8);
                viewHolder.msg_owner.setVisibility(8);
                break;
            case 2:
                viewHolder.photoImageView.setVisibility(8);
                viewHolder.textTextView.setVisibility(8);
                viewHolder.faceImageView.setVisibility(0);
                viewHolder.faceImageView.setImageResource(this.context.getResources().getIdentifier(message.getContentText(), "drawable", this.context.getPackageName()));
                break;
            case 3:
                viewHolder.textTextView.setVisibility(8);
                viewHolder.photoImageView.setVisibility(0);
                viewHolder.faceImageView.setVisibility(8);
                viewHolder.photoImageView.setImageResource(this.context.getResources().getIdentifier(message.getContentText(), "drawable", this.context.getPackageName()));
                break;
            default:
                viewHolder.textTextView.setText(message.getContentText());
                viewHolder.photoImageView.setVisibility(8);
                viewHolder.faceImageView.setVisibility(8);
                break;
        }
        return convertView;
    }

    public List<Message> getData() {
        return this.data;
    }

    public void setData(List<Message> data) {
        this.data = data;
    }
}
