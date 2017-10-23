package com.panda.videoliveplatform.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import com.panda.videolivecore.CommonDefine;
import com.panda.videolivecore.SharePreference.SharePreferenceHelper;
//import com.panda.videolivecore.UpdateManager;
//import com.panda.videolivecore.net.LiveRoomRequest;
import com.panda.videolivecore.net.UrlConst;
import com.panda.videolivecore.net.http.HttpRequest;
import com.panda.videolivecore.net.http.IHttpRequestEvent;
//import com.panda.videolivecore.net.info.NewerTaskListInfo;
//import com.panda.videolivecore.net.info.NewerTaskListInfo.TaskListItem;
//import com.panda.videolivecore.net.info.ResultMsgInfo;
//import com.panda.videolivecore.net.info.UserInfo;
//import com.panda.videolivecore.network.DownloadAvatarImageTask;
//import com.panda.videolivecore.network.LoginManager;
//import com.panda.videolivecore.setting.SettingStorage;
import com.panda.videolivecore.view.CircleImageView;
import com.panda.videoliveplatform.MyApplication;
import com.panda.videoliveplatform.R;
//import com.panda.videoliveplatform.activity.AboutActivity;
//import com.panda.videoliveplatform.activity.FeedbackActivity;
//import com.panda.videoliveplatform.activity.FollowActivity;
//import com.panda.videoliveplatform.activity.LoginActivity;
//import com.panda.videoliveplatform.activity.TaskListActivity;
//import com.panda.videoliveplatform.activity.UserInfoActivity;
//import com.umeng.analytics.MobclickAgent;
//import com.umeng.message.PushAgent;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

public class AccountFragment extends Fragment implements IHttpRequestEvent {
    private final String MY_UNDOTASK_CHECKED = "MY_UNDOTASK_CHECKED";
    private final String MY_UNDOTASK_LAST_CHECK_TIME = "MY_UNDOTASK_LAST_CHECK_TIME";
    private final String MY_UNDOTASK_POINT_VISIBLE = "MY_UNDOTASK_POINT_VISIBLE";
    private View ParentView = null;
    private final String REQUEST_FUNC_REFESH_TASK_LIST = "RefreshTaskList";
    private final String REQUEST_FUNC_REFESH_ZHUZI = "RefreshZhuzi";
    private BroadcastReceiver mBroadcastReceiver = null;
    private CircleImageView mCircleimageview;
    //private LiveRoomRequest mHttpRequest;
    private OnAccountFragmentListener mListener;
    private View mLoginTextView;
    private ImageView mTaskPoint;
    private TextView mZhuziCountText;
    private View mZhuziView;
    private TextView m_accountUser;
    private HttpRequest m_request = null;
    private TextView txtVersion;

