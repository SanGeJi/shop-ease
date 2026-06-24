package com.shop.common;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.HexFormat;
import java.util.UUID;

public class AuthUtil {
    private static final int ITERATIONS = 100000;
    private static final int KEY_LEN = 512;

    public static String hashPassword(String password, String salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes("UTF-8"), ITERATIONS, KEY_LEN);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return HexFormat.of().formatHex(f.generateSecret(spec).getEncoded());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String genSalt() {
        return UUID.randomUUID().toString().replace("-", "") + "xx";
    }

    public static String genToken() {
        return UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
    }

    public static String genOrderNo() {
        return "ORD" + System.currentTimeMillis() + (int)(Math.random()*9000+1000);
    }
}