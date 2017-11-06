package com.panda.videoliveplatform.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.JsonReader;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.panda.videolivecore.CommonDefine;
import com.panda.videolivecore.data.ImageCacheManager;
import com.panda.videolivecore.net.UrlConst;
import com.panda.videolivecore.net.http.HttpRequest;
import com.panda.videolivecore.net.http.IHttpRequestEvent;
import com.panda.videolivecore.net.info.NewerTaskListInfo;
import com.panda.videolivecore.net.info.ResultMsgInfo;
import com.panda.videolivecore.network.LoginManager;
//import com.panda.videolivecore.share.ShareAppDialog;
import com.panda.videolivecore.share.ShareAppDialog;
import com.panda.videolivecore.utils.CheckResultErrorUtils;
import com.panda.videolivecore.utils.CookieContiner;
import com.panda.videolivecore.utils.Md5Utils;
import com.panda.videoliveplatform.MyApplication;
import com.panda.videoliveplatform.R;
import com.panda.videoliveplatform.activity.TaskListAdapter.OnTaskListItemListener;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.UUID;

public class TaskListActivity extends Activity implements OnTaskListItemListener, IHttpRequestEvent {
    public static final int TASK_LIST_REQUEST_CODE = 768;
    private final String REQUEST_FUNC_GET_SHARE_TASK_DONE = "GetShareTaskDone";
    private final String REQUEST_FUNC_GET_TASK_REWARDS = "GetTaskRewards";
    private final String REQUEST_FUNC_REFESH_TASK_LIST = "RefreshTaskList";
    private TaskListAdapter mAdapter;
    private boolean mIsLoadingData = false;
    private View mLoadErrorView;
    private View mLoadingView;
    private ShareAppDialog mShareDialog;
    private boolean mShareTaskReward = false;
    private PullToRefreshListView mTaskListCtrl;
    private HttpRequest m_request = null;

    public class TaskListAdapter extends BaseAdapter {
        private Context mContext;

        public final class ItemViewHolder {
            public TextView desc;
            public View div_bottom;
            public View div_top;
            public ImageView get_rewards;
            public ImageView icon;
            public LinearLayout layout_getting;
            public LinearLayout layout_got;
            public TextView name;
            public int position;

            public void adjustDoneState(String done) {
                if (done != null && !done.isEmpty()) {
                    if (done.equalsIgnoreCase("1") || done.equalsIgnoreCase("2")) {
                        this.layout_getting.setVisibility(0);
                        this.layout_got.setVisibility(8);
                    } else if (done.equalsIgnoreCase("3")) {
                        this.layout_getting.setVisibility(8);
                        this.layout_got.setVisibility(0);
                    }
                }
            }
        }

        public TaskListAdapter(Context c) {
            this.mContext = c;
        }

        public int getCount() {
//            return TaskListActivity.this.mShareDialog;
            return 0;
        }


