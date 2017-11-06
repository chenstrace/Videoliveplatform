package com.panda.videolivecore.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtils {
    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return image;
        }
    }
}