    public class BroadcastReceiver extends android.content.BroadcastReceiver {
        private static final String LOG_TAG = "LoginReceiver";

        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals(LoginManager.BROADCAST_LOGIN)) {
//                AccountFragment.this.refreshZhuzi();
//                AccountFragment.this.setUserHeaderControl();
//                AccountFragment.this.checkUndoTask();
//            } else if (action.equals(LoginManager.BROADCAST_LOGOUT)) {
//                AccountFragment.this.mZhuziView.setVisibility(8);
//                AccountFragment.this.mZhuziCountText.setText("0");
//                AccountFragment.this.setUserHeaderControl();
//                AccountFragment.this.showUndoPoint(false);
//            } else if (action.equals(CommonDefine.BROADCAST_BAMBOOS_CHANGED)) {
//                AccountFragment.this.refreshZhuzi();
//            }
        }
    }

    public interface OnAccountFragmentListener {
        void onAccountClickTest(int i);

        void setAccountPoint(boolean z);
    }

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //MobclickAgent.setDebugMode(false);
        View view = inflater.inflate(R.layout.fragment_account, null);
        this.ParentView = view;
        initViews(view);
        IntentFilter filter = new IntentFilter();
        //filter.addAction(LoginManager.BROADCAST_LOGIN);
        //filter.addAction(LoginManager.BROADCAST_LOGOUT);
        filter.addAction(CommonDefine.BROADCAST_BAMBOOS_CHANGED);
        this.mBroadcastReceiver = new BroadcastReceiver();
        getActivity().registerReceiver(this.mBroadcastReceiver, filter);
        checkUndoTask();
        return view;
    }

    public void refreshZhuzi() {
//        if (this.mHttpRequest == null) {
//            this.mHttpRequest = new LiveRoomRequest(this);
//        }
//        this.mHttpRequest.sendMyBamboos("RefreshZhuzi");
    }

    public boolean onResponse(boolean bResult, String strReponse, String strContext) {
//        JsonReader reader;
//        if (!strContext.equalsIgnoreCase("RefreshTaskList")) {
//            if (strContext.equalsIgnoreCase("RefreshZhuzi")) {
//                String count = "";
//                try {
//                    reader = new JsonReader(new InputStreamReader(new ByteArrayInputStream(strReponse.getBytes()), "UTF-8"));
//                    reader.beginObject();
//                    while (reader.hasNext()) {
//                        String name = reader.nextName();
//                        if (name.equalsIgnoreCase("errno")) {
//                            reader.nextInt();
//                        } else if (name.equalsIgnoreCase("errmsg")) {
//                            reader.nextString();
//                        } else if (name.equalsIgnoreCase("data")) {
//                            count = reader.nextString();
//                        } else {
//                            reader.skipValue();
//                        }
//                    }
//                    reader.endObject();
//                    reader.close();
//                    if (!count.isEmpty()) {
//                        UserInfo userinfo = MyApplication.getInstance().GetLoginManager().GetUserInfo();
//                        if (userinfo != null) {
//                            userinfo.bamboos = count;
//                        }
//                        this.mZhuziView.setVisibility(0);
//                        this.mZhuziCountText.setText(count);
//                    }
//                } catch (Exception e) {
//                    return false;
//                }
//            }
//        } else if (bResult) {
//            try {
//                ResultMsgInfo info = new ResultMsgInfo();
//                info.read(strReponse);
//                if (info.error == 0) {
//                    reader = new JsonReader(new InputStreamReader(new ByteArrayInputStream(new String(info.data).getBytes(HttpRequest.CHARSET_NAME_UTF8)), HttpRequest.CHARSET_NAME_UTF8));
//                    NewerTaskListInfo task_info = new NewerTaskListInfo();
//                    task_info.read(reader);
//                    reader.close();
//                    boolean has_undo = false;
//                    for (TaskListItem item : task_info.mTaskListData) {
//                        if (!item.done.equalsIgnoreCase("3")) {
//                            has_undo = true;
//                            break;
//                        }
//                    }
//                    SharePreferenceHelper.save("MY_UNDOTASK_CHECKED", Boolean.valueOf(true));
//                    SharePreferenceHelper.save("MY_UNDOTASK_LAST_CHECK_TIME", System.currentTimeMillis());
//                    SharePreferenceHelper.save("MY_UNDOTASK_POINT_VISIBLE", Boolean.valueOf(has_undo));
//                    if (has_undo) {
//                        showUndoPoint(true);
//                    }
//                }
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        }
        return true;
    }

    private void initViews(View view) {
        this.m_accountUser = (TextView) view.findViewById(R.id.account_text_item);
        this.mCircleimageview = (CircleImageView) view.findViewById(R.id.faceCategroyTabs);
        this.mZhuziView = view.findViewById(R.id.textblock);
        this.mZhuziCountText = (TextView) view.findViewById(R.id.zhuzi_count);
        this.mLoginTextView = view.findViewById(R.id.account_logintext_item);
        setUserHeaderControl();
        refreshZhuzi();
        view.findViewById(R.id.my_account_head).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AccountFragment.this.onClickUser();
            }
        });
        view.findViewById(R.id.my_account_attention).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AccountFragment.this.onClickAttention();
            }
        });
        view.findViewById(R.id.my_account_task).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AccountFragment.this.onClickTask();
                AccountFragment.this.showUndoPoint(false);
            }
        });
        view.findViewById(R.id.my_account_update).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AccountFragment.this.onClickUpdate(v);
            }
        });
        view.findViewById(R.id.my_account_feedback).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AccountFragment.this.onClickFeedback();
            }
        });
        view.findViewById(R.id.my_account_about).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AccountFragment.this.onClickAbout();
            }
        });
        this.mTaskPoint = (ImageView) view.findViewById(R.id.task_point);
        Switch SwithcBroad = (Switch) view.findViewById(R.id.BroadCastRemeber);
        //SwithcBroad.setChecked(SettingStorage.IsPlayingNotify());
        SwithcBroad.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //开播通知
