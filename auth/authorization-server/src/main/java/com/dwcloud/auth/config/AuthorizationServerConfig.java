//package com.dwcloud.auth.config;
//
//import com.dwcloud.auth.common.service.AuthClientDetailService;
//import lombok.SneakyThrows;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
//
///**
// * 认证服务的配置， TokenStore实现配置， Token增强配置以及自定义Client的查询实现。
// */
//@Configuration
//@EnableAuthorizationServer
//public class AuthorizationServerConfig {
//    /**
//     * Redis 缓存配置
//     *
//     * @return
//     */
//    @Bean
//    public RedisTemplate<String, Object> stockRedisTemplate() {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
//        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        return redisTemplate;
//    }
//
//
//    /**
//     * 自定义Client查询，可以修改表名， 字段等
//     *
//     * @param clients
//     */
//    @Override
//    @SneakyThrows
//    public void configure(ClientDetailsServiceConfigurer clients) {
//        AuthClientDetailService clientDetailsService = new AuthClientDetailService(dataSource);
//        clientDetailsService.setSelectClientDetailsSql(DEFAULT_SELECT_STATEMENT);
//        clientDetailsService.setFindClientDetailsSql(DEFAULT_FIND_STATEMENT);
//        clients.withClientDetails(clientDetailsService);
//    }
//
//
//    /**
//     * t_oauth_client_details 表的字段，不包括client_id、client_secret
//     */
//    String CLIENT_FIELDS = "client_id, client_secret, resource_ids, scope, "
//            + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
//            + "refresh_token_validity, additional_information, autoapprove";
//
//    /**
//     * JdbcClientDetailsService 查询语句
//     */
//    String BASE_FIND_STATEMENT = "select " + CLIENT_FIELDS
//            + " from t_oauth_client_details";
//
//    /**
//     * 默认的查询语句
//     */
//    String DEFAULT_FIND_STATEMENT = BASE_FIND_STATEMENT + " order by client_id";
//
//    /**
//     * 按条件client_id 查询
//     */
//    String DEFAULT_SELECT_STATEMENT = BASE_FIND_STATEMENT + " where client_id = ?";
//
//
//    /**
//     * 防止申请token时出现401错误
//     *
//     * @param oauthServer
//     */
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
//        oauthServer
//                .tokenKeyAccess("permitAll()")
//                .checkTokenAccess("permitAll()")
//                .allowFormAuthenticationForClients();
//    }
//
//    /**
//     * 认证服务配置
//     *
//     * @param endpoints
//     */
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
//        endpoints
//                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
//                .tokenStore(tokenStore())
//                .tokenEnhancer(tokenEnhancer())
//                .userDetailsService(authStockUserDetailService)
//                .authenticationManager(authenticationManager)
//                .reuseRefreshTokens(false);
//    }
//
//
//}
