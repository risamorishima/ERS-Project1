package com.ers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

public class AES {
	public final Logger log = Logger.getLogger(AES.class);
	private SecretKeySpec secretKey;
    private byte[] key;
    private final String AES = "AES";

    public void setKey(String sKey) {
        MessageDigest sha = null;
        try {
            key = sKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, AES);
        } catch (NoSuchAlgorithmException e) {
        	log.error("secret key could not be set");
        }
    }

    public String encrypt(String input, String key) {
        try {
            setKey(key);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(input.getBytes("UTF-8")));
        } catch (Exception e) {
            log.error("unable to encrypt");
        }
        return null;
    }

    public String decrypt(String input, String key) {
        try {
            setKey(key);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            log.info("decryption occured");
            return new String(cipher.doFinal(Base64.getDecoder().decode(input)));
        } catch (Exception e) {
            log.error("unable to decrypt");
        }
        return null;
    }
    
//    public static void main(String args[]) throws NoSuchAlgorithmException
//    {
//        final String SECRETKEY = "somesecretkey";
//
//        String input = "anotherpassword";
//
//        AES aes = new AES();
//        String encrypted = aes.encrypt(input, SECRETKEY);
//        String decrypted = aes.decrypt(encrypted, SECRETKEY);
//
//        System.out.println(encrypted);
//        System.out.println(decrypted);
//    }
}
