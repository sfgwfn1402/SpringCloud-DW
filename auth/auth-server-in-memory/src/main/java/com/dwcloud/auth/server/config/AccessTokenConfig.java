package com.dwcloud.auth.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * 令牌存储策略配置类
 * 令牌支持多种方式存储，比如内存方式、Redis、JWT，比较常用的两种则是Redis、JWT。
 */
@Configuration
public class AccessTokenConfig {

    /**
     * 令牌的存储策略
     */
    @Bean
    TokenStore tokenStore() {
        //todo 方便测试，使用内存存储策略，一旦服务重启令牌失效，后续可以使用数据库存储或者JWT
        return new InMemoryTokenStore();
    }
}