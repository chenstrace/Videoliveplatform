package kd.push.utils;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAOperator {
    private static final String TAG = "RSAOperator";
    private static final byte[] X509_ENVELOP = new byte[]{(byte) 48, (byte) -127, (byte) -97, (byte) 48, 13, (byte) 6, (byte) 9, (byte) 42, (byte) -122, (byte) 72, (byte) -122, (byte) -9, 13, (byte) 1, (byte) 1, (byte) 1, (byte) 5, (byte) 0, (byte) 3, (byte) -127, (byte) -115, (byte) 0};
    private RSAPublicKey mPublicKey;

    protected RSAOperator(RSAPublicKey pubKey) {
        this.mPublicKey = pubKey;
    }

    public RSAOperator(byte[] bufferOfPublicKey) throws Exception {
        byte[] x509;
        if (bufferOfPublicKey.length == 140) {
            x509 = new byte[162];
            int i = 0;
            while (i < X509_ENVELOP.length) {
                x509[i] = X509_ENVELOP[i];
                i++;
            }
            int j = 0;
            while (i < 162) {
                x509[i] = bufferOfPublicKey[j];
                i++;
                j++;
            }
        } else if (bufferOfPublicKey.length == 162) {
            x509 = bufferOfPublicKey;
        } else {
            throw new Exception("Invalid Buffer Length: " + bufferOfPublicKey.length + " of Public Key");
        }
        this.mPublicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(x509));
    }

    public RSAPublicKey getPublicKey() {
        return this.mPublicKey;
    }

    public byte[] getEncodedPublicKey() {
        return this.mPublicKey.getEncoded();
    }

    public byte[] encrypt(byte[] plain) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Key key = this.mPublicKey;
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(1, key);
        return cipher.doFinal(plain);
    }

    public boolean verify(byte[] data, byte[] bufferOfSignature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(this.mPublicKey);
        signature.update(data);
        return signature.verify(bufferOfSignature);
    }
}