        public Object getItem(int position) {
            return Integer.valueOf(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ItemViewHolder holder;
            if (convertView == null) {
                holder = new ItemViewHolder();
                convertView = TaskListActivity.this.getLayoutInflater().inflate(R.layout.layout_mini_control, parent, false);
                holder.name = (TextView) convertView.findViewById(R.id.view_task_item_div_top);
                holder.get_rewards = (ImageView) convertView.findViewById(R.id.textview_task_item_desc);
                holder.desc = (TextView) convertView.findViewById(R.id.imageview_task_item_icon);
                holder.layout_getting = (LinearLayout) convertView.findViewById(R.id.textview_task_item_name);
                holder.layout_got = (LinearLayout) convertView.findViewById(R.id.layout_task_item_get_rewards_getting);
                holder.icon = (ImageView) convertView.findViewById(R.id.img_play);
                holder.div_top = convertView.findViewById(R.id.img_fullscreen);
                holder.div_bottom = convertView.findViewById(R.id.imageview_task_item_get_rewards);
//                if (position == TaskListActivity.this.mShareDialog - 1) {
//                    holder.div_bottom.setVisibility(0);
//                }
                holder.position = position;
                convertView.setTag(holder);
            } else {
                holder = (ItemViewHolder) convertView.getTag();
            }
            if (holder != null) {
//                holder.name.setText(((TaskListItem) TaskListActivity.access$400(TaskListActivity.this).get(position)).name);
//                holder.desc.setText(((TaskListItem) TaskListActivity.access$400(TaskListActivity.this).get(position)).desc);
//                holder.get_rewards.setTag(Integer.valueOf(position));
//                holder.adjustDoneState(((TaskListItem) TaskListActivity.access$400(TaskListActivity.this).get(position)).done);
//                if (((TaskListItem) TaskListActivity.access$400(TaskListActivity.this).get(position)).icon != null) {
//                    ImageCacheManager.loadImage(((TaskListItem) TaskListActivity.access$400(TaskListActivity.this).get(position)).icon, ImageCacheManager.getImageListener(holder.icon, TaskListActivity.access$500(TaskListActivity.this), TaskListActivity.access$500(TaskListActivity.this)));
//                }
                holder.get_rewards.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
//                        int pos = ((Integer) v.getTag()).intValue();
//                        if (((TaskListItem) TaskListActivity.access$400(TaskListActivity.this).get(pos)).done.equalsIgnoreCase("1")) {
//                            Toast.makeText(TaskListAdapter.this.mContext, String.format("请先 %s 再领取奖励", new Object[]{((TaskListItem) TaskListActivity.access$400(TaskListActivity.this).get(pos)).name}), 0).show();
//                        } else if (((TaskListItem) TaskListActivity.access$400(TaskListActivity.this).get(pos)).done.equalsIgnoreCase("2")) {
//                            TaskListActivity.this.getTaskRewards(((TaskListItem) TaskListActivity.access$400(TaskListActivity.this).get(pos)).my_task_id);
//                        } else if (!((TaskListItem) TaskListActivity.access$400(TaskListActivity.this).get(pos)).done.equalsIgnoreCase("3")) {
//                        }
                    }
                });
            }
            return convertView;
        }
    }

    class TaskListItem {
        public String bamboo;
        public String desc;
        public String done;
        public String icon;
        public String id;
        public String interval;
        public String my_task_id;
        public String name;
        public String priority;

        TaskListItem() {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        initLoadingView();
        initTaskList();
        if (VERSION.SDK_INT >= 19) {
            findViewById(R.id.statusbar_dummy).setVisibility(0);
            Window win = getWindow();
            LayoutParams winParams = win.getAttributes();
            winParams.flags |= 67108864;
            win.setAttributes(winParams);
        }
        ((ImageButton) findViewById(R.id.image_button_back)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TaskListActivity.this.setResult(-1, new Intent());
                TaskListActivity.this.finish();
            }
        });
        ((TextView) findViewById(R.id.title_text)).setText(getText(R.string.title_activity_task_list));
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    private void initTaskList() {
        this.m_request = new HttpRequest(this);
        this.mTaskListCtrl = (PullToRefreshListView) findViewById(R.id.listview_task);
        this.mTaskListCtrl.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(DateUtils.formatDateTime(MyApplication.getInstance().getApplicationContext(), System.currentTimeMillis(), 524305));
                if (!TaskListActivity.this.mIsLoadingData) {
                    TaskListActivity.this.refreshTaskList();
                }
            }
        });
        this.mTaskListCtrl.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount && TaskListActivity.this.mIsLoadingData) {
                }
            }
        });
        this.mTaskListCtrl.setMode(Mode.PULL_FROM_START);
        this.mTaskListCtrl.setScrollingWhileRefreshingEnabled(true);
