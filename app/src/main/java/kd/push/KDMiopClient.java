package kd.push;

import android.util.Base64;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.UUID;
import kd.push.message.Message;
import kd.push.message.MessageInputStream;
import kd.push.message.MessageOutputStream;
import kd.push.utils.DesUtil;
import kd.push.utils.MD5Util;
import kd.push.utils.RSAUtil;

public class KDMiopClient {
    private String authType;
    private String bindId;
    private MessageInputStream in;
    private int keepaliveTimeout;
    private short miopVersion;
    private MessageOutputStream out;
    private String sign;
    private Socket socket;
    private String tsIn;

    public KDMiopClient(short miopVersion, int keepaliveTimeout, String bindId) {
        this.miopVersion = miopVersion;
        this.keepaliveTimeout = keepaliveTimeout;
        this.bindId = bindId;
    }

    public KDMiopClient(short miopVersion, int keepaliveTimeout, String bindId, String timestampIn, String sign, String authType) {
        this.miopVersion = miopVersion;
        this.keepaliveTimeout = keepaliveTimeout;
        this.bindId = bindId;
        this.tsIn = timestampIn;
        this.sign = sign;
        this.authType = authType;
    }

    protected boolean connect(String address) {
        try {
            String host;
            String port;
            String[] ip_port = address.split(":");
            if (ip_port.length == 2) {
                host = ip_port[0];
                port = ip_port[1];
            } else {
                host = address;
                port = "80";
            }
            this.socket = new Socket();
            this.socket.connect(new InetSocketAddress(host, Integer.parseInt(port)), 15000);
            this.in = new MessageInputStream(this.socket.getInputStream());
            this.out = new MessageOutputStream(this.socket.getOutputStream());
            Message connectMessage = new Message(this.miopVersion);
            String t = Integer.toString(this.keepaliveTimeout);
            String ts = Long.toString(System.currentTimeMillis());
            switch (this.miopVersion) {
                case (short) 2:
                case (short) 4:
                    String n = UUID.randomUUID().toString();
                    String priDes = UUID.randomUUID().toString().substring(0, 8);
                    String rkey = new String(Base64.encode(RSAUtil.encrypt(priDes.getBytes()), 0)).replace("\n", "");
                    String signid = new String(Base64.encode(DesUtil.encryptDES(("user=" + this.bindId + "&sign=" + MD5Util.encode("n" + n + "t" + t + "ts" + ts)).getBytes(), priDes.getBytes()), 0)).replace("\n", "");
                    connectMessage.addProperty("t", t);
                    connectMessage.addProperty("n", n);
                    connectMessage.addProperty("ts", ts);
                    connectMessage.addProperty("s", signid);
                    connectMessage.addProperty("r", rkey);
                    break;
                case (short) 3:
                    connectMessage.addProperty("t", t);
                    connectMessage.addProperty("u", this.bindId);
                    connectMessage.addProperty("ts", ts);
                    break;
                case (short) 5:
                    connectMessage.addProperty("u", this.bindId);
                    connectMessage.addProperty("ts", ts);
                    connectMessage.addProperty("t", t);
                    connectMessage.addProperty("op", String.valueOf(2));
                    break;
                case (short) 6:
                    connectMessage.addProperty("u", this.bindId);
                    connectMessage.addProperty("ts", this.tsIn);
                    connectMessage.addProperty("t", t);
                    connectMessage.addProperty("sign", this.sign);
                    connectMessage.addProperty("authtype", this.authType);
                    connectMessage.addProperty("op", String.valueOf(2));
                    break;
            }
            this.out.Write(connectMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected Message receiveMessage(int timeout) throws IOException {
        this.socket.setSoTimeout(timeout * 1000);
        return this.in.Read();
    }

    protected void sendAckMessage(String ackId) throws IOException {
        synchronized (this) {
            Message ackMessage = new Message(this.miopVersion);
            ackMessage.addProperty("ack", ackId);
            ackMessage.addProperty("op", String.valueOf(4));
            System.out.println("send message ack " + ackId);
            this.out.Write(ackMessage);
        }
    }

    protected void sendPindMessage() throws IOException {
        synchronized (this) {
            Message pingMessage = new Message(this.miopVersion);
            pingMessage.addProperty("op", String.valueOf(0));
            this.out.Write(pingMessage);
        }
    }
}
