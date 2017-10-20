package com.panda.videoliveplatform.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class FragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentArray;

    public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragmentArray) {
        this(fm);
        this.fragmentArray = fragmentArray;
    }

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int arg0) {
        return (Fragment) this.fragmentArray.get(arg0);
    }

    public int getCount() {
        return this.fragmentArray.size();
    }
}