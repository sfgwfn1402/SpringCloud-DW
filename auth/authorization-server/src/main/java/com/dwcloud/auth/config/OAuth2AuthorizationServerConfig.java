//package com.dwcloud.auth.config;
//
//import com.dwcloud.auth.service.MyUserDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
//import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//
///**
// * 配置认证服务器
// */
//@Configuration
//@EnableAuthorizationServer //开启认证服务器
//public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
//
//    //在 MyOAuth2Config 添加到容器了
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private MyUserDetailsService myUserDetailsService;
//
//    @Autowired
//    @Qualifier("jwtTokenStore")
//    TokenStore jwtTokenStore;
//
//    @Autowired
//    JwtAccessTokenConverter jwtAccessTokenConverter;
//
//    @Autowired
//    TokenEnhancerChain tokenEnhancerChain;
//
//    /**
//     * 配置被允许访问此认证服务器的客户端详细信息
//     * 1.内存管理
//     * 2.数据库管理方式
//     *
//     * @param clients
//     * @throws Exception
//     */
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//                //客户端名称
//                .withClient("test-pc")
//                //客户端密码
//                .secret(passwordEncoder.encode("123456"))
//                //资源id,商品资源
//                .resourceIds("oauth2-server")
//                //授权类型, 可同时支持多种授权类型
//                .authorizedGrantTypes("authorization_code", "password", "implicit", "client_credentials", "refresh_token")
//                //授权范围标识，哪部分资源可访问（all是标识，不是代表所有）
//                .scopes("all")
//                //false 跳转到授权页面手动点击授权，true 不用手动授权，直接响应授权码
//                .autoApprove(false)
//                //客户端回调地址
//                .redirectUris("http://www.baidu.com/")
//        ;
//    }
//
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        //密码模式需要配置认证管理器
//        endpoints.authenticationManager(authenticationManager);
//        //刷新令牌获取新令牌时需要
//        endpoints.userDetailsService(myUserDetailsService);
//        endpoints.tokenEnhancer(tokenEnhancerChain);
//        //令牌管理策略
////        endpoints.tokenStore(tokenStore);
//        //设置为jwt存储
//        endpoints.tokenStore(jwtTokenStore);
//        endpoints.accessTokenConverter(jwtAccessTokenConverter);
//        //授权码管理策略，针对授权码模式有效，会将授权码放到 auth_code 表，授权后就会删除它
//        //endpoints.authorizationCodeServices(jdbcAuthorizationCodeServices);
//        DefaultTokenServices tokenService = getTokenStore(endpoints);
//        endpoints.tokenServices(tokenService);
//    }
//
//
//    //配置TokenService参数
//    private DefaultTokenServices getTokenStore(AuthorizationServerEndpointsConfigurer endpoints) {
//        DefaultTokenServices tokenService = new DefaultTokenServices();
//        tokenService.setTokenStore(endpoints.getTokenStore());
//        tokenService.setSupportRefreshToken(true);
//        tokenService.setClientDetailsService(endpoints.getClientDetailsService());
//        tokenService.setTokenEnhancer(endpoints.getTokenEnhancer());
//        //token有效期 1小时
//        tokenService.setAccessTokenValiditySeconds(3600);
//        //token刷新有效期 15天
//        tokenService.setRefreshTokenValiditySeconds(3600 * 12 * 15);
//        tokenService.setReuseRefreshToken(false);
//        return tokenService;
//    }
//
//    /**
//     * 解决访问/oauth/check_token 403的问题
//     *
//     * @param security
//     * @throws Exception
//     */
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        // 允许表单认证
//        security
//                .tokenKeyAccess("permitAll()")
//                .checkTokenAccess("permitAll()")
//                .allowFormAuthenticationForClients();
//
//    }
//}