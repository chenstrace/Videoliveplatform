package com.panda.videoliveplatform.chat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.panda.videolivecore.data.EnterRoomState;
import com.panda.videolivecore.data.ImageCacheManager;
import com.panda.videolivecore.net.LiveRoomRequest;
import com.panda.videolivecore.net.UserRequest;
import com.panda.videolivecore.net.http.HttpRequest;
import com.panda.videolivecore.net.http.IHttpRequestEvent;
import com.panda.videolivecore.net.info.ResultMsgInfo;
import com.panda.videolivecore.utils.CookieContiner;
import com.panda.videoliveplatform.R;
//import com.panda.videoliveplatform.activity.LoginActivity;

public class ChatRoomHostInfoView extends LinearLayout implements IHttpRequestEvent {
    private final String REQUEST_FUNC_CANCELFOLLOWROOM = "CancelFollowRoom";
    private final String REQUEST_FUNC_DOWNHOSTHEADER = "DownHostHeader";
    private final String REQUEST_FUNC_GETFOLLOWINFO = "GetFollowInfo";
    private final String REQUEST_FUNC_SETFOLLOWROOM = "SetFollowRoom";
    private Activity mContext;
    private Drawable mDefaultDrawable;
    private EnterRoomStateEvent mEvent;
    private LinearLayout mFollowBtn;
    private boolean mHasFollowed;
    private TextView mHostPeople;
    private ImageView mImgFollow;
    private ImageView mImgHostHeader;
    private TextView mRoomBulletin;
    private EnterRoomState mRoomState;
    private TextView mTxtBamboo;
    private TextView mTxtFollow;
    private TextView mTxtHostName;
    private TextView mTxtRoomName;
    private final UserRequest m_reqUser = new UserRequest(this);
    private final LiveRoomRequest m_request = new LiveRoomRequest(this);

    public ChatRoomHostInfoView(Activity context, EnterRoomState roomState, EnterRoomStateEvent event) {
        super(context);
        this.mContext = context;
        this.mRoomState = roomState;
        this.mEvent = event;
        this.mHasFollowed = false;
        this.mDefaultDrawable = context.getResources().getDrawable(R.drawable.head_img_press);
        InitView();
        refreshFollowState();
    }

