package com.eleven.MongoConnApp.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays; /**
 * Class is used to decrypt the encrypted  DB String token that fetches from AWS Secrets Manager. The DB String token is decrypted using the key from AWS Systems Manager
 */
public class Decryptor {

    private static final Logger logger = LogManager.getLogger(Decryptor.class);
    /**
     * This Method is used to decrypt the DBURL that fetched from AWS SecretsManager
     * @param unlockKey
     * @param dbStringURL
     * @return String
     */
    public static String decryptDBURL(String unlockKey, String dbStringURL) {
        logger.info("Inside Decryptor :: decryptDBURL");
        String decryptedText = null;
        byte[] finalDecryptedBytes = null;
        try {

            String[] textParts = dbStringURL.split(":");

            String ivString = textParts[0];
            textParts = Arrays.copyOfRange(textParts, 1, textParts.length);
            if (ivString.length() % 2 != 0) {
                throw new IllegalArgumentException("Invalid IV string length: must be even for hexadecimal representation");
            }
            byte[] ivBytes = new byte[ivString.length() / 2];
            for (int i = 0; i < ivString.length(); i += 2) {
                String hexByte = ivString.substring(i, i + 2);
                ivBytes[i / 2] = (byte) Integer.parseInt(hexByte, 16);
            }
            ByteBuffer ivBuffer = ByteBuffer.wrap(ivBytes);

            String joinedText = String.join(":", textParts);
            if (joinedText.length() % 2 != 0) {
                throw new IllegalArgumentException("Invalid encrypted text length: must be even for hexadecimal representation");
            }
            byte[] encryptedBytes = new byte[joinedText.length() / 2];
            for (int i = 0; i < joinedText.length(); i += 2) {
                String hexByte = joinedText.substring(i, i + 2);
                encryptedBytes[i / 2] = (byte) Integer.parseInt(hexByte, 16);
            }
            ByteBuffer encryptedBuffer = ByteBuffer.wrap(encryptedBytes);

            byte[] passwordBytes = unlockKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec keySpec = new SecretKeySpec(passwordBytes, "AES");
            Cipher decipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            decipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(ivBuffer.array()));

            ByteBuffer decryptedBuffer = ByteBuffer.allocate(encryptedBuffer.remaining());
            int decryptedBytes = decipher.update(encryptedBuffer, decryptedBuffer);
            if (decryptedBytes == 0) {
                try {
                    finalDecryptedBytes = decipher.doFinal(decryptedBuffer.array());
                } catch (IllegalBlockSizeException | BadPaddingException e) {
                    throw new IllegalStateException("Decryption error", e);
                }
            } else {
                try {
                    finalDecryptedBytes = decipher.doFinal();
                } catch (IllegalBlockSizeException | BadPaddingException e) {
                    throw new IllegalStateException("Decryption error", e);
                }
            }
            decryptedBuffer.flip();

            decryptedText = new String(decryptedBuffer.array(), StandardCharsets.UTF_8);

            ByteBuffer finalDecryptedBuffer = ByteBuffer.wrap(finalDecryptedBytes);
            String finalDecryptedText = new String(finalDecryptedBuffer.array(), StandardCharsets.UTF_8);

            decryptedText = decryptedText+finalDecryptedText;

        } catch (Exception e) {
            logger.error("Error Occured in Decryptor :: decryptDBURL{}", e.getMessage());
        }
        return decryptedText;
    }
}
