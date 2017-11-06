package kd.push;

import java.util.List;
import kd.push.message.MessageData;

public interface QHPushCallback {
    void connectionLost(Throwable th);

    void messageArrived(String str, List<MessageData> list);
}
