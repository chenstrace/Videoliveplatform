package com.panda.videoliveplatform.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import java.util.List;

public class LiveRoomChartAdapter extends PagerAdapter {
    List<View> m_ViewList;

    public LiveRoomChartAdapter(List<View> lists) {
        this.m_ViewList = lists;
    }

    public int getCount() {
        return this.m_ViewList.size();
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public void destroyItem(View view, int position, Object object) {
        ((ViewPager) view).removeView((View) this.m_ViewList.get(position));
    }

    public Object instantiateItem(View view, int position) {
        ((ViewPager) view).addView((View) this.m_ViewList.get(position), 0);
        return this.m_ViewList.get(position);
    }
}
