package com.panda.videoliveplatform.chat;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.panda.videolivecore.CommonDefine;
import com.panda.videolivecore.data.EnterRoomState;
import com.panda.videolivecore.net.LiveRoomRequest;
import com.panda.videolivecore.net.http.IHttpRequestEvent;
import com.panda.videolivecore.net.info.ResultMsgInfo;
import com.panda.videolivecore.utils.CookieContiner;
import com.panda.videolivecore.utils.ImeUtils;
import com.panda.videolivecore.utils.ToastUtils;
import com.panda.videoliveplatform.MyApplication;
import com.panda.videoliveplatform.R;
//import com.panda.videoliveplatform.activity.LoginActivity;
//import com.panda.videoliveplatform.activity.TaskListActivity;
import com.panda.videoliveplatform.chat.Message.MsgReceiverType;
//import com.sina.weibo.sdk.utils.LogUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
//import tv.danmaku.ijk.media.player.IMediaPlayer;

public class ChatRoomView extends LinearLayout implements IHttpRequestEvent {
    private final String BROADCAST_COLOR = "#ff2846";
    private final String RECEIVE_COLOR = "#0291eb";
    private final String REQUEST_FUNC_GetMYBAMBOOS = "GetMyBamboos";
    private final String REQUEST_FUNC_SENDBAMBOOS = "SendBamboos";
    private final String REQUEST_FUNC_SENDGROUPMSG = "SendGroupMsg";
    private final String SEND_COLOR = "#24b3b8";
//    private MessageAdapter mAdapter;
    private Activity mContext;
    private int mDefaultBambooNum = 100;
//    private ArrayList<EmoticonBean> mEmoticonBeanList = null;
    private LinearLayout mGiftLayout;
    private TextView mGiveBambooNum = null;
//    private MessageInputToolBox mInputBox;
    private long mLastSendMsgTime = 0;
    private ListView mListView;
    public OnOperationListener mOnOperationListener;
    private EnterRoomState mRoomState;
    private LinearLayout mSelectBambooComb;
    private LiveRoomRequest m_request = new LiveRoomRequest(this);
    private List<Message> messages;

    public ChatRoomView(Activity context, EnterRoomState roomState) {
        super(context);
        this.mContext = context;
        this.mRoomState = roomState;
        InitView();
    }

    public void setDefaultBambooNum(int num) {
        this.mDefaultBambooNum = num;
        if (this.mGiveBambooNum != null) {
            this.mGiveBambooNum.setText(String.valueOf(num));
        }
    }

    public void HideInputBox() {
//        if (this.mInputBox != null) {
//            this.mInputBox.hide();
//        }
    }

    private void InitView() {
//        View container = LayoutInflater.from(this.mContext).inflate(R.layout.chat_room_view, this);
//        this.messages = new ArrayList();
//        this.mEmoticonBeanList = new ArrayList();
//        initMessageInputToolBox();
//        this.mListView = (ListView) container.findViewById(R.id.messageListview);
//        this.mAdapter = new MessageAdapter(this.mContext, this.messages, this.mEmoticonBeanList);
//        this.mListView.setAdapter(this.mAdapter);
//        this.mAdapter.notifyDataSetChanged();
//        this.mListView.setOnTouchListener(new OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                ChatRoomView.this.mInputBox.hide();
//                return false;
//            }
//        });
//        InitBamboo();
//        BroadcastMessage("服务器连接中...");
    }

