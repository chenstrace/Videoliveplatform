package com.panda.videolivecore.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.GridView;

public class HomeGridView extends GridView
{
  public HomeGridView(Context paramContext)
  {
    super(paramContext);
  }

  public HomeGridView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, View.MeasureSpec.makeMeasureSpec(536870911, -2147483648));
  }
}

/* Location:           D:\software\onekey-decompile-apk好用版本\pandalive_1.0.0.1097.apk.jar
 * Qualified Name:     com.panda.videolivecore.view.HomeGridView
 * JD-Core Version:    0.6.1
 */