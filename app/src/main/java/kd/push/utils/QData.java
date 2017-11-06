package kd.push.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class QData {
    private static final String PERFERENCES_NAME = "qhopen_game_session_info";
    private static String TAG = "QData";

    public static void setPreference(Context ctx, String key, String value) {
        ctx.getSharedPreferences(PERFERENCES_NAME, 0).edit().putString(key, value).commit();
    }

    public static void setPreference(Context ctx, String key, long value) {
        ctx.getSharedPreferences(PERFERENCES_NAME, 0).edit().putLong(key, value).commit();
    }

    public static String getPreferenceString(Context ctx, String key, String defValue) {
        return ctx.getSharedPreferences(PERFERENCES_NAME, 0).getString(key, defValue);
    }

    public static long getPreferenceLong(Context ctx, String key, long defValue) {
        return ctx.getSharedPreferences(PERFERENCES_NAME, 0).getLong(key, defValue);
    }

    public static void setTotalSession(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PERFERENCES_NAME, 0);
        sp.edit().putLong("totalSession", sp.getLong("totalSession", 0) + 1).commit();
    }

    public static void setTodaySession(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PERFERENCES_NAME, 0);
        String day = getPreferenceString(ctx, "day", "");
        String current = QUtil.getCurrentDay();
        if (current.equals(day)) {
            sp.edit().putLong("todaySession", sp.getLong("todaySession", 0) + 1).commit();
            return;
        }
        sp.edit().putString("day", current).commit();
        sp.edit().putLong("todaySession", 1).commit();
    }

    public static void setChannel(Context ctx, String channel) {
        ctx.getSharedPreferences(PERFERENCES_NAME, 0).edit().putString("channel", channel).commit();
    }

    public static String getChannel(Context ctx, String defValue) {
        return ctx.getSharedPreferences(PERFERENCES_NAME, 0).getString("channel", defValue);
    }

    public static String getRegisterLog(Context ctx, String registerId) {
        String logData = "";
        try {
            JSONObject json = new JSONObject();
            json.put("header", new QHeader(ctx, registerId).convertJson());
            logData = QUtil.encode(json.toString());
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        } catch (Error err) {
            QLOG.error(TAG, err);
        }
        return logData;
    }

    public static synchronized String mergeLogData(Context ctx, String registerId, String tag) {
        String logData;
        synchronized (QData.class) {
            logData = "";
            try {
                if (TextUtils.isEmpty(tag)) {
                    tag = TAG;
                }
                String content = QUtil.decode(QUtil.getFileContent(QUtil.getLogFile(ctx)));
                JSONObject json = new JSONObject();
                JSONObject event = null;
                JSONArray currentEventArray = null;
                if (!(currentEventArray == null || currentEventArray.length() <= 0 || event == null)) {
                    event.put(QUtil.generateSession(ctx), currentEventArray);
                }
                if (event != null && event.length() > 0) {
                    json.put("event", event);
                }
                json.put("header", new QHeader(ctx, registerId).convertJson());
                logData = json.toString();
                String logFile = QUtil.getLogFile(ctx);
                QLOG.debug(tag, "logFile: " + logFile);
                QLOG.debug(tag, "logData: " + logData);
                logData = QUtil.encode(logData);
                QUtil.saveFile(logFile, logData);
            } catch (Exception ex) {
                QLOG.error(TAG, ex);
            } catch (Error err) {
                QLOG.error(TAG, err);
            }
        }
        return logData;
    }
}
