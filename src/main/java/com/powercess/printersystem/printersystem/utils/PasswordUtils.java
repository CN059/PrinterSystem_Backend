package com.powercess.printersystem.printersystem.utils;

import org.mindrot.jbcrypt.BCrypt;
public class PasswordUtils {
    /**
     * 加密明文密码
     */
    public static String hashPassword(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }
    /**
     * 验证密码是否匹配
     */
    public static boolean checkPassword(String plainText, String hashed) {
        return BCrypt.checkpw(plainText, hashed);
    }
}