//        this.mAdapter = new TaskListAdapter(MyApplication.getInstance().getApplicationContext(), this);
        this.mTaskListCtrl.setAdapter(this.mAdapter);
        this.mTaskListCtrl.setEmptyView(findViewById(R.id.loadview));
        refreshTaskList();
    }

    protected void initLoadingView() {
        this.mLoadingView = findViewById(R.id.loading);
        this.mLoadErrorView = findViewById(R.id.loaderror);
        this.mLoadErrorView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TaskListActivity.this.mLoadingView.setVisibility(0);
                TaskListActivity.this.mLoadErrorView.setVisibility(8);
                TaskListActivity.this.refreshTaskList();
            }
        });
    }

    protected void refreshTaskList() {
        this.mIsLoadingData = true;
        this.m_request.send(UrlConst.getNewerTaskListUrl(), true, "RefreshTaskList");
    }

    protected boolean parseTaskList(String strResponse) {
        try {
            ResultMsgInfo info = new ResultMsgInfo();
            info.read(strResponse);
            if (info.error != 0) {
                return false;
            }
            JsonReader reader = new JsonReader(new InputStreamReader(new ByteArrayInputStream(new String(info.data).getBytes(HttpRequest.CHARSET_NAME_UTF8)), HttpRequest.CHARSET_NAME_UTF8));
            NewerTaskListInfo task_info = new NewerTaskListInfo();
            task_info.read(reader);
            //this.mAdapter.updateData(task_info.mTaskListData);
            reader.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected String parseTaskRewards(String strResponse) {
        if (CheckResultErrorUtils.CheckError(strResponse)) {
            return new ResultMsgInfo().readDataString(strResponse);
        }
        return "";
    }

    public boolean onResponse(boolean bResult, String strReponse, String strContext) {
        if (strContext.equalsIgnoreCase("RefreshTaskList")) {
            if (parseTaskList(strReponse)) {
                this.mIsLoadingData = false;
                this.mTaskListCtrl.onRefreshComplete();
                loadDataSuccess();
            } else {
                this.mTaskListCtrl.onRefreshComplete();
                loadDataError();
                this.mIsLoadingData = false;
            }
        } else if (strContext.equalsIgnoreCase("GetTaskRewards")) {
            if (!parseTaskRewards(strReponse).isEmpty()) {
                Toast.makeText(this, String.format("领取奖励成功，你获得了 %s 个竹子。", new Object[]{parseTaskRewards(strReponse)}), 0).show();
                refreshTaskList();
                if (this.mShareTaskReward) {
                    //UmengStatic.StaticShareTaskGet(this);
                }
            }
            Intent intent = new Intent(CommonDefine.BROADCAST_BAMBOOS_CHANGED);
            intent.setPackage(getPackageName());
            sendBroadcast(intent);
            this.mShareTaskReward = false;
        } else if (strContext.equalsIgnoreCase("GetShareTaskDone")) {
            ResultMsgInfo info = new ResultMsgInfo();
            info.read(strReponse);
            if (info.error == 0) {
                refreshTaskList();
            }
        }
        return true;
    }

    protected void loadDataSuccess() {
    }

    protected void loadDataError() {
        this.mLoadErrorView.setVisibility(0);
        this.mLoadingView.setVisibility(8);
    }

    public void onGetTaskRewards(String my_task_id, String appkey) {
        String strUrl = UrlConst.getNewerTaskRewardUrl(my_task_id, appkey);
        this.mShareTaskReward = appkey.equalsIgnoreCase(ShareAppDialog.PANDA_SHARE_TASK);
        this.m_request.send(strUrl, true, "GetTaskRewards");
    }

    public void onShareTask(final String my_task_id) {
        if (this.mShareDialog == null) {
            this.mShareDialog = new ShareAppDialog(this, R.style.simple_bubble_message_dialog);
            Window win = this.mShareDialog.getWindow();
            win.setGravity(51);
            LayoutParams params = new LayoutParams();
            params.copyFrom(win.getAttributes());
            Rect frame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            params.width = frame.width();
            params.height = frame.height();
            win.setAttributes(params);
            win.setWindowAnimations(0);
            this.mShareDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    if (TaskListActivity.this.mShareDialog.mShareFinished) {
                        //UmengStatic.StaticShareTaskEnd(TaskListActivity.this);
                        TaskListActivity.this.shareTaskDone(my_task_id);
                    }
                    TaskListActivity.this.mShareDialog = null;
                }
            });
        }
        this.mShareDialog.show();
//        UmengStatic.StaticShareTaskBegin(this);
    }

    private void shareTaskDone(String my_task_id) {
        try {
            String rid = String.valueOf(MyApplication.getInstance().GetLoginManager().GetUserInfo().rid);
            String authseq = LoginManager.getMD5(UUID.randomUUID().toString());
            this.m_request.send(UrlConst.getShareTaskDoneUrl(rid, my_task_id, getSign(my_task_id, rid, authseq), authseq), true, "GetShareTaskDone");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getSign(String taskid, String rid, String authseq) {
        StringBuilder key = new StringBuilder();
        key.append("__plat=android&__version=" + MyApplication.getInstance().version());
        key.append("&authseq=" + authseq);
        key.append("&pt_sign=" + CookieContiner.getPtSign());
        key.append("&rid=" + rid);
        key.append("&task_id=" + taskid);
        key.append("Api.M.PaNda.TV");
        key.append("pt_time=" + CookieContiner.getPtTime());
        return Md5Utils.getMD5(key.toString());
    }
}
