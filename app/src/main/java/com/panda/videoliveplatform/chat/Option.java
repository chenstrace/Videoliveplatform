package com.panda.videoliveplatform.chat;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class Option {
    private Drawable iconDrawable;
    private String name;

    public Option(String name, Drawable iconDrawable) {
        this.name = name;
        this.iconDrawable = iconDrawable;
    }

    public Option(Context context, String name, int iconResId) {
        this.name = name;
        this.iconDrawable = context.getResources().getDrawable(iconResId);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIconDrawable() {
        return this.iconDrawable;
    }

    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }
}
