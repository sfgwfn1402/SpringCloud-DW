//package com.dwcloud.auth.config;
//
//import com.nimbusds.jose.jwk.JWKSet;
//import com.nimbusds.jose.jwk.RSAKey;
//import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
//import com.nimbusds.jose.jwk.source.JWKSource;
//import com.nimbusds.jose.proc.SecurityContext;
//import lombok.SneakyThrows;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.security.oauth2.provider.token.TokenEnhancer;
//import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
//
//import java.security.KeyStore;
//import java.security.KeyStoreException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author Charles
// * @module springboot
// * @since 2023/6/20 15:25
// */
//
//@Configuration
//public class JwtTokenStoreConfig {
//
//    @Autowired
//    private CustomTokenEnhancer customTokenEnhancer;
//
//
//    @Value("${privateKey}")
//    private String privateKey;
//
//    @Value("${password}")
//    private String password;
//
//    @Value("${alias}")
//    private String alias;
//
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
//        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(privateKey), password.toCharArray());
//        jwtAccessTokenConverter.setKeyPair(keyStoreKeyFactory.getKeyPair(alias));
//        return jwtAccessTokenConverter;
//    }
//
//    @Bean("jwtTokenStore")
//    public TokenStore jwtTokenStore() {
//        JwtTokenStore jwtTokenStore = new JwtTokenStore(jwtAccessTokenConverter());
//        return jwtTokenStore;
//    }
//
//
//    @Bean
//    public TokenEnhancerChain tokenEnhancerChain() {
//        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
//        List<TokenEnhancer> enhancers = new ArrayList();
//        enhancers.add(jwtAccessTokenConverter());
//        enhancers.add(customTokenEnhancer);
//        //将自定义Enhancer加入EnhancerChain的delegates数组中
//        enhancerChain.setTokenEnhancers(enhancers);
//        return enhancerChain;
//    }
//
//
//    private static final KeyStore JKS_STORE;
//
//    static {
//        try {
//            JKS_STORE = KeyStore.getInstance("jks");
//        } catch (KeyStoreException e) {
//            throw new RuntimeException("can not obtain jks keystore instance");
//        }
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    @SneakyThrows
//    public JWKSource<SecurityContext> jwkSource() {
//        ClassPathResource classPathResource = new ClassPathResource(privateKey);
//        char[] pin = password.toCharArray();
//        JKS_STORE.load(classPathResource.getInputStream(), pin);
//        RSAKey rsaKey = RSAKey.load(JKS_STORE, alias, pin);
//        JWKSet jwkSet = new JWKSet(rsaKey);
//        return new ImmutableJWKSet(jwkSet);
//    }
//}