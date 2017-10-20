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
import java.util.HashMap;
import java.util.Map;

public class GsonRequest<T> extends Request<T> {
    private final Class<T> mClazz;
    private final Gson mGson;
    private final Map<String, String> mHeaders;
    private final Listener<T> mListener;

    public GsonRequest(String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
        this(0, url, clazz, new HashMap(), listener, errorListener);
    }

    public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers, Listener<T> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mGson = new Gson();
        headers.put("xiaozhangdepandatv", "1");
        this.mClazz = clazz;
        this.mHeaders = headers;
        this.mListener = listener;
    }

    public Map<String, String> getHeaders() throws AuthFailureError {
        return this.mHeaders != null ? this.mHeaders : super.getHeaders();
    }

    protected void deliverResponse(T response) {
        this.mListener.onResponse(response);
    }

    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(this.mGson.fromJson(new String(response.data, HttpHeaderParser.parseCharset(response.headers)), this.mClazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (Throwable e) {
            return Response.error(new ParseError(e));
        }
    }
}