    private void InitBamboo() {
//        this.mSelectBambooComb = (LinearLayout) findViewById(R.id.select_bamboo_comb);
//        this.mSelectBambooComb.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                View popupView = ((LayoutInflater) ChatRoomView.this.mContext.getSystemService("layout_inflater")).inflate(R.layout.select_bamboo_list, null);
//                SelectBambooPopupWindow popupWindow = new SelectBambooPopupWindow(ChatRoomView.this.mContext, ChatRoomView.this, popupView, -1, -1, true, ChatRoomView.this.mDefaultBambooNum);
//                popupWindow.setAnimationStyle(0);
//                popupWindow.showAtLocation(ChatRoomView.this, 17, 0, 0);
//            }
//        });
//        this.mGiveBambooNum = (TextView) findViewById(R.id.give_bamboo_num);
//        ((TextView) findViewById(R.id.give_bamboo_btn)).setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                ChatRoomView.this.m_request.sendBamboos(String.valueOf(ChatRoomView.this.mDefaultBambooNum), String.valueOf(ChatRoomView.this.mRoomState.mInfoExtend.hostInfo.rid), ChatRoomView.this.mRoomState.mRoomId, "SendBamboos");
//            }
//        });
//        ((TextView) findViewById(R.id.acquire_bamboo_btn)).setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                ChatRoomView.this.mContext.startActivityForResult(new Intent(ChatRoomView.this.mContext, TaskListActivity.class), TaskListActivity.TASK_LIST_REQUEST_CODE);
//            }
//        });
//        refreshBamboo();
    }

    public void refreshBamboo() {
        if (CookieContiner.isLogin()) {
            this.m_request.sendMyBamboos("GetMyBamboos");
        }
    }

    public void updateRoomInfo(EnterRoomState roomState) {
        this.mRoomState = roomState;
    }

    public void ReceiveMessage(String name, String content, MsgReceiverType receiver_type) {
        addMessageToListView(new Message(0, name + ":", "#0291eb", content, receiver_type));
    }

    public void ReceiveBambooMessage(String name, String content, MsgReceiverType receiver_type) {
        addMessageToListView(new Message(1, name, "#0291eb", content, receiver_type));
    }

    public void BroadcastMessage(String content) {
        addMessageToListView(new Message(0, content, "#ff2846", "", MsgReceiverType.MSG_RECEIVER_NORMAL));
    }

    private void addMessageToListView(Message message) {
//        List<Message> allmsg = this.mAdapter.getData();
//        allmsg.add(message);
//        int size = allmsg.size();
//        if (size > 230) {
//            this.mAdapter.setData(new ArrayList(allmsg.subList((size - 230) + 59, size - 1)));
//        }
//        this.mAdapter.notifyDataSetChanged();
//        this.mListView.setSelection(this.mAdapter.getCount() - 1);
    }

//    private void initMessageInputToolBox() {
//        this.mInputBox = (MessageInputToolBox) findViewById(R.id.messageInputToolBox);
//        this.mGiftLayout = (LinearLayout) findViewById(R.id.gift_layout);
//        this.mOnOperationListener = new OnOperationListener() {
//            public boolean send(String content) {
//                if (LoginActivity.showLogin(ChatRoomView.this.mContext, false)) {
//                    return false;
//                }
//                if (System.currentTimeMillis() - ChatRoomView.this.mLastSendMsgTime < 2000) {
//                    ToastUtils.show(ChatRoomView.this.mContext, "请稍后发言");
//                    return false;
//                }
//                ChatRoomView.this.addMessageToListView(new Message(Integer.valueOf(0), MyApplication.getInstance().GetLoginManager().GetUserDisplayName() + ":", "#24b3b8", content, MsgReceiverType.MSG_RECEIVER_NORMAL));
//                ChatRoomView.this.m_request.sendGroupMsg(ChatRoomView.this.mRoomState.mRoomId, content, "SendGroupMsg");
//                ChatRoomView.this.mLastSendMsgTime = System.currentTimeMillis();
//                return true;
//            }
//
//            public void SelectGift() {
//                if (!LoginActivity.showLogin(ChatRoomView.this.mContext, false)) {
//                    if (ChatRoomView.this.mGiftLayout.getVisibility() == 0) {
//                        ChatRoomView.this.mGiftLayout.setVisibility(8);
//                        return;
//                    }
//                    ChatRoomView.this.mGiftLayout.setVisibility(0);
//                    ImeUtils.hideSoftInputBox(ChatRoomView.this.mContext);
//                }
//            }
//
//            public void hideGiftLayout() {
//                ChatRoomView.this.mGiftLayout.setVisibility(8);
//            }
//
//            public void selectedFace(String content) {
//                String strBean = null;
//                for (int i = 0; i < ChatRoomView.this.mEmoticonBeanList.size(); i++) {
//                    EmoticonBean bean = (EmoticonBean) ChatRoomView.this.mEmoticonBeanList.get(i);
//                    if (bean.getIconUri().compareToIgnoreCase(content) == 0) {
//                        strBean = bean.getContent();
//                        break;
//                    }
//                }
//                if (!TextUtils.isEmpty(strBean) && ChatRoomView.this.mInputBox.getEditText().length() + strBean.length() <= 20) {
//                    int index = ChatRoomView.this.mInputBox.getEditText().getSelectionStart();
//                    Editable editable = ChatRoomView.this.mInputBox.getEditText().getEditableText();
//                    if (index < 0) {
//                        editable.append(strBean);
//                    } else {
//                        editable.insert(index, strBean);
//                    }
//                }
//            }
//
//            public void selectedFuncation(int index) {
//                switch (index) {
//                }
//            }
//        };
//        this.mInputBox.setOnOperationListener(this.mOnOperationListener);
//    }

