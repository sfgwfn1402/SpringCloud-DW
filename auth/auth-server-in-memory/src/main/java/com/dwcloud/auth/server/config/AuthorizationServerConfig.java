package com.dwcloud.auth.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 认证中心的相关的配置类
 * `@EnableAuthorizationServer`：这个注解标注这是一个认证中心
 * 继承AuthorizationServerConfigurerAdapter
 * <p>
 * 不是所有配置类都可以作为OAuth2.0认证中心的配置类，需要满足以下两点：
 * <p>
 * 继承AuthorizationServerConfigurerAdapter
 * 标注 @EnableAuthorizationServer 注解
 */
@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 令牌存储策略
     */
    @Autowired
    private TokenStore tokenStore;

    /**
     * 客户端存储策略，这里使用内存方式，后续可以存储在数据库
     *
     * 客户端配置的存储也支持多种方式，比如内存、数据库，
     * 对应的接口为：org.springframework.security.oauth2.provider.ClientDetailsService
     */
    @Autowired
    private ClientDetailsService clientDetailsService;

    /**
     * Security的认证管理器，密码模式需要用到
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 配置客户端
     *
     * 并不是所有的客户端都能接入授权服务。因此一些必要的配置是要认证中心分配给客户端的，
     * 比如：客户端id、客户端秘钥、资源id、授权模式、授权范围等。
     *
     * 例：像百度这样的客户端，这里的客户端id和密码是固定的，后续可以存储在数据库中
     *  参数说明：
     * .withClient("myjszl")：指定客户端唯一ID为myjszl
     * .secret()：指定秘钥，使用加密算法加密了，秘钥为123
     * .resourceIds("res1")：给客户端分配的资源权限，对应的是资源服务，比如订单这个微服务就可以看成一个资源，作为客户端肯定不是所有资源都能访问。
     * authorizedGrantTypes()：定义认证中心支持的授权类型，总共支持五种
     * 授权码模式：authorization_code
     * 密码模式：password
     * 客户端模式：client_credentials
     * 简化模式：implicit
     * 令牌刷新：refresh_token，这并不是OAuth2的模式，定义这个表示认证中心支持令牌刷新
     * scopes()：定义客户端的权限，这里只是一个标识，资源服务可以根据这个权限进行鉴权。
     * autoApprove：是否需要授权，设置为true则不需要用户点击确认授权直接返回授权码
     * redirectUris：跳转的uri
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //TODO 暂定内存模式，后续可以存储在数据库中，更加方便
        clients.inMemory()
                //客户端id
                .withClient("myjszl")
                //客户端秘钥
                .secret(new BCryptPasswordEncoder().encode("123"))
                //资源id，唯一，比如订单服务作为一个资源,可以设置多个
                .resourceIds("res1")
                //授权模式，总共四种，1. authorization_code（授权码模式）、password（密码模式）、client_credentials（客户端模式）、implicit（简化模式）
                //refresh_token并不是授权模式，
                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
                //允许的授权范围，客户端的权限，这里的all只是一种标识，可以自定义，为了后续的资源服务进行权限控制
                .scopes("all")
                //false 则跳转到授权页面
                .autoApprove(false)
                //授权码模式的回调地址
                .redirectUris("http://www.baidu.com");
    }

    /**
     * 令牌管理服务的配置
     * 除了令牌的存储策略需要配置，还需要配置令牌的服务AuthorizationServerTokenServices用来创建、获取、刷新令牌
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        //客户端端配置策略
        services.setClientDetailsService(clientDetailsService);
        //支持令牌的刷新
        services.setSupportRefreshToken(true);
        //令牌服务
        services.setTokenStore(tokenStore);
        //access_token的过期时间
        services.setAccessTokenValiditySeconds(60 * 60 * 2);
        //refresh_token的过期时间
        services.setRefreshTokenValiditySeconds(60 * 60 * 24 * 3);
        return services;
    }


    /**
     * 授权码模式的service，使用授权码模式authorization_code必须注入
     * 用来颁布和删除授权码，当然授权码也支持多种方式存储，比如内存，数据库，这里暂时使用内存方式存储
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        //todo 授权码暂时存在内存中，后续可以存储在数据库中
        return new InMemoryAuthorizationCodeServices();
    }

    /**
     * 配置令牌访问的端点
     * 框别想
     * /oauth/authorize：获取授权码的端点
     * /oauth/token：获取令牌端点。
     * /oauth/confifirm_access：用户确认授权提交端点。
     * /oauth/error：授权服务错误信息端点。
     * /oauth/check_token：用于资源服务访问的令牌解析端点。
     * /oauth/token_key：提供公有密匙的端点，如果你使用JWT令牌的话。
     *
     * 也可以自定义端点：
     * 例如：/oauth/my_custom_endpoint：自定义端点
     * AuthorizationServerEndpointsConfigurer有一个方法可以添加自定义端点：
     * .pathMapping("/oauth/my_custom_endpoint", "/my_custom_endpoint")：自定义端点的路径映射
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                //授权码模式所需要的authorizationCodeServices
                .authorizationCodeServices(authorizationCodeServices())
                //密码模式所需要的authenticationManager
                .authenticationManager(authenticationManager)
                //令牌管理服务，无论哪种模式都需要
                .tokenServices(tokenServices())
                //只允许POST提交访问令牌，uri：/oauth/token
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    /**
     * 配置令牌访问的安全约束
     * 主要对一些端点的权限进行配置，代码如下：
     * .tokenKeyAccess("permitAll()")：允许所有人访问/oauth/token_key端点，获取公有密匙。
     * .checkTokenAccess("isAuthenticated()")：允许所有已认证过的用户访问/oauth/check_token端点，解析令牌。
     * .allowFormAuthenticationForClients()：允许客户端使用表单认证，即client_id和client_secret通过表单提交。
     *
     * 今牌端点安全约束配置，比如：/oauth/token对哪些开放
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                //开启/oauth/token_key验证端口权限访问
                .tokenKeyAccess("permitAll()")
                //开启/oauth/check_token验证端口认证权限访问
                .checkTokenAccess("permitAll()")
                //表示支持 client_id 和 client_secret 做登录认证
                .allowFormAuthenticationForClients();
    }


}