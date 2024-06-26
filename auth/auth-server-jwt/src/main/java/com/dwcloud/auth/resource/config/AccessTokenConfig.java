package com.dwcloud.auth.resource.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 令牌的配置
 */
@Configuration
public class AccessTokenConfig {
    /**
     * JWT的秘钥
     *
     * JWT签名的秘钥，这里使用的是对称加密，资源服务中也要使用相同的秘钥进行校验和解析JWT令牌。
     * 注意：实际工作中还是要使用非对称加密的方式，比较安全，这种方式后续文章介绍。
     *
     * TODO 实际项目中需要统一配置到配置文件中，资源服务也需要用到
     */
    private final static String SIGN_KEY="myjszl";

    /**
     * 令牌的存储策略
     *
     * 令牌的存储策略，这里使用的是JwtTokenStore，使用JWT的令牌生成方式
     * ，其实还有以下两个比较常用的方式：
     * RedisTokenStore：将令牌存储到Redis中，此种方式相对于内存方式来说性能更好
     * JdbcTokenStore：将令牌存储到数据库中，需要新建从对应的表，有兴趣的可以尝试
     */
    @Bean
    public TokenStore tokenStore() {
        //使用JwtTokenStore生成JWT令牌
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 令牌增强类，用于JWT令牌和OAuth身份进行转换
     *
     * JwtAccessTokenConverter
     * TokenEnhancer的子类，在JWT编码的令牌值和OAuth身份验证信息之间进行转换。
     * TODO：后期可以使用非对称加密
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 设置秘钥
        converter.setSigningKey(SIGN_KEY);
        return converter;
    }
}