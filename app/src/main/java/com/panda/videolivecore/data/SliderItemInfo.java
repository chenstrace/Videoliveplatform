package com.panda.videolivecore.data;

import java.util.ArrayList;

public class SliderItemInfo
{
  public String bigimg;
  public String img;
  public String name;
  public String nickname;
  public String roomid;
  public String title;
  public String url;

  public SliderItemInfo(String paramString)
  {
    this.title = paramString;
    this.roomid = "";
  }

  public class ResponseData
  {
    public ArrayList<SliderItemInfo> data;
    public String errmsg;
    public int errno;

    public ResponseData()
    {
    }
  }
}

/* Location:           D:\software\onekey-decompile-apk好用版本\pandalive_1.0.0.1097.apk.jar
 * Qualified Name:     com.panda.videolivecore.data.SliderItemInfo
 * JD-Core Version:    0.6.1
 */