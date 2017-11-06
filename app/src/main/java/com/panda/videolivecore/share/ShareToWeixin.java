package com.panda.videolivecore.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.SendMessageToWX.Req;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//import com.tencent.mm.sdk.openapi.WXMediaMessage;
//import com.tencent.mm.sdk.openapi.WXTextObject;
//import com.tencent.mm.sdk.openapi.WXWebpageObject;
import java.io.ByteArrayOutputStream;

public class ShareToWeixin {
    public static final int LIMIT_WIDTH = 300;
    private static final int THUMB_SIZE = 150;
    public static final String WEIXIN_APP_ID = "wx186ca1f9eb9b3301";

    public static boolean share(Context context, String title, String content, String shareUrl, Bitmap thumBitmap, int scene) {
//        WXWebpageObject webpage = new WXWebpageObject();
//        webpage.webpageUrl = shareUrl;
//        WXMediaMessage msg = new WXMediaMessage(webpage);
//        msg.title = title;
//        msg.description = content;
//        if (thumBitmap != null) {
//            msg.setThumbImage(thumBitmap);
//        }
//        Req req = new Req();
//        req.transaction = String.format("appdata %1$s", new Object[]{Long.valueOf(System.currentTimeMillis())});
//        req.message = msg;
//        req.scene = scene;
//        IWXAPI api = WXAPIFactory.createWXAPI(context, WEIXIN_APP_ID, true);
//        if (api.registerApp(WEIXIN_APP_ID)) {
//            return api.sendReq(req);
//        }
        return false;
    }

    public static byte[] bmpToByteArray(Bitmap bmp, boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean shareText(Context context, String text, String shareUrl, int scene) {
        return false;
//        IWXAPI api = WXAPIFactory.createWXAPI(context, WEIXIN_APP_ID, true);
//        if (!api.registerApp(WEIXIN_APP_ID)) {
//            return false;
//        }
//        WXTextObject textObj = new WXTextObject();
//        textObj.text = text;
//        WXMediaMessage msg = new WXMediaMessage();
//        msg.mediaObject = textObj;
//        msg.description = text;
//        Req req = new Req();
//        req.transaction = String.format("text %1$s", new Object[]{Long.valueOf(System.currentTimeMillis())});
//        req.message = msg;
//        req.scene = scene;
//        return api.sendReq(req);
    }

    public static boolean shareImage(Context context, String text, String imagePath, String url, int scene) {
        return false;
//        IWXAPI api = WXAPIFactory.createWXAPI(context, WEIXIN_APP_ID, true);
//        if (!api.registerApp(WEIXIN_APP_ID)) {
//            return false;
//        }
//        WXWebpageObject webpage = new WXWebpageObject();
//        webpage.webpageUrl = url;
//        WXMediaMessage msg = new WXMediaMessage();
//        msg.mediaObject = webpage;
//        Bitmap bmp = BitmapFactory.decodeFile(imagePath);
//        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
//        bmp.recycle();
//        msg.thumbData = bmpToByteArray(thumbBmp, true);
//        msg.description = text;
//        msg.title = text;
//        Req req = new Req();
//        req.transaction = String.format("webpage %1$s", new Object[]{Long.valueOf(System.currentTimeMillis())});
//        req.message = msg;
//        req.scene = scene;
//        return api.sendReq(req);
    }
}
