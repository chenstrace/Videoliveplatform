package kd.push.utils;

import java.security.MessageDigest;

public class MD5Util {
    public static String encode(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(string.getBytes());
            byte[] b = md.digest();
            StringBuffer buf = new StringBuffer("");
            for (int i : b) {
                int i2=0;
                if (i2 < 0) {
                    i2 += 256;
                }
                if (i2 < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i2));
            }
            return buf.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
