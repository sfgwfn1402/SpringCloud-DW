//package com.dwcloud.auth.config;
//
//import com.dwcloud.auth.common.utils.EncryptUtil;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
///**
//+ 密码加密处理器StockPasswordEncoder
//
//    使用密码加密器后， OAuth内置的Client认证以及用户密码认证都会加密处理。
//
//```java
// */
//@Component
//@Log4j2
//public class StockPasswordEncoder implements PasswordEncoder {
//
//    /**
//     * 编码处理
//     * @param rawPassword
//     * @return
//     */
//    @Override
//    public String encode(CharSequence rawPassword) {
//        return rawPassword.toString();
//    }
//
//    /**
//     * 密码校验判断
//     * @param rawPassword
//     * @param encodedPassword
//     * @return
//     */
//    @Override
//    public boolean matches(CharSequence rawPassword, String encodedPassword) {
//        if(rawPassword != null && rawPassword.length() > 0){
//            try {
//                // 这里通过MD5及B64加密
//                String password = EncryptUtil.encryptSigned(rawPassword.toString());
//                boolean isMatch= encodedPassword.equals(password);
//                if(!isMatch) {
//                    log.warn("password 不一致！");
//                }
//                return isMatch;
//            } catch (ComponentException e) {
//               log.error(e.getMessage(), e);
//            }
//        }
//        return false;
//    }
//
//}
//