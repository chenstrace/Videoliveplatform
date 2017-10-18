package com.panda.videolivecore.utils;

import com.panda.videolivecore.CoreApplication;
import com.panda.videolivecore.net.info.PtTokenInfo;
import com.panda.videolivecore.network.LoginManager;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class CookieContiner
{
  public static String COOKIE_NAME = "Cookie";
  private static HashMap<String, String> mCookieContiner = new HashMap();
  private static CookieStore mCookieStore = new BasicCookieStore();
  private static HashMap<String, String> mPtTokenContiner = new HashMap();

  public static void clearCookies()
  {
    try
    {
      mCookieStore.clear();
      mCookieContiner.clear();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public static void clearPtToken()
  {
    try
    {
      mPtTokenContiner.clear();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public static boolean fillCookieHeader(HttpGet paramHttpGet, String paramString)
  {
    try
    {
      StringBuffer localStringBuffer = new StringBuffer();
      boolean bool1 = getCookie(paramString, localStringBuffer);
      boolean bool2 = false;
      if (bool1)
      {
        paramHttpGet.setHeader("Cookie", paramString + "=" + localStringBuffer);
        bool2 = true;
      }
      return bool2;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public static boolean fillCookieHeaders(HttpURLConnection paramHttpURLConnection)
  {
    try
    {
      String str = getCookies();
      if (!str.isEmpty())
      {
        paramHttpURLConnection.setRequestProperty(COOKIE_NAME, str);
        bool = true;
        return bool;
      }
      boolean bool = false;
    }
    finally
    {
    }
  }

  public static boolean fillCookieHeaders(HttpGet paramHttpGet)
  {
    try
    {
      String str = getCookies();
      if (!str.isEmpty())
      {
        paramHttpGet.setHeader(COOKIE_NAME, str);
        bool = true;
        return bool;
      }
      boolean bool = false;
    }
    finally
    {
    }
  }

  public static void fillTokenHeaders(HttpURLConnection paramHttpURLConnection)
  {
    try
    {
      Iterator localIterator = mPtTokenContiner.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if ((!((String)localEntry.getKey()).isEmpty()) && (!((String)localEntry.getValue()).isEmpty()))
          paramHttpURLConnection.setRequestProperty((String)localEntry.getKey(), (String)localEntry.getValue());
      }
    }
    finally
    {
    }
  }

  public static void fillTokenHeaders(HttpGet paramHttpGet)
  {
    try
    {
      Iterator localIterator = mPtTokenContiner.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if ((!((String)localEntry.getKey()).isEmpty()) && (!((String)localEntry.getValue()).isEmpty()))
          paramHttpGet.setHeader((String)localEntry.getKey(), (String)localEntry.getValue());
      }
    }
    finally
    {
    }
  }

  public static boolean getCookie(String paramString, StringBuffer paramStringBuffer)
  {
    try
    {
      boolean bool1 = mCookieContiner.containsKey(paramString);
      boolean bool2 = false;
      if (bool1)
      {
        paramStringBuffer.append((String)mCookieContiner.get(paramString));
        bool2 = true;
      }
      return bool2;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public static Map<String, String> getCookieHeaderMap()
  {
    try
    {
      HashMap localHashMap = new HashMap();
      String str = "";
      Iterator localIterator = mCookieContiner.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if (!str.isEmpty())
          str = str + ";";
        str = str + (String)localEntry.getKey() + "=" + (String)localEntry.getValue();
      }
      if (!str.isEmpty())
        localHashMap.put(COOKIE_NAME, str);
      return localHashMap;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public static String getCookies()
  {
    try
    {
      Object localObject1 = "";
      Iterator localIterator = mCookieContiner.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if (!((String)localObject1).isEmpty())
          localObject1 = (String)localObject1 + ";";
        String str = (String)localObject1 + (String)localEntry.getKey() + "=" + (String)localEntry.getValue();
        localObject1 = str;
      }
      return localObject1;
    }
    finally
    {
      localObject2 = finally;
      throw localObject2;
    }
  }

  public static HttpContext getHttpContext()
  {
    try
    {
      BasicHttpContext localBasicHttpContext = new BasicHttpContext();
      localBasicHttpContext.setAttribute("http.cookie-store", mCookieStore);
      return localBasicHttpContext;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public static String getPtSign()
  {
    try
    {
      String str = (String)mPtTokenContiner.get("pt_sign");
      return str;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public static String getPtTime()
  {
    try
    {
      String str = (String)mPtTokenContiner.get("pt_time");
      return str;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public static String getToken()
  {
    try
    {
      Object localObject1 = "";
      Iterator localIterator = mPtTokenContiner.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if (!((String)localObject1).isEmpty())
          localObject1 = (String)localObject1 + "&";
        String str = (String)localObject1 + (String)localEntry.getKey() + "=" + (String)localEntry.getValue();
        localObject1 = str;
      }
      return localObject1;
    }
    finally
    {
      localObject2 = finally;
      throw localObject2;
    }
  }

  public static boolean isLogin()
  {
    try
    {
      boolean bool = CoreApplication.getInstance().GetLoginManager().IsLogin();
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public static boolean setCookies(HttpResponse paramHttpResponse)
  {
    try
    {
      boolean bool = setCookies(paramHttpResponse.getHeaders("Set-Cookie"));
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public static boolean setCookies(CookieStore paramCookieStore)
  {
    boolean bool;
    try
    {
      List localList = paramCookieStore.getCookies();
      if (localList != null)
      {
        Iterator localIterator = localList.iterator();
        if (localIterator.hasNext())
        {
          Cookie localCookie = (Cookie)localIterator.next();
          String str1 = localCookie.getName();
          String str2 = localCookie.getValue();
          mCookieContiner.put(str1, str2);
        }
      }
    }
    finally
    {
    }
    while (true)
    {
      return bool;
      bool = false;
    }
  }

  public static boolean setCookies(Header[] paramArrayOfHeader)
  {
    boolean bool = false;
    int i;
    if (paramArrayOfHeader != null)
      i = 0;
    try
    {
      while (i < paramArrayOfHeader.length)
      {
        String[] arrayOfString1 = paramArrayOfHeader[i].getValue().split(";");
        int j = 0;
        if (j < arrayOfString1.length)
        {
          String[] arrayOfString2 = arrayOfString1[j].split("=");
          String str1 = arrayOfString2[0].trim();
          if (arrayOfString2.length > 1);
          for (String str2 = arrayOfString2[1].trim(); ; str2 = "")
          {
            mCookieContiner.put(str1, str2);
            j++;
            break;
          }
        }
        i++;
      }
      bool = true;
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public static void setPtToken(PtTokenInfo paramPtTokenInfo)
  {
    try
    {
      mPtTokenContiner.put("pt_time", paramPtTokenInfo.strTime);
      mPtTokenContiner.put("pt_sign", paramPtTokenInfo.strToken);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
}

/* Location:           D:\software\onekey-decompile-apk好用版本\pandalive_1.0.0.1097.apk.jar
 * Qualified Name:     com.panda.videolivecore.utils.CookieContiner
 * JD-Core Version:    0.6.1
 */