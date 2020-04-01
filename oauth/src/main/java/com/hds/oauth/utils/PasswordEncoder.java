package com.hds.oauth.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author admin
 * @date 2020/4/1
 */
public class PasswordEncoder {
    /**
     * secret或者密码加密
     * @param password
     * @return
     */
    public String passwordEncoder(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    /**
     * 验证密码和加密后的密码是否匹配
     * @param password
     * @param encodedPassword
     * @return
     */
    public boolean passwordMatchs(String password, String encodedPassword) {
        return new BCryptPasswordEncoder().matches(password, encodedPassword);
    }
}
