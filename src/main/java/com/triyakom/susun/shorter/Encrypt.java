package com.triyakom.susun.shorter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.InvalidKeyException;

public interface Encrypt {
    String encrypt(Object t);
    <T> T decrypt(String s, Class<T> c) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException;
}
