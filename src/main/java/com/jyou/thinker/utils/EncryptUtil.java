package com.jyou.thinker.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * TODO: 对数据进行加密操作
 * @author wgbing
 * @date 2018/7/13 9:53
 */
public class EncryptUtil {

    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    /**
     * TODO: 密码非可逆加密
     * @author wgbing
     * @date 2018/7/13 9:53
     */
    public static String encode(CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * TODO: 加密密码和非加密密码匹配
     * @author wgbing
     * @date 2018/7/13 9:54
     */
    public static Boolean match(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * TODO: 获取加密实体
     * @author wgbing
     * @date 2018/7/13 9:54
     */
    public static PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public static void main(String[] args) {
        String str = encode("123456");
        System.out.println(str);
    }
}
