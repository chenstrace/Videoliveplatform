package kd.push;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;
import kd.push.utils.QLogUtil;
import kd.push.utils.VersionUtils;

public class WakeUpPushReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            QLogUtil.debugLog("WakeUpPushReceiver: " + action);
            Intent service = new Intent(context, KDPushService.class);
            service.setAction(action);
            context.startService(service);
            startPushAlarmManager(context);
        }
    }

    @TargetApi(19)
    private void startPushAlarmManager(Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService("alarm");
        PendingIntent pendIntent = getPendingIntent(context);
        alarmMgr.cancel(pendIntent);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(12, 3);
        long triggerAtTime = calendar.getTimeInMillis();
        if (VersionUtils.hasKitkat()) {
            alarmMgr.setExact(0, triggerAtTime, pendIntent);
        } else {
            alarmMgr.set(0, triggerAtTime, pendIntent);
        }
    }

    private PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, WakeUpPushReceiver.class);
        intent.setAction("AlarmManager");
        return PendingIntent.getBroadcast(context, 99, intent, 134217728);
    }
}
