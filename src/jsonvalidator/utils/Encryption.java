package jsonvalidator.utils;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class Encryption {
    public static final String SHR_KEY = "!A%D*F-JaNdRgUkX";
    private static final String intializationVector = "PdSgVkXp2s5v8y/B";

    public static String encrypt(String message) {
        String encryptedString = null;
        try {
            IvParameterSpec iv = new IvParameterSpec(intializationVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(SHR_KEY.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(message.getBytes());
            encryptedString = Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.getMessage();
        }
        return encryptedString;
    }

    public static String decrypt(String encrypted) {
        String originalMessage = null;
        try {
            IvParameterSpec iv = new IvParameterSpec(intializationVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(SHR_KEY.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            originalMessage = new  String(original);
        } catch (Exception ex) {
            ex.getMessage();
        }

        return originalMessage;
    }
}