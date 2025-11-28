package com.sky.utils;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

/**
 * 密码加密工具类
 * 使用Argon2算法进行密码加密和验证
 */
public class PasswordEncoder {

    // Argon2加密参数
    private static final int ITERATIONS = 3;      // 迭代次数
    private static final int MEMORY = 65536;      // 内存成本（64MB）
    private static final int PARALLELISM = 4;     // 并行度

    /**
     * 对明文密码进行加密
     * @param rawPassword 明文密码
     * @return 加密后的密码
     */
    public static String encode(String rawPassword) {
        Argon2 argon2 = Argon2Factory.create();
        char[] passwordChars = rawPassword.toCharArray();
        try {
            return argon2.hash(ITERATIONS, MEMORY, PARALLELISM, passwordChars);
        } finally {
            // 清理密码字符数组
            argon2.wipeArray(passwordChars);
        }
    }

    /**
     * 验证密码是否匹配
     * @param encodedPassword 加密后的密码
     * @param rawPassword 明文密码
     * @return 是否匹配
     */
    public static boolean matches(String encodedPassword, String rawPassword) {
        Argon2 argon2 = Argon2Factory.create();
        char[] passwordChars = rawPassword.toCharArray();
        try {
            return argon2.verify(encodedPassword, passwordChars);
        } finally {
            // 清理密码字符数组
            argon2.wipeArray(passwordChars);
        }
    }
}