//                if (isChecked) {
//                    PushAgent.getInstance(AccountFragment.this.ParentView.getContext()).enable();
//                } else {
//                    PushAgent.getInstance(AccountFragment.this.ParentView.getContext()).disable();
//                }
//                SettingStorage.SetPlayNotify(isChecked);
            }
        });
        this.txtVersion = (TextView) view.findViewById(R.id.txtVersion);
        this.txtVersion.setText("当前版本:" + MyApplication.getInstance().version());
    }

    private void onClickUser() {
        /*
        if (MyApplication.getInstance().GetLoginManager().IsLogin()) {
            startActivityForResult(new Intent(getActivity(), UserInfoActivity.class), LoginActivity.LOGIN_REQUEST_CODE);
        } else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.LOGIN_REQUEST_CODE);
        }
        */
    }

    private void onClickAttention() {
        /*
        if (MyApplication.getInstance().GetLoginManager().IsLogin()) {
            startActivityForResult(new Intent(getActivity(), FollowActivity.class), 0);
        } else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), 0);
        }
        */
    }

    private void onClickTask() {
        /*
        if (MyApplication.getInstance().GetLoginManager().IsLogin()) {
            startActivityForResult(new Intent(getActivity(), TaskListActivity.class), 0);
        } else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), 0);
        }
        */
    }

    private void onClickUpdate(View v) {
        //UpdateManager.checkUpdate(getActivity(), this.ParentView, false);
    }

    private void onClickFeedback() {
//        startActivity(new Intent(getActivity(), FeedbackActivity.class));
    }

    private void onClickAbout() {
        //startActivity(new Intent(getActivity(), AboutActivity.class));
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnAccountFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        if (this.mBroadcastReceiver != null) {
            getActivity().unregisterReceiver(this.mBroadcastReceiver);
        }
        this.mListener = null;
    }

    private void setUserHeaderControl() {
//        if (MyApplication.getInstance().GetLoginManager().IsLogin()) {
//            this.mZhuziView.setVisibility(0);
//            this.mLoginTextView.setVisibility(4);
//            UserInfo userinfo = MyApplication.getInstance().GetLoginManager().GetUserInfo();
//            this.m_accountUser.setText(userinfo.nickName);
//            if (DownloadAvatarImageTask.isAvatarFileNameExist(userinfo.avatar)) {
//                Drawable drawable = Drawable.createFromPath(DownloadAvatarImageTask.getFileNameFromUrl(userinfo.avatar));
//                if (drawable != null) {
//                    this.mCircleimageview.setImageDrawable(drawable);
//                    return;
//                }
//                return;
//            }
//            new DownloadAvatarImageTask(this.mCircleimageview).StartTask(userinfo.avatar);
//            return;
//        }
        this.mZhuziView.setVisibility(4);
        this.mLoginTextView.setVisibility(0);
        this.mCircleimageview.setImageDrawable(getResources().getDrawable(R.drawable.head_img_normal));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkUndoTask() {
//        if (MyApplication.getInstance().GetLoginManager().IsLogin()) {
//            long day = (System.currentTimeMillis() - SharePreferenceHelper.getLong("MY_UNDOTASK_LAST_CHECK_TIME", 0)) / 86400000;
//            if (!SharePreferenceHelper.getBoolean("MY_UNDOTASK_CHECKED", false) || day >= 3) {
//                if (this.m_request == null) {
//                    this.m_request = new HttpRequest(this);
//                }
//                this.m_request.send(UrlConst.getNewerTaskListUrl(), true, "RefreshTaskList");
//            } else if (SharePreferenceHelper.getBoolean("MY_UNDOTASK_POINT_VISIBLE", false)) {
//                showUndoPoint(true);
//            }
//        }
    }

    private void showUndoPoint(boolean show) {
        if (SharePreferenceHelper.getBoolean("MY_UNDOTASK_CHECKED", false)) {
            if (show) {
                this.mTaskPoint.setVisibility(0);
            } else {
                this.mTaskPoint.setVisibility(8);
                SharePreferenceHelper.save("MY_UNDOTASK_POINT_VISIBLE", Boolean.valueOf(false));
            }
            this.mListener.setAccountPoint(show);
        }
    }
}
