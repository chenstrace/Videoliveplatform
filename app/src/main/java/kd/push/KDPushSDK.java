package kd.push;

import android.content.Context;
import android.content.Intent;

public class KDPushSDK {
    public static void start(Context context) {
        Intent intent = new Intent(context, WakeUpPushReceiver.class);
        intent.setAction(Constants.ACTION_START_PUSH);
        context.sendBroadcast(intent);
    }

    public static void setLogEnable(boolean isLogEnable) {
        Constants.logEnable = isLogEnable;
    }
}
