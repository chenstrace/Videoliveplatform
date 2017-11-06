package kd.push.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DesUtil {
    private static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
    private static final int BUFFER_LENGTH = 1024;
    public static final String KEY = "zshTtp^1";
    private static final String TAG = "DesUtil";

    public static byte[] encryptDES(byte[] encryptData) {
        return encryptDES(encryptData, KEY.getBytes());
    }

    public static byte[] encryptDES(byte[] encryptData, byte[] key) {
        SecretKeySpec secretKeySpec;
        IvParameterSpec ivParameterSpec;
        if (encryptData == null) {
            return null;
        }
        try {
            if (encryptData.length <= 0) {
                return null;
            }
            IvParameterSpec zeroIv = new IvParameterSpec(key);
            try {
                SecretKeySpec sks = new SecretKeySpec(key, "DES");
                try {
                    Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
                    cipher.init(1, sks, zeroIv);
                    secretKeySpec = sks;
                    ivParameterSpec = zeroIv;
                    return cipher.doFinal(encryptData);
                } catch (Exception e) {
                    secretKeySpec = sks;
                    ivParameterSpec = zeroIv;
                    return null;
                }
            } catch (Exception e2) {
                ivParameterSpec = zeroIv;
                return null;
            }
        } catch (Exception e3) {
            return null;
        }
    }

    public static byte[] decryptDES(byte[] encryptData) {
        return decryptDES(encryptData, KEY.getBytes());
    }

    public static byte[] decryptDES(byte[] encryptData, byte[] key) {
        if (encryptData == null) {
            return null;
        }
        try {
            if (encryptData.length <= 0) {
                return null;
            }
            byte[] byteMi = encryptData;
            IvParameterSpec zeroIv = new IvParameterSpec(key);
            IvParameterSpec ivParameterSpec;
            try {
                SecretKeySpec sks = new SecretKeySpec(key, "DES");
                SecretKeySpec secretKeySpec;
                try {
                    Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
                    cipher.init(2, sks, zeroIv);
                    secretKeySpec = sks;
                    ivParameterSpec = zeroIv;
                    return cipher.doFinal(byteMi);
                } catch (Exception e) {
                    secretKeySpec = sks;
                    ivParameterSpec = zeroIv;
                    return null;
                }
            } catch (Exception e2) {
                ivParameterSpec = zeroIv;
                return null;
            }
        } catch (Exception e3) {
            return null;
        }
    }

    public static String decryptDES(InputStream response, int contentLength) {
        return decryptDES(response, contentLength, KEY.getBytes());
    }

    public static String decryptDES(InputStream response, int contentLength, byte[] key) {
        int size;
        IvParameterSpec ivParameterSpec;
        String ret = null;
        if (contentLength >= 1024) {
            size = contentLength;
        } else {
            size = 1024;
        }
        ByteArrayOutputStream result = new ByteArrayOutputStream(size);
        byte[] buffer = new byte[1024];
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(key);
            try {
                SecretKeySpec sks = new SecretKeySpec(key, "DES");
                try {
                    Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
                    CipherInputStream cis = new CipherInputStream(response, cipher);
                    cipher.init(2, sks, zeroIv);
                    while (true) {
                        int length = cis.read(buffer);
                        if (length == -1) {
                            break;
                        }
                        result.write(buffer, 0, length);
                    }
                    result.flush();
                    result.close();
                    cis.close();
                    ret = result.toString();
                    SecretKeySpec secretKeySpec = sks;
                    ivParameterSpec = zeroIv;
                } catch (Exception e) {
                    ivParameterSpec = zeroIv;
                }
            } catch (Exception e2) {
                ivParameterSpec = zeroIv;
            }
        } catch (Exception e3) {
        }
        return ret;
    }

    public static void encryptFile(String desKey, File sourceFile, File targetFile) throws IOException {
        if (sourceFile != null && sourceFile.exists() && sourceFile.isFile()) {
            FileInputStream in = new FileInputStream(sourceFile);
            DataInputStream dis = new DataInputStream(in);
            FileOutputStream out = new FileOutputStream(targetFile);
            DataOutputStream dos = new DataOutputStream(out);
            byte[] buffer = new byte[1024];
            try {
                byte[] desData;
                int n;
                if (sourceFile.length() >= 1024) {
                    dis.read(buffer, 0, buffer.length);
                    desData = encryptDES(buffer, desKey.getBytes());
                    dos.writeLong((long) desData.length);
                    dos.write(desData);
                    while (true) {
                        n = dis.read(buffer, 0, buffer.length);
                        if (n == -1) {
                            break;
                        }
                        dos.write(buffer, 0, n);
                    }
                } else {
                    n = dis.read(buffer, 0, buffer.length);
                    byte[] temp = new byte[n];
                    System.arraycopy(buffer, 0, temp, 0, n);
                    desData = encryptDES(temp, desKey.getBytes());
                    dos.writeLong((long) desData.length);
                    dos.write(desData);
                }
                dis.close();
                in.close();
                dos.close();
                out.close();
            } catch (IOException e) {
                throw new IOException();
            } catch (Throwable th) {
                dis.close();
                in.close();
                dos.close();
                out.close();
            }
        } else {
            throw new IOException();
        }
    }

    public static void decryptFile(String desKey, File sourceFile, File targetFile) throws IOException {
        if (sourceFile != null && sourceFile.exists() && sourceFile.isFile()) {
            FileInputStream in = new FileInputStream(sourceFile);
            DataInputStream dis = new DataInputStream(in);
            FileOutputStream out = new FileOutputStream(targetFile);
            DataOutputStream dos = new DataOutputStream(out);
            byte[] buffer = new byte[1024];
            try {
                long desLength = dis.readLong();
                int n;
                if (sourceFile.length() >= 8 + desLength) {
                    byte[] desBuffer = new byte[((int) desLength)];
                    dis.read(desBuffer, 0, desBuffer.length);
                    dos.write(decryptDES(desBuffer, desKey.getBytes()));
                    while (true) {
                        n = dis.read(buffer, 0, buffer.length);
                        if (n == -1) {
                            break;
                        }
                        dos.write(buffer, 0, n);
                    }
                } else {
                    n = dis.read(buffer, 0, buffer.length);
                    byte[] temp = new byte[n];
                    System.arraycopy(buffer, 0, temp, 0, n);
                    dos.write(decryptDES(temp, desKey.getBytes()));
                }
                dis.close();
                in.close();
                dos.close();
                out.close();
            } catch (IOException e) {
                throw new IOException();
            } catch (Throwable th) {
                dis.close();
                in.close();
                dos.close();
                out.close();
            }
        } else {
            throw new IOException();
        }
    }
}
