package kd.push.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import kd.push.Constants;
import kd.push.utils.QLogUtil;

public class TPayloadReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                QLogUtil.debugLog("TPayloadReceiver json:" + bundle.getString(Constants.KEY_MESSAGE_DATA));
            }
        }
    }
}
