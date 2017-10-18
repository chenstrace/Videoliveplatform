package com.panda.videolivecore.data;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class GsonRequest<T> extends Request<T>
{
  private final Class<T> mClazz;
  private final Gson mGson = new Gson();
  private final Map<String, String> mHeaders;
  private final Response.Listener<T> mListener;

  public GsonRequest(int paramInt, String paramString, Class<T> paramClass, Map<String, String> paramMap, Response.Listener<T> paramListener, Response.ErrorListener paramErrorListener)
  {
    super(paramInt, paramString, paramErrorListener);
    paramMap.put("xiaozhangdepandatv", "1");
    this.mClazz = paramClass;
    this.mHeaders = paramMap;
    this.mListener = paramListener;
  }

  public GsonRequest(String paramString, Class<T> paramClass, Response.Listener<T> paramListener, Response.ErrorListener paramErrorListener)
  {
    this(0, paramString, paramClass, new HashMap(), paramListener, paramErrorListener);
  }

  protected void deliverResponse(T paramT)
  {
    this.mListener.onResponse(paramT);
  }

  public Map<String, String> getHeaders()
    throws AuthFailureError
  {
    if (this.mHeaders != null);
    for (Map localMap = this.mHeaders; ; localMap = super.getHeaders())
      return localMap;
  }

  protected Response<T> parseNetworkResponse(NetworkResponse paramNetworkResponse) {
    try {
      String str = new String(paramNetworkResponse.data, HttpHeaderParser.parseCharset(paramNetworkResponse.headers));
      Response localResponse2 = Response.success(this.mGson.fromJson(str, this.mClazz), HttpHeaderParser.parseCacheHeaders(paramNetworkResponse));
      //localResponse1 = localResponse2;
      //return localResponse1;
      return localResponse2;
    } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
      //while (true)
      //localResponse1 = Response.error(new ParseError(localUnsupportedEncodingException));
    }

    return Response.success(this.mGson.fromJson("", this.mClazz), HttpHeaderParser.parseCacheHeaders(paramNetworkResponse));
  }
}

/* Location:           D:\software\onekey-decompile-apk好用版本\pandalive_1.0.0.1097.apk.jar
 * Qualified Name:     com.panda.videolivecore.data.GsonRequest
 * JD-Core Version:    0.6.1
 */