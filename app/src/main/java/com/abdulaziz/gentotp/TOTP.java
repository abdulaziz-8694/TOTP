package com.abdulaziz.gentotp;

/**
 * Created by abdulaziz on 08/08/16.
 */

import java.lang.reflect.UndeclaredThrowableException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TOTP {

    private static final String TOTP_KEY = "com.abdulaziz.gentotp.TOTPKEY";

    private static final int[] DIGITS_POWER
            // 0 1 2 3 4 5 6 7 8
            = { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000 };

    private TOTP() {
    }

    /**
     * This method uses the JCE to provide the crypto algorithm. HMAC computes a
     * Hashed Message Authentication Code with the crypto hash algorithm as a
     * parameter.
     *
     * @param crypto
     *            : the crypto algorithm (HmacSHA1, HmacSHA256, HmacSHA512)
     * @param keyBytes
     *            : the bytes to use for the HMAC key
     * @param text
     *            : the message or text to be authenticated
     */

    private static byte[] hmacSha(String crypto, byte[] keyBytes, byte[] text)throws UndeclaredThrowableException {
        try {
            Mac hmac;
            hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }

    /**
     * This method generates a TOTP value for the given set of parameters.
     *
     * @param key
     *            : the shared secret
     * @param time
     *            : a value that reflects a time
     * @param digits
     *            : number of digits to return
     * @param crypto
     *            : the crypto function to use
     *
     * @return: the TOTP from time
     */

    private static int generateTOTP(byte[] key, long time, int digits, String crypto) {

        byte[] msg = ByteBuffer.allocate(8).putLong(time).array();
        byte[] hash = hmacSha(crypto, key, msg);

        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);
        return binary % DIGITS_POWER[digits];
    }

    public static int genTOTP(){
        String crypto = "HmacSHA1";
        byte[] key = TOTP_KEY.getBytes();
        long timeInMinutes = (new Date().getTime())/(long)(60*1000);
        int digits = 5;
        return generateTOTP(key, timeInMinutes, digits,crypto);

    }

}