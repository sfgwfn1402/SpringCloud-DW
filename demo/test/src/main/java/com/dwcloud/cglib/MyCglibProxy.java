package com.dwcloud.cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;

/**
 * StudentProxy
 */
public class MyCglibProxy<T> implements MethodInterceptor {

    /**
     * getProxyInstance
     * @author senfel
     * @date 2024/4/3 16:27
     * @return java.lang.Object
     */
    public Object getProxyInstance(Class<T> tClass)  {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(tClass);
        enhancer.setCallback(this); // 设置回调方法为当前类
        return enhancer.create();
    }

    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("开始执行代理逻辑...");
        // 前置处理或其他逻辑
        beforeProxyRun();
        // 调用原始方法（即目标方法）
        Object result = proxy.invokeSuper(obj, args);
        // 后置处理或其他逻辑
        afterProxyRun();
        System.out.println("结束执行增代理逻辑...");
        return result;
    }

    /**
     * beforeProxyRun
     * @author senfel
     * @date 2024/4/3 16:33
     * @return void
     */
    private void beforeProxyRun() {
        System.out.println("代理前：执行一些预处理操作...");
    }

    /**
     * afterProxyRun
     * @author senfel
     * @date 2024/4/3 16:33
     * @return void
     */
    private void afterProxyRun() {
        System.out.println("代理后：执行一些后续处理操作...");
    }
}
