package kd.push;

import android.content.Context;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;
import java.util.List;
import kd.push.message.Message;
import kd.push.message.MessageData;
import kd.push.utils.QDefine;
import kd.push.utils.QLogUtil;

public class KDPushClientX {
    private String authType;
    private volatile Boolean isAddRoomIp = Boolean.valueOf(false);
    private boolean isPushEnabled = true;
    private boolean isRunningFlag;
    private int keepAliveTimeout = 300;
    private String mAppId;
    private String mBindId;
    private KDMiopClient mMiopClient;
    private String[] mRoomAddIp = null;
    private String[] mRoomIp;
    private String mUserId;
    private WeakReference<PushCallbackX> mWeakPushCallback;
    private String sign;
    private String tsIn;
    private short version = (short) 6;

    public KDPushClientX(Context ctx, String userId, String appId, String[] roomIp, String tsIn, String sign, String authType, PushCallbackX pushCallback) {
        this.mWeakPushCallback = new WeakReference(pushCallback);
        this.mUserId = userId;
        this.mAppId = appId;
        this.mRoomIp = roomIp;
        this.tsIn = tsIn;
        this.sign = sign;
        this.authType = authType;
        this.mBindId = getBindId(ctx);
    }

    public void AddRoomIp(String[] roomIp) {
        if (roomIp != null) {
            synchronized (this.isAddRoomIp) {
                this.mRoomAddIp = (String[]) roomIp.clone();
                this.isAddRoomIp = Boolean.valueOf(true);
            }
        }
    }

    private String getBindId(Context ctx) {
        String tokenID;
        if (this.mUserId == null || this.mUserId.isEmpty()) {
            tokenID = QDefine.getTokenID(ctx);
        } else {
            tokenID = this.mUserId;
        }
        return tokenID + "@" + this.mAppId;
    }

    protected String[] getRoomIp() {
        return this.mRoomIp;
    }

    protected boolean isRunning() {
        boolean bRunning;
        synchronized (this) {
            bRunning = this.isRunningFlag;
        }
        return bRunning;
    }

    protected void stopPush() {
        synchronized (this) {
            this.isPushEnabled = false;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void starPush() {

    }

    void checkBindAck(Message message) throws MiopBindException {
        String bindAckR = message.getPropery("r");
        if (!bindAckR.equalsIgnoreCase("0")) {
            throw new MiopBindException("KDPushClientX@handleMessage | Bind Failed | r:" + bindAckR, bindAckR);
        } else if (this.mWeakPushCallback.get() != null) {
            ((PushCallbackX) this.mWeakPushCallback.get()).connectionEstablished(this.mBindId);
        }
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
                        if (this.mWeakPushCallback.get() != null) {
                            ((PushCallbackX) this.mWeakPushCallback.get()).messageArrived(this.mBindId, messageDataList);
                        }
                        this.mMiopClient.sendAckMessage(message.getPropery("ack"));
                        break;
                    case 6:
                        checkBindAck(message);
                        break;
                    default:
                        break;
                }
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                if (pingFlag) {
                    QLogUtil.debugLog("KDPushClient send ping timeout:" + e.toString());
                    if (this.mWeakPushCallback.get() != null) {
                        ((PushCallbackX) this.mWeakPushCallback.get()).connectionLost(this.mBindId, e);
                    }
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
                        if (this.mWeakPushCallback.get() != null) {
                            ((PushCallbackX) this.mWeakPushCallback.get()).connectionLost(this.mBindId, Ie);
                        }
                        isConnected = false;
                    }
                }
            } catch (IOException e2) {
                e2.printStackTrace();
                QLogUtil.debugLog("KDPushClient socket error:" + e2.toString());
                if (this.mWeakPushCallback.get() != null) {
                    ((PushCallbackX) this.mWeakPushCallback.get()).connectionLost(this.mBindId, e2);
                }
                isConnected = false;
            } catch (MiopBindException e3) {
                if (this.mWeakPushCallback.get() != null) {
                    ((PushCallbackX) this.mWeakPushCallback.get()).connectionLost(this.mBindId, e3);
                }
                isConnected = false;
                this.isPushEnabled = false;
            }
            QLogUtil.debugLog("QHPushClient isConnected:" + isConnected);
        }
    }

    private boolean connect() {
        String[] address = getRoomIp();
        if (address == null || address.length == 0) {
            return false;
        }
        for (String addr : address) {
            if (this.mMiopClient.connect(addr)) {
                return true;
            }
        }
        return false;
    }
}
