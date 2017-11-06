package kd.push;

import android.content.Context;
import com.panda.videolivecore.utils.LogUtils;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import kd.push.message.Message;
import kd.push.message.MessageData;
import kd.push.utils.QDefine;
import kd.push.utils.QLogUtil;

public class KDPushClient {
    private static KDPushClient ourInstance;
    private boolean isPushEnabled = true;
    private boolean isRunningFlag;
    private int keepAliveTimeout = 300;
    private String mAppId;
    private KDMiopClient mMiopClient;
    private String mProduct;
    private QHPushCallback mPushCallback;
    private String mUserId;
    private short version = (short) 6;

    protected static synchronized KDPushClient getInstance(Context mContext, QHPushCallback mPushCallback) {
        KDPushClient kDPushClient;
        synchronized (KDPushClient.class) {
            if (ourInstance == null) {
                ourInstance = new KDPushClient(mContext, mPushCallback);
            }
            kDPushClient = ourInstance;
        }
        return kDPushClient;
    }

    private KDPushClient(Context mContext, QHPushCallback mPushCallback) {
        this.mPushCallback = mPushCallback;
        this.mUserId = getUserId(mContext);
        QLogUtil.debugLog("KDPushClient mUserId:" + this.mUserId);
        this.mProduct = QDefine.getMetaData(mContext, Constants.QHOPENSDK_PRODUCT);
    }

    private static String getUserId(Context ctx) {
        return QDefine.getTokenID(ctx) + "@" + QDefine.getMetaData(ctx, Constants.QHOPENSDK_APPID);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void starPush() {
        /*
        r5 = this;
        monitor-enter(r5);
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0049 }
        r1.<init>();	 Catch:{ all -> 0x0049 }
        r2 = "KDPushClient starPush isRunningFlag:";
        r1 = r1.append(r2);	 Catch:{ all -> 0x0049 }
        r2 = r5.isRunningFlag;	 Catch:{ all -> 0x0049 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x0049 }
        r1 = r1.toString();	 Catch:{ all -> 0x0049 }
        kd.push.utils.QLogUtil.debugLog(r1);	 Catch:{ all -> 0x0049 }
        r1 = r5.isRunningFlag;	 Catch:{ all -> 0x0049 }
        if (r1 == 0) goto L_0x001f;
    L_0x001d:
        monitor-exit(r5);	 Catch:{ all -> 0x0049 }
    L_0x001e:
        return;
    L_0x001f:
        r1 = 1;
        r5.isRunningFlag = r1;	 Catch:{ all -> 0x0049 }
        monitor-exit(r5);	 Catch:{ all -> 0x0049 }
        r1 = r5.mMiopClient;
        if (r1 != 0) goto L_0x0034;
    L_0x0027:
        r1 = new kd.push.KDMiopClient;
        r2 = r5.version;
        r3 = r5.keepAliveTimeout;
        r4 = r5.mUserId;
        r1.<init>(r2, r3, r4);
        r5.mMiopClient = r1;
    L_0x0034:
        r1 = r5.isPushEnabled;
        if (r1 == 0) goto L_0x0050;
    L_0x0038:
        r1 = r5.connect();
        if (r1 != 0) goto L_0x004c;
    L_0x003e:
        r2 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        java.lang.Thread.sleep(r2);	 Catch:{ InterruptedException -> 0x0044 }
        goto L_0x0034;
    L_0x0044:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0034;
    L_0x0049:
        r1 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0049 }
        throw r1;
    L_0x004c:
        r5.handleMessage();
        goto L_0x0034;
    L_0x0050:
        monitor-enter(r5);
        r1 = 0;
        r5.isRunningFlag = r1;	 Catch:{ all -> 0x0056 }
        monitor-exit(r5);	 Catch:{ all -> 0x0056 }
        goto L_0x001e;
    L_0x0056:
        r1 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0056 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: kd.push.KDPushClient.starPush():void");
    }

    private void handleMessage() {
        boolean pingFlag = false;
        boolean isConnected = true;
        while (this.isPushEnabled && isConnected) {
            try {
                Message message = this.mMiopClient.receiveMessage(pingFlag ? 30 : this.keepAliveTimeout);
                pingFlag = false;
                int opCode = Integer.parseInt(message.getPropery("op"));
                QLogUtil.debugLog("KDPushClient opcode: " + opCode);
                switch (opCode) {
                    case 3:
                        List<MessageData> messageDataList = message.getMessageDatas();
                        if (this.mPushCallback != null) {
                            this.mPushCallback.messageArrived(this.mUserId, messageDataList);
                        }
                        this.mMiopClient.sendAckMessage(message.getPropery("ack"));
                        break;
                    default:
                        break;
                }
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                if (pingFlag) {
                    QLogUtil.debugLog("KDPushClient send ping timeout:" + e.toString());
                    this.mPushCallback.connectionLost(e);
                    isConnected = false;
                } else {
                    try {
                        QLogUtil.debugLog("KDPushClient wait message timeout:" + e.toString());
                        QLogUtil.debugLog("KDPushClient sendPindMessage");
                        this.mMiopClient.sendPindMessage();
                        pingFlag = true;
                    } catch (IOException Ie) {
                        Ie.printStackTrace();
                        QLogUtil.debugLog("KDPushClient sendPindMessage failed :" + Ie.toString());
                        this.mPushCallback.connectionLost(Ie);
                        isConnected = false;
                    }
                }
            } catch (IOException e2) {
                e2.printStackTrace();
                QLogUtil.debugLog("KDPushClient socket error:" + e2.toString());
                this.mPushCallback.connectionLost(e2);
                isConnected = false;
            }
            QLogUtil.debugLog("QHPushClient isConnected:" + isConnected);
        }
    }

    private boolean connect() {
        String address = getRoomIpFromDispatcher();
        LogUtils.d("[XXX]", "KDPushClient::connect | address : " + address);
        return this.mMiopClient.connect(address);
    }

    private String getRoomIpFromDispatcher() {
        return "218.30.118.71:443";
    }
}
