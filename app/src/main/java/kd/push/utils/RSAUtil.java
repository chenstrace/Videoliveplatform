package kd.push.utils;

public class RSAUtil {
    private static RSAOperator rsaOperator = null;
    private static final String strRSAPubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC79aaj3GEdtzUM1hsa9gtV6wbd\riZr0BApUzNpDyWgycMDcY9Gf48Kl4MRG2qi/oWrcxmHM4p9KXyHqMEzXjdJVYX3b\rv6hVX2r8CakHTHaS3AAXPEdaLfDundI8Ru/YiJ7MI0CfWCmsYCAJvno6110qWqnk\rJYRcVaA4oEhzPqz0cwIDAQAB";

    public static byte[] encrypt(byte[] plain) {
        if (rsaOperator == null) {
            try {
                rsaOperator = new RSAOperator(Base64.decode(strRSAPubKey, 0));
            } catch (Exception e) {
            }
        }
        try {
            return rsaOperator.encrypt(plain);
        } catch (Exception e2) {
            return null;
        }
    }
}
