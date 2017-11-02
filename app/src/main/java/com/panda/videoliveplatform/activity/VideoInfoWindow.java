package com.panda.videoliveplatform.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import com.panda.videolivecore.utils.DpUtils;
import com.panda.videoliveplatform.R;

public class VideoInfoWindow {
    private View mAnchor;
    private Context mContext;
    private View mContextView;
    private ImageView mIconView;
    private int mResId;
    private String mText;
    private TextView mTextview;
    private PopupWindow mWindow;

    public VideoInfoWindow(Context context, View anchor, int resId, String text) {
        this.mContext = context;
        this.mAnchor = anchor;
        this.mResId = resId;
        this.mText = text;
    }

    public void SetResId(int resId) {
        this.mResId = resId;
        if (this.mIconView != null) {
            this.mIconView.setBackgroundResource(this.mResId);
            this.mIconView.invalidate();
        }
    }

    public void SetText(String text) {
        this.mText = text;
        if (this.mTextview != null) {
            this.mTextview.setText(this.mText);
            this.mTextview.invalidate();
        }
    }

    public void Show() {
        CloseWindow();
        this.mWindow = new PopupWindow(this.mContext);
        this.mWindow.setFocusable(false);
        this.mWindow.setBackgroundDrawable(null);
        this.mWindow.setOutsideTouchable(true);
        this.mWindow.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                VideoInfoWindow.this.mWindow = null;
                VideoInfoWindow.this.mContextView = null;
                VideoInfoWindow.this.mIconView = null;
                VideoInfoWindow.this.mTextview = null;
            }
        });
        this.mContextView = LayoutInflater.from(this.mContext).inflate(R.layout.video_info_window, null);
        this.mIconView = (ImageView) this.mContextView.findViewById(R.id.icon_img);
        this.mIconView.setBackgroundResource(this.mResId);
        this.mTextview = (TextView) this.mContextView.findViewById(R.id.text);
        this.mTextview.setText(this.mText);
        this.mWindow.setContentView(this.mContextView);
        this.mWindow.setWidth(-2);
        this.mWindow.setHeight(DpUtils.Dp2Px(this.mContext, 40.0f));
        this.mWindow.showAtLocation(this.mAnchor, 17, 0, 0);
    }

    public void CloseWindow() {
        if (this.mWindow != null) {
            this.mWindow.dismiss();
            this.mWindow = null;
        }
    }
}
