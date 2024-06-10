package com.dwcloud.auth.cloud.common.utils;

import com.dwcloud.auth.cloud.common.model.LoginVal;
import com.dwcloud.auth.cloud.common.model.RequestConstant;

/**
 * @author  公众号：码猿技术专栏
 * OAuth2.0工具类，从请求的线程中获取个人信息
 */
public class OauthUtils {

    /**
     * 获取当前请求登录用户的详细信息
     */
    public static LoginVal getCurrentUser(){
        return (LoginVal) RequestContextUtils.getRequest().getAttribute(RequestConstant.LOGIN_VAL_ATTRIBUTE);
    }
}
