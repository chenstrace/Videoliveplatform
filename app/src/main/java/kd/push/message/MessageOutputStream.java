package kd.push.message;

import java.io.IOException;
import java.io.OutputStream;

public class MessageOutputStream {
    private OutputStream out;

    public MessageOutputStream(OutputStream out) {
        this.out = out;
    }

    public void Write(Message message) throws IOException {
        this.out.write(message.toByte());
    }
}
