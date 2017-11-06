package com.panda.videolivecore.share;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.JsonReader;
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
import com.panda.videoliveplatform.R;
import com.panda.videolivecore.net.UrlConst;
import com.panda.videolivecore.net.http.HttpRequest;
import com.panda.videolivecore.net.http.IHttpRequestEvent;
import com.panda.videolivecore.net.info.ResultMsgInfo;
import com.panda.videolivecore.net.info.TaskConfigInfo;
import com.panda.videolivecore.utils.ToastUtils;
//import com.tencent.tauth.IUiListener;
//import com.tencent.tauth.UiError;
//import com.umeng.message.proguard.aY;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class ShareAppDialog extends Dialog implements OnClickListener, OnTouchListener, IHttpRequestEvent {
    public static final String PANDA_SHARE_TASK = "panda_share_task";
    private static final String TAG = "ShareAppDialog";
    private final String REQUEST_FUNC_TASK_CONFIG = "TaskConfig";
    private Activity mActivity;
    private SimpleAdapter mAdapter;
    private Context mContext;
    private OnItemClickListener mGridItemCickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            switch (position) {
                case 0:
                    if (ShareAppDialog.this.mShareUtils.getWeixinInstalled()) {
                        ShareAppDialog.this.mShareFinished = ShareAppDialog.this.mShareUtils.shareToWeixinFriend(ShareAppDialog.this.mContext.getResources().getString(R.string.title_activity_main_fragment), ShareAppDialog.this.mShareMsg, ShareAppDialog.this.mShareUrl);
                        ShareAppDialog.this.dismiss();
                        return;
                    }
                    ToastUtils.show(ShareAppDialog.this.mContext, String.format(ShareAppDialog.this.mContext.getResources().getString(R.string.share_app_not_install), new Object[]{ShareAppDialog.this.mContext.getResources().getString(R.string.share_to_name_weixin_friend)}));
                    return;
                case 1:
                    if (ShareAppDialog.this.mShareUtils.getWeixinInstalled()) {
                        ShareAppDialog.this.mShareFinished = ShareAppDialog.this.mShareUtils.shareToWeixinTimeLine(ShareAppDialog.this.mShareMsg, ShareAppDialog.this.mShareMsg, ShareAppDialog.this.mShareUrl);
                        ShareAppDialog.this.dismiss();
                        return;
                    }
                    ToastUtils.show(ShareAppDialog.this.mContext, String.format(ShareAppDialog.this.mContext.getResources().getString(R.string.share_app_not_install), new Object[]{ShareAppDialog.this.mContext.getResources().getString(R.string.share_to_name_weixin_friend)}));
                    return;
                case 2:
                    if (ShareAppDialog.this.mShareUtils.getWeiboInstalled()) {
                        ShareAppDialog.this.mShareFinished = ShareAppDialog.this.mShareUtils.shareToWeibo(ShareAppDialog.this.mShareMsg, ShareAppDialog.this.mShareUrl, ShareAppDialog.this.mPageThumbnailPath);
                        ShareAppDialog.this.dismiss();
                        return;
                    }
                    ToastUtils.show(ShareAppDialog.this.mContext, String.format(ShareAppDialog.this.mContext.getResources().getString(R.string.share_app_not_install), new Object[]{ShareAppDialog.this.mContext.getResources().getString(R.string.share_to_name_weibo)}));
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
    public boolean mShareFinished = false;
    private String mShareMsg;
    private int[] mShareToImage = new int[]{R.drawable.share_wechat, R.drawable.share_pyq, R.drawable.share_weibo};
    private int[] mShareToName = new int[]{R.string.share_to_name_weixin_friend, R.string.share_to_name_weixin_timeline, R.string.share_to_name_weibo};
    private String mShareUrl;
    private ShareUtils mShareUtils;
//    private IUiListener mTencentUiListener = new IUiListener() {
//        public void onError(UiError ex) {
//            ToastUtils.show(ShareAppDialog.this.mContext, ShareAppDialog.this.mContext.getResources().getString(R.string.share_to_qq_failed) + ex.errorMessage);
//        }
//
//        public void onComplete(Object arg0) {
//        }
//
//        public void onCancel() {
//        }
//    };
    private ViewGroup mView;
    private HttpRequest m_request = new HttpRequest(this);

    public ShareAppDialog(Activity activity, int theme) {
        super(activity, theme);
        this.mActivity = activity;
        this.mContext = activity;
        this.mRoomName = null;
        this.mRoomUrl = null;
        this.mPageThumbnailPath = null;
        this.mShareMsg = this.mContext.getResources().getString(R.string.share_to_message_task_title);
        this.mShareUrl = ShareUtils.SHARE_URL;
        init(this.mContext);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init(Context context) {
        this.mView = (ViewGroup) ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.activity_share_app_layout, null);
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
        gridView.setNumColumns(3);
        gridView.setOnItemClickListener(this.mGridItemCickListener);
        this.m_request.send(UrlConst.getTaskConfig(PANDA_SHARE_TASK), true, "TaskConfig");
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

    public boolean onResponse(boolean bResult, String strReponse, String strContext) {
        if (strContext.equalsIgnoreCase("TaskConfig") && bResult) {
            try {
                ResultMsgInfo info = new ResultMsgInfo();
                info.read(strReponse);
                if (info.error == 0) {
                    ByteArrayInputStream is = new ByteArrayInputStream(new String(info.data).getBytes(HttpRequest.CHARSET_NAME_UTF8));
                    TaskConfigInfo task_info = new TaskConfigInfo();
                    JsonReader reader = new JsonReader(new InputStreamReader(is, HttpRequest.CHARSET_NAME_UTF8));
                    task_info.read(reader);
                    reader.close();
                    if (!(TextUtils.isEmpty(task_info.content) || TextUtils.isEmpty(task_info.link))) {
                        this.mShareMsg = task_info.content;
                        this.mShareUrl = task_info.link;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
