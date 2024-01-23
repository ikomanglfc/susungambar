package com.triyakom.susun.shorter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.spec.KeySpec;


public class Json3DesEncrypt implements Encrypt {
    private ObjectMapper mapper = new ObjectMapper();
    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private KeySpec myKeySpec;
    private SecretKeyFactory mySecretKeyFactory;
    private Cipher cipher;
    byte[] keyAsBytes;
    private String myEncryptionKey;
    private String myEncryptionScheme;
    SecretKey key;

    public Json3DesEncrypt(String key) throws Exception{
        this.myEncryptionKey = key;
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        keyAsBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        myKeySpec = new DESedeKeySpec(keyAsBytes);
        mySecretKeyFactory = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance(myEncryptionScheme);
        this.key = mySecretKeyFactory.generateSecret(myKeySpec);
    }

    @Override
    public String encrypt(Object t) {
        try {
            String s = mapper.writeValueAsString(t);
            return encrypt(s);
        }catch (Exception ex){
            return null;
        }
    }

    private String encrypt(String unencryptedString)
            throws InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
        byte[] encryptedText = cipher.doFinal(plainText);
        String encryptedString = new String(Base64.encodeBase64URLSafe(encryptedText));

        return encryptedString;
    }

    @Override
    public <T> T decrypt(String s, Class<T> c) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        byte[] decrypt = decrypt(s);
        String n = new String(decrypt);
        T t = mapper.readValue(decrypt, c);
        return t;
    }

    private byte[] decrypt(String encryptedString)
            throws InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] encryptedText = Base64.decodeBase64(encryptedString);
        byte[] plainText = cipher.doFinal(encryptedText);
        return plainText;
    }

    private static String bytes2String(byte[] bytes) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            stringBuffer.append((char) bytes[i]);
        }
        return stringBuffer.toString();
    }
}
