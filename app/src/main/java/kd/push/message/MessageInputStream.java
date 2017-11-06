package kd.push.message;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import kd.push.utils.FormatUtil;

public class MessageInputStream {
    private DataInputStream in;

    public MessageInputStream(InputStream in) {
        this.in = new DataInputStream(in);
    }

    public Message Read() throws IOException {
        byte[] header = new byte[4];
        this.in.readFully(header);
        short version = FormatUtil.stream2Short(header, 0);
        short opCode = FormatUtil.stream2Short(header, 2);
        if (opCode == (short) 1 || opCode == (short) 0 || opCode == (short) 7) {
            Message message = new Message(version);
            message.addProperty("op", String.valueOf(opCode));
            return message;
        }
        int i;
        byte[] proper = new byte[2];
        this.in.readFully(proper);
        short proplen = FormatUtil.stream2Short(proper, 0);
        Message message = new Message(version);
        message.addProperty("op", String.valueOf(opCode));
        if (proplen > (short) 0) {
            byte[] propData = new byte[proplen];
            this.in.readFully(propData);
            String[] properties = new String(propData, "UTF-8").split("\n");
            for (String split : properties) {
                String[] keyValues = split.split(":");
                if (keyValues.length == 2) {
                    message.addProperty(keyValues[0], keyValues[1]);
                }
            }
        }
        if (opCode == (short) 4 || opCode == (short) 6) {
            return message;
        }
        byte[] dataByte = new byte[4];
        this.in.readFully(dataByte);
        int datalen = FormatUtil.stream2Int(dataByte, 0);
        if (datalen <= 0) {
            return message;
        }
        byte[] contentByte = new byte[datalen];
        this.in.readFully(contentByte);
        int offset = 0;
        while (offset < datalen) {
            long messageid = FormatUtil.stream2Long(contentByte, offset);
            offset += 8;
            int appid = FormatUtil.stream2Int(contentByte, offset);
            offset += 4;
            int bodylen = FormatUtil.stream2Int(contentByte, offset);
            offset += 4;
            byte[] body = new byte[bodylen];
            if (bodylen > 0) {
                for (i = 0; i < bodylen; i++) {
                    body[i] = contentByte[i + offset];
                }
            }
            offset += bodylen;
            message.addMessageData(new MessageData(messageid, (long) appid, body));
        }
        return message;
    }
}
