package com.dwcloud.core;

import com.dwcloud.core.model.HttpDomain;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.proxy.Enhancer;

/**
 * 使用FactoryBean生成bean
 */
public class HttpConsumerProxyFactoryBean implements FactoryBean<Object> {

    private final Class<?> proxyKlass;

    private final HttpDomain httpDomain;

    private final OkHttpClient okHttpClient;

    public HttpConsumerProxyFactoryBean(Class<?> proxyKlass, HttpDomain httpDomain, OkHttpClient okHttpClient) {
        this.proxyKlass = proxyKlass;
        this.httpDomain = httpDomain;
        this.okHttpClient = okHttpClient;
    }

    @Override
    public Object getObject() {
        Enhancer enhancer = new Enhancer();
        if (proxyKlass.isInterface()) {
            enhancer.setInterfaces(new Class[]{proxyKlass});
        } else {
            enhancer.setSuperclass(proxyKlass);
        }
        HttpConsumerInterceptor httpConsumerInterceptor = new HttpConsumerInterceptor(proxyKlass, httpDomain, okHttpClient);
        enhancer.setCallback(httpConsumerInterceptor);
        return enhancer.create();
    }

    @Override
    public Class<?> getObjectType() {
        return proxyKlass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
