package com.panda.videolivecore.data;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.panda.videolivecore.CoreApplication;

public class RequestManager {
    public static RequestQueue mRequestQueue = Volley.newRequestQueue(CoreApplication.getInstance().getApplication().getApplicationContext());

    public static void addRequest(Request<?> paramRequest, Object paramObject) {
        if (paramObject != null)
            paramRequest.setTag(paramObject);
        mRequestQueue.add(paramRequest);
    }

    public static void cancelAll(Object paramObject) {
        mRequestQueue.cancelAll(paramObject);
    }
}