package com.panda.videoliveplatform.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;

public class FragmentAdapter extends FragmentPagerAdapter
{
  private ArrayList<Fragment> fragmentArray;

  public FragmentAdapter(FragmentManager paramFragmentManager)
  {
    super(paramFragmentManager);
  }

  public FragmentAdapter(FragmentManager paramFragmentManager, ArrayList<Fragment> paramArrayList)
  {
    this(paramFragmentManager);
    this.fragmentArray = paramArrayList;
  }

  public int getCount()
  {
    return this.fragmentArray.size();
  }

  public Fragment getItem(int paramInt)
  {
    return (Fragment)this.fragmentArray.get(paramInt);
  }
}

/* Location:           D:\software\onekey-decompile-apk好用版本\pandalive_1.0.0.1097.apk.jar
 * Qualified Name:     com.panda.videoliveplatform.fragment.FragmentAdapter
 * JD-Core Version:    0.6.1
 */