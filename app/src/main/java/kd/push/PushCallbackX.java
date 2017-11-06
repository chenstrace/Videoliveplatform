package kd.push;

import java.util.List;
import kd.push.message.MessageData;

public interface PushCallbackX {
    void connectionEstablished(String str);

    void connectionFailed(String str);

    void connectionLost(String str, Throwable th);

    void messageArrived(String str, List<MessageData> list);
}
