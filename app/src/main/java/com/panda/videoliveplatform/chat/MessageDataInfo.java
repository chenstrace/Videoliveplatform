package com.panda.videoliveplatform.chat;

import com.panda.videoliveplatform.chat.Message.MsgReceiverType;

public class MessageDataInfo {
    public String contentData = "";
    public String currentId = "";
    public String dataType = "";
    public String fromUserNick = "";
    public String recvId = "";
    public String toRoomId = "";
    public MsgReceiverType userType = MsgReceiverType.MSG_RECEIVER_NORMAL;
}
