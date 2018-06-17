package donkey.koin.krypto;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class KeyUtil {
    public static PublicKey getRsaPublicKeyKeyFromBytes(byte[] bytes) {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes));
        } catch (Exception e) {
            return null;
        }
    }
}
