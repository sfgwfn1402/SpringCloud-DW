package com.dwcloud.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记可以通过http访问的服务
 * <p>
 * 第二个“HttpRequest”，用于服务提供方定义服务。
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpRequest {
    /**
     * http请求url
     *
     * @return url
     */
    String value();
}
