package kd.push;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.panda.videolivecore.utils.LogUtils;
import java.util.List;
import kd.push.message.MessageData;
import kd.push.utils.QLogUtil;

public class KDPushService extends Service implements QHPushCallback {
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d("[XXX]", "KDPushService::onStartCommand: " + intent.getAction());
        if (intent != null) {
            QLogUtil.debugLog("PushService onStartCommand action:" + intent.getAction());
        }
        new Thread() {
            public void run() {
                super.run();
                KDPushClient.getInstance(KDPushService.this, KDPushService.this).starPush();
            }
        }.start();
        return 1;
    }

    public void connectionLost(Throwable cause) {
    }

    public void messageArrived(String userId, List<MessageData> messageDatasList) {
        for (MessageData messageData : messageDatasList) {
            String payload = messageData.getBody();
            Intent intent = new Intent(Constants.ACTION_RECEIVE_MESSAGE);
            intent.putExtra(Constants.KEY_MESSAGE_DATA, payload);
            intent.setPackage(getPackageName());
            sendBroadcast(intent);
        }
    }
}
