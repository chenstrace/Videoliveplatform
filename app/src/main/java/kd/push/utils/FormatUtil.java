package kd.push.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FormatUtil {
    public static short stream2Short(byte[] stream, int offset) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(stream[offset]);
        buffer.put(stream[offset + 1]);
        return buffer.getShort(0);
    }

    public static long stream2Long(byte[] stream, int offset) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(stream[offset]);
        buffer.put(stream[offset + 1]);
        buffer.put(stream[offset + 2]);
        buffer.put(stream[offset + 3]);
        buffer.put(stream[offset + 4]);
        buffer.put(stream[offset + 5]);
        buffer.put(stream[offset + 6]);
        buffer.put(stream[offset + 7]);
        return buffer.getLong(0);
    }

    public static int stream2Int(byte[] stream, int offset) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(stream[offset]);
        buffer.put(stream[offset + 1]);
        buffer.put(stream[offset + 2]);
        buffer.put(stream[offset + 3]);
        return buffer.getInt(0);
    }

    public static byte[] short2Stream(short data) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putShort(data);
        buffer.flip();
        return buffer.array();
    }

    public static byte[] int2Stream(int data) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(data);
        buffer.flip();
        return buffer.array();
    }

    public static byte[] long2Stream(long data) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putLong(data);
        buffer.flip();
        return buffer.array();
    }
}
