package com.panda.videolivecore.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ImageView;

public class ScaleImageView extends ImageView {
    private boolean mHasSetScale = false;
    private float mScaleValue;

    public ScaleImageView(Context context) {
        super(context);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setScaleValue(float val) {
        this.mHasSetScale = true;
        this.mScaleValue = val;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (this.mHasSetScale) {
            height = (int) (((float) width) * this.mScaleValue);
        } else {
            height = (int) ((((float) width) * ((float) getDrawable().getIntrinsicHeight())) / ((float) getDrawable().getIntrinsicWidth()));
        }
        setMeasuredDimension(width, height);
    }
}
