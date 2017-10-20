package com.panda.videolivecore.data;

import android.app.ActivityManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.widget.ImageView;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.panda.videolivecore.CoreApplication;

public class ImageCacheManager {
    private static final int MEM_CACHE_SIZE = ((((ActivityManager) CoreApplication.getInstance().getApplication().getApplicationContext().getSystemService("activity")).getMemoryClass() * 1048576) / 8);
    private static ImageLoader mImageLoader = new ImageLoader(RequestManager.mRequestQueue, new BitmapLruCache(MEM_CACHE_SIZE));

    private ImageCacheManager() {
    }

    public static ImageContainer loadImage(String requestUrl, ImageListener imageListener) {
        return loadImage(requestUrl, imageListener, 0, 0);
    }

    public static ImageContainer loadImage(String requestUrl, ImageListener imageListener, int maxWidth, int maxHeight) {
        return mImageLoader.get(requestUrl, imageListener, maxWidth, maxHeight);
    }

    public static ImageListener getImageListener(final ImageView view, final Drawable defaultImageDrawable, final Drawable errorImageDrawable) {
        return new ImageListener() {
            public void onErrorResponse(VolleyError error) {
                if (errorImageDrawable != null) {
                    view.setImageDrawable(errorImageDrawable);
                }
            }

            public void onResponse(ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    if (isImmediate || defaultImageDrawable == null) {
                        view.setImageBitmap(response.getBitmap());
                        return;
                    }
                    TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{defaultImageDrawable, new BitmapDrawable(CoreApplication.getInstance().getApplication().getApplicationContext().getResources(), response.getBitmap())});
                    transitionDrawable.setCrossFadeEnabled(true);
                    view.setImageDrawable(transitionDrawable);
                    transitionDrawable.startTransition(100);
                } else if (defaultImageDrawable != null) {
                    view.setImageDrawable(defaultImageDrawable);
                }
            }
        };
    }
}
