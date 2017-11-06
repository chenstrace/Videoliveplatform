package kd.push.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kd.push.utils.FormatUtil;

public class Message {
    public static final short MIOP5 = (short) 5;
    public static final short NEED_ACK_AND_ENCRYPT = (short) 4;
    public static final short ONLY_NEED_ACK = (short) 3;
    public static final short ONLY_NEED_ENCRYPT = (short) 2;
    public static final short OpcodeBind = (short) 2;
    public static final short OpcodeBindAck = (short) 6;
    public static final short OpcodeMsg = (short) 3;
    public static final short OpcodeMsgAck = (short) 4;
    public static final short OpcodePing = (short) 0;
    public static final short OpcodePong = (short) 1;
    public static final short OpcodeUnbind = (short) 5;
    public static final short OpcodeUnbindAck = (short) 7;
    private List<MessageData> messageDatas;
    private Map<String, String> properties;
    private short version;

    public Message(short version) {
        this.version = version;
        this.properties = new HashMap();
        this.messageDatas = new ArrayList();
    }

    public Message() {
        this.version = (short) 3;
        this.properties = new HashMap();
        this.messageDatas = new ArrayList();
    }

    public void addProperty(String key, String value) {
        this.properties.put(key, value);
    }

    public String getPropery(String key) {
        return (String) this.properties.get(key);
    }

    public boolean isExistPropery(String key) {
        return this.properties.containsKey(key);
    }

    protected void addMessageData(MessageData messageData) {
        this.messageDatas.add(messageData);
    }

    public List<MessageData> getMessageDatas() {
        return this.messageDatas;
    }

    public byte[] toByte() {
        byte[] stream;
        short opCode = Short.valueOf(getPropery("op")).shortValue();
        this.properties.remove("op");
        byte[] version;
        int i;
        int offset;
        byte[] opCodeByte;
        if (opCode == (short) 0) {
            stream = new byte[4];
            version = FormatUtil.short2Stream(this.version);
            for (i = 0; i < 2; i++) {
                stream[0 + i] = version[i];
            }
            offset = 0 + 2;
            opCodeByte = FormatUtil.short2Stream(opCode);
            for (i = 0; i < 2; i++) {
                stream[i + 2] = opCodeByte[i];
            }
        } else {
            int len;
            byte[] prop = properties2Byte();
            byte[] data = messageData2Byte();
            if (data.length == 0) {
                len = (prop.length + 6) + data.length;
            } else {
                len = (prop.length + 10) + data.length;
            }
            stream = new byte[len];
            version = FormatUtil.short2Stream(this.version);
            for (i = 0; i < 2; i++) {
                stream[0 + i] = version[i];
            }
            offset = 0 + 2;
            opCodeByte = FormatUtil.short2Stream(opCode);
            for (i = 0; i < 2; i++) {
                stream[i + 2] = opCodeByte[i];
            }
            offset += 2;
            byte[] proplen = FormatUtil.short2Stream((short) prop.length);
            for (i = 0; i < 2; i++) {
                stream[i + 4] = proplen[i];
            }
            offset += 2;
            for (i = 0; i < prop.length; i++) {
                stream[i + 6] = prop[i];
            }
            offset = prop.length + 6;
            if (data.length != 0) {
                byte[] datalen = FormatUtil.int2Stream(data.length);
                for (i = 0; i < 4; i++) {
                    stream[offset + i] = datalen[i];
                }
                offset += 4;
                for (i = 0; i < data.length; i++) {
                    stream[offset + i] = data[i];
                }
                offset += data.length;
            }
        }
        return stream;
    }

    private byte[] properties2Byte() {
        String prop = "";
        for (String key : this.properties.keySet()) {
            if (prop != "") {
                prop = prop + "\n";
            }
            prop = prop + key + ":" + ((String) this.properties.get(key));
        }
        if (prop == "") {
            return new byte[0];
        }
        return prop.getBytes();
    }

    private byte[] messageData2Byte() {
        int i;
        int len = 0;
        for (i = 0; i < this.messageDatas.size(); i++) {
            len += ((MessageData) this.messageDatas.get(i)).toBytes().length;
        }
        byte[] messageDataByte = new byte[len];
        int offset = 0;
        for (i = 0; i < this.messageDatas.size(); i++) {
            for (byte b : ((MessageData) this.messageDatas.get(i)).toBytes()) {
                messageDataByte[offset] = b;
                offset++;
            }
        }
        return messageDataByte;
    }

    public short getVersion() {
        return this.version;
    }
}
