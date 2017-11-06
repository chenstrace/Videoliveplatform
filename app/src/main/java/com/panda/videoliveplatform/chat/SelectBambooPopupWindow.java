package com.panda.videoliveplatform.chat;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.panda.videoliveplatform.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectBambooPopupWindow extends PopupWindow {
    private final int[] BAMBOO_NUM_LIST = new int[]{100, 520, 1000, 2333, 6666, 10000, 88888, 99999};
    private SimpleAdapter mAdapter;
    private View mContentView;
    private Context mContext;
    private int mDefaultBambooNum;
    private ChatRoomView mParentView;
    private GridView mSelectBambooGrid;

    private class MySimpleAdapter extends SimpleAdapter {
        private int mPosition;

        public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to, int position) {
            super(context, data, resource, from, to);
            this.mPosition = position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            if (view != null) {
                View child_view = ((ViewGroup) view).getChildAt(0);
                if (child_view instanceof TextView) {
                    TextView text_view = (TextView) child_view;
                    if (this.mPosition == position) {
                        text_view.setTextColor(SelectBambooPopupWindow.this.mContext.getResources().getColor(R.color.bamboo_num));
                    } else {
                        text_view.setTextColor(SelectBambooPopupWindow.this.mContext.getResources().getColor(R.color.bamboo_title));
                    }
                }
                view.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (position >= 0 && position < SelectBambooPopupWindow.this.BAMBOO_NUM_LIST.length) {
                            SelectBambooPopupWindow.this.mParentView.setDefaultBambooNum(SelectBambooPopupWindow.this.BAMBOO_NUM_LIST[position]);
                        }
                        SelectBambooPopupWindow.this.dismiss();
                    }
                });
            }
            return view;
        }
    }

    public SelectBambooPopupWindow(Context context, ChatRoomView parentView, View contentView, int width, int height, boolean focusable, int defaultBambooNum) {
        super(contentView, width, height, focusable);
        this.mContext = context;
        this.mParentView = parentView;
        this.mContentView = contentView;
        this.mDefaultBambooNum = defaultBambooNum;
        init();
    }

    private void init() {
        setTouchable(true);
        setOutsideTouchable(true);
        setInputMethodMode(2);
        setBackgroundDrawable(this.mContext.getResources().getDrawable(17170445));
        this.mContentView.findViewById(R.id.parent).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SelectBambooPopupWindow.this.dismiss();
            }
        });
        this.mSelectBambooGrid = (GridView) this.mContentView.findViewById(R.id.select_bamboo_grid);
        setSelectBambooList();
    }

    private void setSelectBambooList() {
        ArrayList<HashMap<String, Object>> listItem = new ArrayList();
        String[] from = new String[]{"bamboo_num"};
        int[] to = new int[]{R.id.bamboo_num};
        int position = 0;
        for (int index = 0; index < this.BAMBOO_NUM_LIST.length; index++) {
            if (this.BAMBOO_NUM_LIST[index] == this.mDefaultBambooNum) {
                position = index;
            }
            HashMap<String, Object> map = new HashMap();
            map.put(from[0], String.valueOf(this.BAMBOO_NUM_LIST[index]));
            listItem.add(map);
        }
        this.mAdapter = new MySimpleAdapter(this.mContext, listItem, R.layout.select_bamboo_list_item, from, to, position);
        this.mSelectBambooGrid.setAdapter(this.mAdapter);
        this.mSelectBambooGrid.setSelection(position);
    }
}
