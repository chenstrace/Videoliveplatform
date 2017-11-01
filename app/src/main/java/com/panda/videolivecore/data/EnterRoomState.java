package com.panda.videolivecore.data;

import com.panda.videolivecore.net.info.EnterRoomInfo;
import com.panda.videolivecore.net.info.ResultMsgInfo;

public class EnterRoomState {
    public ResultMsgInfo mInfo = new ResultMsgInfo();
    public EnterRoomInfo mInfoExtend = new EnterRoomInfo();
    public boolean mResult = false;
    public String mRoomId = "0";

    public EnterRoomState(String strRoomId) {
        this.mRoomId = strRoomId;
    }

    public void update(boolean bResult, ResultMsgInfo info, EnterRoomInfo infoExtend) {
        this.mResult = bResult;
        this.mInfo = info;
        this.mInfoExtend = infoExtend;
    }
}
