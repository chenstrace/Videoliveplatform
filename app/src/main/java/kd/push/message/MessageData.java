package kd.push.message;

import kd.push.utils.FormatUtil;

public class MessageData {
    private long appId;
    private byte[] body;
    private long messageId;

    public MessageData(long messageid, long appid, byte[] content) {
        this.messageId = messageid;
        this.appId = appid;
        this.body = content;
    }

    public long getMessageId() {
        return this.messageId;
    }

    public String getBody() {
        return new String(this.body);
    }

    public long getAppId() {
        return this.appId;
    }

    public byte[] getBodyBytes() {
        return this.body;
    }

    public int getBodyLength() {
        return this.body.length;
    }

    public byte[] toBytes() {
        int i;
        byte[] messageIdByte = FormatUtil.long2Stream(this.messageId);
        byte[] bodylenByte = FormatUtil.int2Stream(this.body.length);
        byte[] data = new byte[(this.body.length + 12)];
        for (i = 0; i < messageIdByte.length; i++) {
            data[(8 - messageIdByte.length) + i] = messageIdByte[i];
        }
        int offset = 8;
        for (i = 0; i < 4; i++) {
            data[offset + i] = bodylenByte[i];
            offset++;
        }
        for (i = 0; i < this.body.length; i++) {
            data[offset + i] = this.body[i];
            offset++;
        }
        return data;
    }
}
