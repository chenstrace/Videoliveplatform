package com.panda.videoliveplatform.chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.panda.videoliveplatform.chat.PagerSlidingTabStrip.IconTabProvider;
import java.util.ArrayList;
import java.util.Map;

public class FaceCategroyAdapter extends FragmentPagerAdapter implements IconTabProvider {
    private Map<Integer, ArrayList<String>> data;
    private OnOperationListener onOperationListener;

    public FaceCategroyAdapter(FragmentManager fm) {
        super(fm);
    }

    public int getPageIconResId(int position) {
        return ((Integer) this.data.keySet().toArray()[position]).intValue();
    }

    public int getCount() {
        return this.data == null ? 0 : this.data.size();
    }

    public Fragment getItem(int position) {
        FacePageFragment f = new FacePageFragment();
        f.setOnOperationListener(this.onOperationListener);
        Bundle b = new Bundle();
        b.putInt(FacePageFragment.ARG_POSITION, position);
        b.putStringArrayList(FacePageFragment.ARG_FACE_DATA, (ArrayList) this.data.get(this.data.keySet().toArray()[position]));
        f.setArguments(b);
        return f;
    }

    public OnOperationListener getOnOperationListener() {
        return this.onOperationListener;
    }

    public void setOnOperationListener(OnOperationListener onOperationListener) {
        this.onOperationListener = onOperationListener;
    }

    public Map<Integer, ArrayList<String>> getData() {
        return this.data;
    }

    public void setData(Map<Integer, ArrayList<String>> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
