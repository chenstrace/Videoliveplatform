package kd.push.utils;

import android.content.Context;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Vector;

public class QSession {
    public static final String CONF_DOMAIN = "md.openapi.360.cn";
    public static final String PRODUCT = "iot";
    private static String TAG = "QSession";
    public static boolean bDebug = true;
    public static Context ctx = null;
    public static Vector<QAppBean> downAppVector = new Vector();
    protected static final String mTag = "iot";
    protected static final String sTag = "iot";
    public static final String sdkVersion = "1.0.0";

    public static void init(Context context) {
        try {
            if (ctx == null) {
                ctx = context;
                if (bDebug) {
                    Toast.makeText(ctx, "您使用的是Debug版的360推送SDK v1.0.0", 1).show();
                }
                QUtil.checkPermission(ctx);
                new Thread() {
                    public void run() {
                        boolean bFirst = true;
                        QLOG.debug(QSession.TAG, "Startup the QSession.");
                        while (QSession.ctx != null) {
                            if (bFirst) {
                                bFirst = false;
                                try {
                                  //  AnonymousClass1.sleep(a.m);
                                } catch (Exception ex) {
                                    QLOG.error(QSession.TAG, ex);
                                } catch (Error err) {
                                    QLOG.error(QSession.TAG, err);
                                }
                            } else {
                                //AnonymousClass1.sleep(a.u);
                            }
                            for (int i = 0; i < QSession.downAppVector.size(); i++) {
                                QAppBean ab = (QAppBean) QSession.downAppVector.get(i);
                                if (ab.versionCode == QUtil.getVersionCode(QSession.ctx, ab.pname)) {
                                    new HashMap().put("msgId", ab.msgId);
                                    QSession.downAppVector.remove(i);
                                    break;
                                }
                            }
                        }
                        QLOG.warn(QSession.TAG, "Warning, the QSession is stoppped!");
                    }
                }.start();
            }
        } catch (Exception ex) {
            QLOG.error(TAG, ex);
        } catch (Error err) {
            QLOG.error(TAG, err);
        }
    }
}
