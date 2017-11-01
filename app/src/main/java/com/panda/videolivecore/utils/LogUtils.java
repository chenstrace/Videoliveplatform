package com.panda.videolivecore.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

public class LogUtils {
    public static String customTagPrefix = "";
    public static boolean isSaveLog = false;
    private static String logPath = "";
    private static boolean sIsDebug = false;
    public static boolean sQueryLogSwitch = false;

    public static class FunctionTracer {
        private String functionName;
        private long lastKickLine = 0;
        private String shortFunctionName;
        private long timeLastKick = 0;
        private long timeStart = 0;

        public FunctionTracer() {
            if (LogUtils.isDebug()) {
                this.timeStart = System.currentTimeMillis();
                this.timeLastKick = this.timeStart;
                StackTraceElement stackTraceElem = Thread.currentThread().getStackTrace()[4];
                this.lastKickLine = (long) stackTraceElem.getLineNumber();
                this.functionName = stackTraceElem.toString();
                String className = stackTraceElem.getClassName();
                className = className.substring(className.lastIndexOf(".") + 1);
                this.shortFunctionName = String.format("%s.%s", new Object[]{className, stackTraceElem.getMethodName()});
                Log.i("FunctionTracer", "进入 " + this.functionName);
            }
        }

        public static FunctionTracer enter() {
            return new FunctionTracer();
        }

        public void kick() {
            if (LogUtils.isDebug()) {
                int currentLine = Thread.currentThread().getStackTrace()[3].getLineNumber();
                long duration = System.currentTimeMillis() - this.timeLastKick;
                this.timeLastKick = System.currentTimeMillis();
                Log.i("FunctionTracer", this.shortFunctionName + ": 从" + this.lastKickLine + "行到" + currentLine + "行，用时" + duration + "毫秒");
                this.lastKickLine = (long) currentLine;
            }
        }

        public void leave() {
            if (LogUtils.isDebug()) {
                Log.i("FunctionTracer", "离开 " + this.functionName + "，函数运行了" + (System.currentTimeMillis() - this.timeStart) + "毫秒");
            }
        }
    }

    public static boolean isDebug() {
        return sIsDebug;
    }

    private LogUtils() {
    }

    public static void SetDebugEnable(Context cxt) {
        boolean z = true;
        if (cxt != null) {
            try {
                ApplicationInfo appInfo = cxt.getPackageManager().getApplicationInfo(cxt.getPackageName(), 128);
                if (appInfo != null) {
                    if (appInfo.metaData.getInt("IS_USE_LOG") != 1) {
                        z = false;
                    }
                    sIsDebug = z;
                }
            } catch (Throwable th) {
                sIsDebug = false;
            }
        }
    }

    private static String generateTag(StackTraceElement caller) {
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        String tag = String.format("%s[%s, %d]", new Object[]{callerClazzName, caller.getMethodName(), Integer.valueOf(caller.getLineNumber())});
        if (TextUtils.isEmpty(customTagPrefix)) {
            return tag;
        }
        return customTagPrefix + ":" + tag;
    }

    public static void d(String content) {
        if (sIsDebug) {
            String tag = generateTag(getCallerStackTraceElement());
            Log.d(tag, content);
            if (isSaveLog) {
                saveLog(tag, content);
            }
        }
    }

    public static void d(String content, Throwable tr) {
        if (sIsDebug) {
            String tag = generateTag(getCallerStackTraceElement());
            Log.d(tag, content, tr);
            if (isSaveLog) {
                saveLog(tag, content);
            }
        }
    }

    public static void d(String tag, String content) {
        if (sIsDebug) {
            Log.d(tag, content);
            if (isSaveLog) {
                saveLog(tag, content);
            }
        }
    }

    public static void e(String content) {
        if (sIsDebug && content != null) {
            String tag = generateTag(getCallerStackTraceElement());
            Log.e(tag, content);
            if (isSaveLog) {
                saveLog(tag, content);
            }
        }
    }

