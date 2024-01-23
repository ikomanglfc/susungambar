package com.triyakom.susun.shorter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.InvalidKeyException;

public class ParseShortUrlData {
    private static long REDIRECT_TIMEOUT = 30 * 60 * 1000;//30 minute diff

    private Encrypt encrypt;

    public ParseShortUrlData(Encrypt encrypt) throws Exception {
        this.encrypt = encrypt;
    }

    public PassData parsing(String webParam, boolean checkTime) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException, TimeExpiredException {
        PassData data = encrypt.decrypt(webParam, PassData.class);
        if(checkTime) {
            //check time diff
            long time = System.currentTimeMillis();
            if ((time - data.getDate()) > REDIRECT_TIMEOUT) {
                throw new TimeExpiredException("Redirect time is expired");
            }
        }

        return data;
    }

    public String encode(PassData passData){
        return encrypt.encrypt(passData);
    }
}
