package com.panda.videoliveplatform.chat;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView.BufferType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class EmoticonsEditText extends EditText {
    public static final int WRAP_DRAWABLE = -1;
    public static final int WRAP_FONT = -2;
    private Context mContext;
    private ArrayList<EmoticonBean> mEmoticonBeanList = null;
    private int mFontHeight;
    private int mItemHeight;
    private int mItemWidth;
    OnSizeChangedListener onSizeChangedListener;
    OnTextChangedInterface onTextChangedInterface;

    public interface OnSizeChangedListener {
        void onSizeChanged();
    }

    public interface OnTextChangedInterface {
        void onTextChanged(CharSequence charSequence);
    }

    public EmoticonsEditText(Context context) {
        super(context);
        this.mContext = context;
    }

    public EmoticonsEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public EmoticonsEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    public void InitEmoticons(ArrayList<EmoticonBean> emoticonBeanList) {
        this.mEmoticonBeanList = emoticonBeanList;
        this.mFontHeight = getFontHeight();
        this.mItemHeight = this.mFontHeight;
        this.mItemWidth = this.mFontHeight;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (oldh > 0 && this.onSizeChangedListener != null) {
            this.onSizeChangedListener.onSizeChanged();
        }
    }

    protected void onTextChanged(CharSequence arg0, int start, int lengthBefore, int after) {
        super.onTextChanged(arg0, start, lengthBefore, after);
        if (after > 0 && this.mEmoticonBeanList != null) {
            int end = start + after;
            String keyStr = arg0.toString().substring(start, end);
            boolean isEmoticonMatcher = false;
            Iterator i$ = this.mEmoticonBeanList.iterator();
            while (i$.hasNext()) {
                EmoticonBean bean = (EmoticonBean) i$.next();
                if (!TextUtils.isEmpty(bean.getContent()) && bean.getContent().equals(keyStr)) {
                    Drawable drawable = null;
                    try {
                        drawable = new BitmapDrawable(BitmapFactory.decodeStream(this.mContext.getAssets().open(bean.getIconUri())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (drawable != null) {
                        int itemHeight;
                        int itemWidth;
                        if (this.mItemHeight == -1) {
                            itemHeight = drawable.getIntrinsicHeight();
                        } else if (this.mItemHeight == -2) {
                            itemHeight = this.mFontHeight;
                        } else {
                            itemHeight = this.mItemHeight;
                        }
                        if (this.mItemWidth == -1) {
                            itemWidth = drawable.getIntrinsicWidth();
                        } else if (this.mItemWidth == -2) {
                            itemWidth = this.mFontHeight;
                        } else {
                            itemWidth = this.mItemWidth;
                        }
                        drawable.setBounds(0, 0, itemHeight, itemWidth);
                        getText().setSpan(new ImageSpan(drawable), start, end, 17);
                        isEmoticonMatcher = true;
                    }
                }
            }
            if (!isEmoticonMatcher) {
                ImageSpan[] oldSpans = (ImageSpan[]) getText().getSpans(start, end, ImageSpan.class);
                if (oldSpans != null) {
                    for (int i = 0; i < oldSpans.length; i++) {
                        int startOld = end;
                        int endOld = (getText().getSpanEnd(oldSpans[i]) + after) - 1;
                        if (startOld >= 0 && endOld > startOld) {
                            ImageSpan imageSpan = new ImageSpan(oldSpans[i].getDrawable(), 1);
                            getText().removeSpan(oldSpans[i]);
                            getText().setSpan(imageSpan, startOld, endOld, 17);
                        }
                    }
                }
            }
        }
        if (this.onTextChangedInterface != null) {
            this.onTextChangedInterface.onTextChanged(arg0);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } catch (ArrayIndexOutOfBoundsException e) {
            setText(getText().toString());
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setGravity(int gravity) {
        try {
            super.setGravity(gravity);
        } catch (ArrayIndexOutOfBoundsException e) {
            setText(getText().toString());
            super.setGravity(gravity);
        }
    }

    public void setText(CharSequence text, BufferType type) {
        try {
            super.setText(text, type);
        } catch (ArrayIndexOutOfBoundsException e) {
            setText(text.toString());
        }
    }

    private int getFontHeight() {
        Paint paint = new Paint();
        paint.setTextSize(getTextSize());
        FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil((double) (fm.bottom - fm.top));
    }

    public void setOnTextChangedInterface(OnTextChangedInterface i) {
        this.onTextChangedInterface = i;
    }

    public void setOnSizeChangedListener(OnSizeChangedListener i) {
        this.onSizeChangedListener = i;
    }
}
