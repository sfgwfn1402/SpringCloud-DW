package com.dwcloud.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注需要代理为http请求发送
 * <p>
 * 第一个“HttpConsumer”，用于消费端注册服务。
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpConsumer {
    /**
     * 域名
     *
     * @return 域名
     */
    String domain();

    /**
     * 端口，默认80
     *
     * @return 端口号
     */
    String port() default "80";
}
