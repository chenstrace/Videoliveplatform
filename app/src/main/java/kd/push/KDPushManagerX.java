package kd.push;

public class KDPushManagerX {
    private static KDPushManagerX sKDPushManagerXinstance;
    private KDPushClientX mKDPushClientX = null;
    private String mLastAppId;

    public static KDPushManagerX getInstance() {
        if (sKDPushManagerXinstance == null) {
            sKDPushManagerXinstance = new KDPushManagerX();
        }
        return sKDPushManagerXinstance;
    }

    private KDPushManagerX() {
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void start(android.content.Context r10, java.lang.String r11, java.lang.String r12, java.lang.String[] r13, java.lang.String r14, java.lang.String r15, java.lang.String r16, kd.push.PushCallbackX r17) {

        //throw new UnsupportedOperationException("Method not decompiled: kd.push.KDPushManagerX.start(android.content.Context, java.lang.String, java.lang.String, java.lang.String[], java.lang.String, java.lang.String, java.lang.String, kd.push.PushCallbackX):void");
    }

    public void AddRoomIp(String[] roomip) {
        if (this.mKDPushClientX != null) {
            this.mKDPushClientX.AddRoomIp(roomip);
        }
    }

    public void stop() {
        synchronized (this) {
            if (this.mKDPushClientX != null) {
                this.mKDPushClientX.stopPush();
                this.mKDPushClientX = null;
            }
        }
    }

    public void checkConnection() {
        synchronized (this) {
            if (!(this.mKDPushClientX == null || this.mKDPushClientX.isRunning())) {
                new Thread() {
                    public void run() {
                        super.run();
                        KDPushManagerX.this.mKDPushClientX.starPush();
                    }
                }.start();
            }
        }
    }
}
