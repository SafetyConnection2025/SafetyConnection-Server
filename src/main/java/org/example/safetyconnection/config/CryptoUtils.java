package org.example.safetyconnection.config;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;

public class CryptoUtils {

    public static final String aeskey = "8d7bd2cc7dcfe6fde4393705896208f6";

    private static final String AES = "AES";
    private static final String AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
    private static final String UTF_8 = "UTF-8";

    public static String encryptData(String plainText, String key) throws Exception {
        // 1. 키 준비
        byte[] keyBytes = aeskey.getBytes();
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        // 2. IV 생성
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // 3. 암호화
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes(UTF_8));

        // 4. IV + 암호문 결합 (복호화 시 IV가 필요하므로)
        byte[] encryptedWithIv = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, encryptedWithIv, 0, iv.length);
        System.arraycopy(encrypted, 0, encryptedWithIv, iv.length, encrypted.length);

        // 5. Base64 인코딩 후 반환
        return Base64.getEncoder().encodeToString(encryptedWithIv);
    }

    public static String decryptData(String encryptedData, String key) throws Exception {
        IvParameterSpec iv = new IvParameterSpec("".getBytes(UTF_8)); // Fixed 16 bytes IV for AES
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(UTF_8), AES);

        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);

        byte[] decodedEncryptedData = Base64.getUrlDecoder().decode(encryptedData);
        byte[] original = cipher.doFinal(decodedEncryptedData);
        return new String(original, UTF_8);
    }
}
