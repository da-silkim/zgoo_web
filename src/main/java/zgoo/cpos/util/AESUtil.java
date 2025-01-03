package zgoo.cpos.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    private static final String SECRET_KEY = "aaaassssddddffffqqqqwwwweeeerrrr";

    public static String encrypt(String plainText) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(SECRET_KEY.substring(0, 16).getBytes("UTF-8"));

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText) throws Exception {
        byte[] decodedEncryptedText = Base64.getDecoder().decode(encryptedText);

        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(SECRET_KEY.substring(0, 16).getBytes("UTF-8"));

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] decryptedBytes = cipher.doFinal(decodedEncryptedText);
        return new String(decryptedBytes);
    }
}
