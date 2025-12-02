package org.cityu.supermarket.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 Encryption Utility
 * Used for password encryption
 */
public class MD5Utils {

    /**
     * Encrypt string using MD5
     * @param str Source string
     * @return MD5 encrypted string
     */
    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] byteDigest = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            // 32-bit encryption
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Test main method
    public static void main(String[] args) {
        // Print MD5 for "123456" to help user update DB
        System.out.println(md5("123456"));
    }
}
