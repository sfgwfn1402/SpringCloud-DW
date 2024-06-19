package com.dwcloud.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注方法参数
 * <p>
 * 第三个“MultiRequestBody”，用于实现多变量传参。
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiRequestBody {
    /**
     * 解析参数时用到JSON中的key
     *
     * @return JSON格式参数
     */
    String value();
}