    public static void e(Throwable content) {
        if (sIsDebug && content != null) {
            String tag = generateTag(getCallerStackTraceElement());
            Throwable cause = content.getCause();
            if (cause == null) {
                if (content.getMessage() != null) {
                    Log.e(tag, content.getMessage());
                } else {
                    Log.e(tag, "" + content);
                }
            } else if (content.getMessage() != null) {
                e(content.getMessage(), cause);
            } else {
                e("msearch", cause);
            }
            if (isSaveLog) {
                saveLog(tag, content.getMessage());
            }
        }
    }

    public static void e(String content, Throwable tr) {
        if (sIsDebug && tr != null) {
            String tag = generateTag(getCallerStackTraceElement());
            Log.e(tag, content, tr);
            if (isSaveLog) {
                saveLog(tag, content);
            }
        }
    }

    public static void e(String tag, String content) {
        if (sIsDebug) {
            Log.e(tag, content);
            if (isSaveLog) {
                saveLog(tag, content);
            }
        }
    }

    public static void i(String content) {
        if (sIsDebug) {
            String tag = generateTag(getCallerStackTraceElement());
            Log.i(tag, content);
            if (isSaveLog) {
                saveLog(tag, content);
            }
        }
    }

    public static void i(String content, Throwable tr) {
        if (sIsDebug) {
            String tag = generateTag(getCallerStackTraceElement());
            Log.e(tag, content, tr);
            if (isSaveLog) {
                saveLog(tag, content);
            }
        }
    }

    public static void i(String tag, String content) {
        if (sIsDebug) {
            Log.i(tag, content);
            if (isSaveLog) {
                saveLog(tag, content);
            }
        }
    }

    public static void v(String content) {
        if (sIsDebug) {
            String tag = generateTag(getCallerStackTraceElement());
            Log.v(tag, content);
            if (isSaveLog) {
                saveLog(tag, content);
            }
        }
    }

    public static void v(String content, Throwable tr) {
        if (sIsDebug) {
            String tag = generateTag(getCallerStackTraceElement());
            Log.e(tag, content, tr);
            if (isSaveLog) {
                saveLog(tag, content);
            }
        }
    }

    public static void v(String tag, String content) {
        if (sIsDebug) {
            Log.v(tag, content);
            if (isSaveLog) {
                saveLog(tag, content);
            }
        }
    }

    public static void w(String content) {
        if (sIsDebug) {
            String tag = generateTag(getCallerStackTraceElement());
            Log.w(tag, content);
            if (isSaveLog) {
                saveLog(tag, content);
            }
        }
    }

    public static void w(String content, Throwable tr) {
        if (sIsDebug) {
            String tag = generateTag(getCallerStackTraceElement());
            Log.e(tag, content, tr);
            if (isSaveLog) {
                saveLog(tag, content);
            }
        }
    }

    public static void w(String tag, String content) {
        if (sIsDebug) {
            Log.w(tag, content);
            if (isSaveLog) {
                saveLog(tag, content);
            }
        }
    }

    public static void w(Throwable tr) {
        if (sIsDebug) {
            String tag = generateTag(getCallerStackTraceElement());
            Log.w(tag, tr);
            if (isSaveLog) {
                saveLog(tag, tr.getMessage());
            }
        }
    }

    public static StackTraceElement getCurrentStackTraceElement() {
        return Thread.currentThread().getStackTrace()[3];
    }

    public static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    public static void ASSERT(boolean b) {
        if (isDebug() && !b) {
            throw new AssertionError("ASSERT FAILED");
        }
    }

    public static void SetDebugable(boolean debugable) {
        sIsDebug = debugable;
    }

    public static void setSaveLog(boolean isSave) {
        if (isSave && !isSaveLog) {
            logPath = Environment.getExternalStorageDirectory() + "/zainamai_barcode.log";
            d("setSaveLog... " + logPath);
            File logFile = new File(logPath);
            if (logFile.exists()) {
                logFile.delete();
            }
            try {
                logFile.createNewFile();
            } catch (Throwable e) {
                e(e);
            }
        }
        isSaveLog = isSave;
    }

    private static void saveLog(String tag, String content) {
        try {
            String log = new Date().toLocaleString() + "\t" + tag + "\t" + content + "\n";
            d("logPath=" + logPath);
            FileWriter fw = new FileWriter(logPath, true);
            fw.write(log);
            fw.close();
        } catch (Throwable e) {
            e(e);
        }
    }
}