    public boolean onResponse(boolean bResult, String strReponse, String strContext) {
        return false;
//        if ("SendGroupMsg" == strContext) {
//            if (bResult) {
//                try {
//                    int errno = new JSONObject(strReponse).getInt("errno");
//                    if (errno != 0) {
//                        if (errno == IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING) {
//                            ToastUtils.show(this.mContext, this.mContext.getString(R.string.notify_send_message_refuse));
//                        } else {
//                            ToastUtils.show(this.mContext, this.mContext.getString(R.string.notify_send_message_fail));
//                        }
//                    }
//                } catch (Exception e) {
//                    ToastUtils.show(this.mContext, this.mContext.getString(R.string.notify_send_message_fail));
//                    //LogUtil.e("ChatRoomView", e.toString());
//                }
//            } else {
//                ToastUtils.show(this.mContext, this.mContext.getString(R.string.notify_send_message_fail));
//            }
//        } else if ("GetMyBamboos" == strContext) {
//            String strBamboos = "0";
//            if (bResult) {
//                info = new ResultMsgInfo();
//                r8 = this.m_request;
//                strBamboos = LiveRoomRequest.readBamboos(strReponse, info);
//                if (strBamboos == null || strBamboos.isEmpty()) {
//                    strBamboos = "0";
//                }
//            }
//            MyApplication.getInstance().GetLoginManager().GetUserInfo().bamboos = strBamboos;
//            ((TextView) findViewById(R.id.my_bamboo_num)).setText(strBamboos);
//        } else if ("SendBamboos" == strContext) {
//            if (bResult) {
//                info = new ResultMsgInfo();
//                r8 = this.m_request;
//                String consume = LiveRoomRequest.readBamboos(strReponse, info);
//                try {
//                    if (!TextUtils.isEmpty(consume) && Integer.parseInt(consume) > 0) {
//                        this.m_request.sendMyBamboos("GetMyBamboos");
//                        Intent intent = new Intent(CommonDefine.BROADCAST_BAMBOOS_CHANGED);
//                        intent.setPackage(this.mContext.getPackageName());
//                        this.mContext.sendBroadcast(intent);
//                    } else if (!TextUtils.isEmpty(info.errmsg)) {
//                        ToastUtils.show(this.mContext, info.errmsg);
//                    }
//                } catch (Exception e2) {
//                }
//            } else {
//                ToastUtils.show(this.mContext, this.mContext.getResources().getString(R.string.fail_for_network_error));
//            }
//        }
//        return false;
    }
}
