package kd.push.utils;

import android.os.Environment;
import android.text.TextUtils;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Calendar;

public class QLogUtil {
    public static final String TAG = QLOG.class.getSimpleName();

    public static void debugLog(String content) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(1);
        int month = calendar.get(2);
        int day = calendar.get(5);
        int hour = calendar.get(11);
        int minute = calendar.get(12);
        int second = calendar.get(13);
        int millisecond = calendar.get(14);
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append(year);
        logBuilder.append("/");
        logBuilder.append(month + 1);
        logBuilder.append("/");
        logBuilder.append(day);
        logBuilder.append("\t\t");
        logBuilder.append(hour);
        logBuilder.append(":");
        logBuilder.append(minute);
        logBuilder.append(":");
        logBuilder.append(second);
        logBuilder.append(".");
        logBuilder.append(millisecond);
        logBuilder.append("\r\n");
        logBuilder.append(content);
        logBuilder.append("\r\n\r\n");
        String text = logBuilder.toString();
        saveLogToSD(text);
        QLOG.error(TAG, text);
    }

    public static boolean saveLogToSD(String log) {
        if (isMounted() && !TextUtils.isEmpty(log)) {
            log = "\n\t" + log;
            try {
                String dirPath = getFileDir();
                File dir = new File(dirPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dirPath, "log.txt");
                if (!file.exists()) {
                    file.createNewFile();
                }
                RandomAccessFile randomFile = new RandomAccessFile(file.getPath(), "rw");
                randomFile.seek(randomFile.length());
                randomFile.writeBytes(log);
                randomFile.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean isMounted() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    private static String getFileDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "360/kdpush/log" + File.separator;
    }
}
