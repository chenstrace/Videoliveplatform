package com.panda.videolivecore.share;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import com.panda.videolivecore.utils.ToastUtils;
import com.panda.videoliveplatform.R;
//import com.tencent.tauth.IUiListener;
//import com.tencent.tauth.UiError;
//import com.umeng.message.proguard.aY;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ShareDialog extends Dialog implements OnClickListener, OnTouchListener {
    private static final String TAG = "ShareDialog";
    private Activity mActivity;
    private SimpleAdapter mAdapter;
    private Context mContext;
    private OnItemClickListener mGridItemCickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            String msg = String.format(ShareDialog.this.mContext.getResources().getString(R.string.share_to_message_title), new Object[]{ShareDialog.this.mRoomName});
            switch (position) {
                case 0:
                    if (ShareDialog.this.mShareUtils.getWeixinInstalled()) {
                        ShareDialog.this.mShareUtils.shareToWeixinTimeLine(msg, msg, ShareDialog.this.mRoomUrl);
                        ShareDialog.this.dismiss();
                        return;
                    }
                    ToastUtils.show(ShareDialog.this.mContext, String.format(ShareDialog.this.mContext.getResources().getString(R.string.share_app_not_install), new Object[]{ShareDialog.this.mContext.getResources().getString(R.string.share_to_name_weixin_friend)}));
                    return;
                case 1:
                    if (ShareDialog.this.mShareUtils.getWeixinInstalled()) {
                        ShareDialog.this.mShareUtils.shareToWeixinFriend(ShareDialog.this.mContext.getResources().getString(R.string.title_activity_main_fragment), msg, ShareDialog.this.mRoomUrl);
                        ShareDialog.this.dismiss();
                        return;
                    }
                    ToastUtils.show(ShareDialog.this.mContext, String.format(ShareDialog.this.mContext.getResources().getString(R.string.share_app_not_install), new Object[]{ShareDialog.this.mContext.getResources().getString(R.string.share_to_name_weixin_friend)}));
                    return;
                case 2:
                    if (ShareDialog.this.mShareUtils.getQQInstalled()) {
//                        ShareDialog.this.mShareUtils.shareToQQ(true, msg, ShareDialog.this.mRoomUrl, ShareUtils.SHARE_ICON_URL, ShareDialog.this.mTencentUiListener);
                        ShareDialog.this.dismiss();
                        return;
                    }
                    ToastUtils.show(ShareDialog.this.mContext, String.format(ShareDialog.this.mContext.getResources().getString(R.string.share_app_not_install), new Object[]{ShareDialog.this.mContext.getResources().getString(R.string.share_to_name_qq)}));
                    return;
                case 3:
                    if (ShareDialog.this.mShareUtils.getWeiboInstalled()) {
                        ShareDialog.this.mShareUtils.shareToWeibo(msg, ShareDialog.this.mRoomUrl, ShareDialog.this.mPageThumbnailPath);
                        ShareDialog.this.dismiss();
                        return;
                    }
                    ToastUtils.show(ShareDialog.this.mContext, String.format(ShareDialog.this.mContext.getResources().getString(R.string.share_app_not_install), new Object[]{ShareDialog.this.mContext.getResources().getString(R.string.share_to_name_weibo)}));
                    return;
                case 4:
                    if (ShareDialog.this.mShareUtils.getQQInstalled()) {
//                        ShareDialog.this.mShareUtils.shareToQQ(false, msg, ShareDialog.this.mRoomUrl, ShareUtils.SHARE_ICON_URL, ShareDialog.this.mTencentUiListener);
                        ShareDialog.this.dismiss();
                        return;
                    }
                    ToastUtils.show(ShareDialog.this.mContext, String.format(ShareDialog.this.mContext.getResources().getString(R.string.share_app_not_install), new Object[]{ShareDialog.this.mContext.getResources().getString(R.string.share_to_name_qq)}));
                    return;
                default:
                    return;
            }
        }
    };
    private View mOverlayView;
    private String mPageThumbnailPath;
    private String mRoomName;
    private String mRoomUrl;
    private int[] mShareToImage = new int[]{R.drawable.share_pyq, R.drawable.share_wechat, R.drawable.share_qqzone, R.drawable.share_weibo, R.drawable.share_qq};
    private int[] mShareToName = new int[]{R.string.share_to_name_weixin_timeline, R.string.share_to_name_weixin_friend, R.string.share_to_name_qqzone, R.string.share_to_name_weibo, R.string.share_to_name_qq};
    private ShareUtils mShareUtils;
//    private IUiListener mTencentUiListener = new IUiListener() {
//        public void onError(UiError ex) {
//            ToastUtils.show(ShareDialog.this.mContext, ShareDialog.this.mContext.getResources().getString(R.string.share_to_qq_failed) + ex.errorMessage);
//        }
//
//        public void onComplete(Object arg0) {
//        }
//
//        public void onCancel() {
//        }
//    };
    private ViewGroup mView;

    public ShareDialog(Activity activity, int theme) {
        super(activity, theme);
        this.mActivity = activity;
        this.mContext = activity;
        this.mRoomName = null;
        this.mRoomUrl = null;
        this.mPageThumbnailPath = null;
        init(this.mContext);
    }

    public void setRoomName(String name) {
        this.mRoomName = name;
    }

    public void setRoomUrl(String url) {
        this.mRoomUrl = url;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init(Context context) {
        this.mView = (ViewGroup) ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.activity_share_layout, null);
        setContentView(this.mView);
        this.mView.setOnTouchListener(this);
        this.mOverlayView = this.mView.findViewById(R.id.share_mask);
        this.mOverlayView.setOnClickListener(this);
        this.mOverlayView.setOnTouchListener(this);
        this.mShareUtils = new ShareUtils(context);
        ArrayList<HashMap<String, Object>> listShareItem = new ArrayList();
        for (int i = 0; i < this.mShareToName.length; i++) {
            HashMap<String, Object> map = new HashMap();
            map.put("name", context.getResources().getString(this.mShareToName[i]));
            map.put("image", Integer.valueOf(this.mShareToImage[i]));
            listShareItem.add(map);
        }
        GridView gridView = (GridView) this.mView.findViewById(R.id.gridview);
        this.mAdapter = new SimpleAdapter(this.mContext, listShareItem, R.layout.share_item_layout, new String[]{"name", "image"}, new int[]{R.id.share_name, R.id.share_icon});
        gridView.setAdapter(this.mAdapter);
        gridView.setNumColumns(4);
        gridView.setOnItemClickListener(this.mGridItemCickListener);
    }

    public String getPageThumbnailPath() {
        return this.mPageThumbnailPath;
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (v != this.mOverlayView) {
            return false;
        }
        if (event.getAction() != 1) {
            return true;
        }
        dismiss();
        return true;
    }

    public void onClick(View v) {
        dismiss();
    }

    private Bitmap zoomImage(Bitmap bitmap, int maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, baos);
        double mid = (double) (baos.toByteArray().length / 1024);
        if (mid <= ((double) maxSize)) {
            return bitmap;
        }
        double i = mid / ((double) maxSize);
        double newWidth = ((double) bitmap.getWidth()) / Math.sqrt(i);
        double newHeight = ((double) bitmap.getHeight()) / Math.sqrt(i);
        float width = (float) bitmap.getWidth();
        float height = (float) bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) newWidth) / width, ((float) newHeight) / height);
        return Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);
    }
}