    private void InitView() {
        View container = LayoutInflater.from(this.mContext).inflate(R.layout.chat_room_host_info, this);
        this.mImgFollow = (ImageView) container.findViewById(R.id.img_follow);
        this.mTxtFollow = (TextView) container.findViewById(R.id.txt_follow);
        this.mFollowBtn = (LinearLayout) container.findViewById(R.id.add_follow);
//        this.mFollowBtn.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                if (!LoginActivity.showLogin(ChatRoomHostInfoView.this.mContext, false)) {
//                    if (ChatRoomHostInfoView.this.mHasFollowed) {
//                        new FollowDeleteWindow(ChatRoomHostInfoView.this.mContext.findViewById(R.id.root), ChatRoomHostInfoView.this.mContext, ChatRoomHostInfoView.this.mRoomState).Show("CancelFollowRoom", ChatRoomHostInfoView.this);
//                    } else {
//                        ChatRoomHostInfoView.this.m_request.sendSetFollowRoom(ChatRoomHostInfoView.this.mRoomState.mRoomId, "SetFollowRoom");
//                    }
//                }
//            }
//        });
        this.mImgHostHeader = (ImageView) container.findViewById(R.id.img_host_header);
        this.mTxtHostName = (TextView) container.findViewById(R.id.txt_host_name);
        this.mTxtRoomName = (TextView) container.findViewById(R.id.txt_room_name);
        this.mTxtBamboo = (TextView) container.findViewById(R.id.txt_bamboo);
        this.mHostPeople = (TextView) container.findViewById(R.id.txt_host_people);
        this.mRoomBulletin = (TextView) container.findViewById(R.id.txt_room_bulletin);
        this.mRoomBulletin.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public void refreshFollowState() {
        if (CookieContiner.isLogin()) {
            this.m_request.sendGetFollowInfo(this.mRoomState.mRoomId, "GetFollowInfo");
        }
    }

    public void updateRoomInfo(EnterRoomState roomState) {
        this.mRoomState = roomState;
        if (!this.mRoomState.mInfoExtend.hostInfo.avatar.equals("")) {
            ImageCacheManager.loadImage(this.mRoomState.mInfoExtend.hostInfo.avatar, ImageCacheManager.getImageListener(this.mImgHostHeader, this.mDefaultDrawable, this.mDefaultDrawable));
        }
        this.mTxtHostName.setText(this.mRoomState.mInfoExtend.hostInfo.name);
        this.mTxtRoomName.setText(this.mRoomState.mInfoExtend.roomInfo.name);
        this.mRoomBulletin.setText(this.mRoomState.mInfoExtend.roomInfo.bulletin);
        updateFansText();
        updateHostBamboo(this.mRoomState.mInfoExtend.hostInfo.bamboos);
    }

    public boolean onResponse(boolean bResult, String strReponse, String strContext) {
        if ("GetFollowInfo" == strContext) {
            if (bResult) {
                updateFollowState(new ResultMsgInfo().readDataBoolean(strReponse));
            }
        } else if ("SetFollowRoom" == strContext) {
            if (bResult) {
                boolean bSucces = new ResultMsgInfo().readDataBoolean(strReponse);
                updateFollowState(true);
                if (bSucces) {
                    this.mRoomState.mInfoExtend.roomInfo.setFans(this.mRoomState.mInfoExtend.roomInfo.getFans() + 1);
                    updateFansText();
                }
            }
        } else if ("CancelFollowRoom" == strContext) {
            if (bResult) {
                boolean bSucces = new ResultMsgInfo().readDataBoolean(strReponse);
                updateFollowState(false);
                if (bSucces) {
                    this.mRoomState.mInfoExtend.roomInfo.setFans(this.mRoomState.mInfoExtend.roomInfo.getFans() - 1);
                    updateFansText();
                }
            }
        } else if ("DownHostHeader" == strContext) {
            onDownHostHeader(bResult, HttpRequest.String2Bytes(strReponse));
        }
        return false;
    }

    private void DownloadHostHeader(String strUrl) {
        this.m_reqUser.sendDownloadImage(strUrl, "DownHostHeader");
    }

    private void onDownHostHeader(boolean bResult, byte[] data) {
        if (bResult && data != null) {
            try {
                if (data.length < 8388608) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    if (bitmap != null) {
                        this.mImgHostHeader.setImageBitmap(bitmap);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateFollowState(boolean bHasFollowed) {
        this.mHasFollowed = bHasFollowed;
        if (this.mHasFollowed) {
            this.mImgFollow.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.ico_already_follow));
            this.mTxtFollow.setText("已关注");
            this.mTxtFollow.setTextColor(getContext().getResources().getColor(R.color.hostname_already_follow_text_color));
            this.mFollowBtn.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_already_follow));
            return;
        }
        this.mImgFollow.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.ico_follow));
        this.mTxtFollow.setText("立即关注");
        this.mTxtFollow.setTextColor(getContext().getResources().getColor(R.color.hostname_follow_text_color));
        this.mFollowBtn.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.blue_rect_btn));
    }

    private void updateFansText() {
        this.mHostPeople.setText(this.mRoomState.mInfoExtend.roomInfo.getFansTextFormat());
        this.mEvent.onChangeRoomInfo(this.mRoomState.mInfoExtend.roomInfo);
    }

    public void updateHostBamboo(String bamboos) {
        float bamboosFloat;
        long bamboosNum = 0;
        try {
            bamboosNum = Long.parseLong(bamboos);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String unit = "m";
        if (bamboosNum <= 1000000) {
            bamboosFloat = ((float) bamboosNum) / 1000.0f;
            unit = "mm";
        } else if (bamboosNum < 1000000000) {
            bamboosFloat = ((float) bamboosNum) / 1000000.0f;
            unit = "m";
        } else {
            bamboosFloat = ((float) bamboosNum) / 1.0E9f;
            unit = "km";
        }
        this.mTxtBamboo.setText("身高：" + (((float) Math.round(bamboosFloat * 100.0f)) / 100.0f) + unit);
    }
}
