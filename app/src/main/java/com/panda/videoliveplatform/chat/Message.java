package com.panda.videoliveplatform.chat;

public class Message {
    public static final int MSG_TYPE_FACE = 2;
    public static final int MSG_TYPE_GIFT = 1;
    public static final int MSG_TYPE_PHOTO = 3;
    public static final int MSG_TYPE_TEXT = 0;
    private String contentText;
    private MsgReceiverType msg_owner;
    private String nameColor;
    private Integer type;
    private String userName;

    public enum MsgReceiverType {
        MSG_RECEIVER_NORMAL,
        MSG_RECEIVER_ROOM_ADMIN,
        MSG_RECEIVER_ROOM_SUPER_ADMIN,
        MSG_RECEIVER_ROOM_OWNER,
        MSG_RECEIVER_HEADER_MASTER
    }

    public Message(Integer type, String userName, String nameColor, String contentText, MsgReceiverType msg_owner) {
        this.type = type;
        this.userName = userName;
        this.nameColor = nameColor;
        this.contentText = contentText;
        this.msg_owner = msg_owner;
    }

    public Integer getType() {
        return this.type;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getNameColor() {
        return this.nameColor;
    }

    public String getContentText() {
        return this.contentText;
    }

    public MsgReceiverType getMsgOwner() {
        return this.msg_owner;
    }
}
