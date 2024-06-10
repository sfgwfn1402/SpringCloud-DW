package com.dwcloud.auth.resource.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

/**
 * 资源服务的配置类
 * `@EnableResourceServer`：该注解标记这是一个资源服务
 * `@EnableGlobalMethodSecurity`：该注解开启注解校验权限
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /**
     * 令牌校验服务配置
     *
     * 配置令牌校验服务，客户端携带令牌访问资源，作为资源端必须检验令牌的真伪
     * TODO 使用JWT作为TOKEN则不必远程调用check_token校验
     * 由于认证中心使用的令牌存储策略是在内存中的，因此服务端必须远程调用认证中心的校验令牌端点/oauth/check_token**进行校验。
     *
     * 注意：远程校验令牌存在性能问题，但是后续使用JWT令牌，则本地即可进行校验，不必远程校验了。
     */
    @Bean
    public RemoteTokenServices tokenServices() {
        //远程调用授权服务的check_token进行令牌的校验
        RemoteTokenServices services = new RemoteTokenServices();
        // /oauth/check_token 这个url是认证中心校验的token的端点
        services.setCheckTokenEndpointUrl("http://localhost:2003/auth-server/oauth/check_token");
        //客户端的唯一id
        services.setClientId("myjszl");
        //客户端的秘钥
        services.setClientSecret("123");
        return services;
    }

    /**
     * 配置资源id和令牌校验服务
     * 说到客户端有一个唯一标识，因此需要配置上
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        //配置唯一资源id
        resources.resourceId("res1")
                //配置令牌校验服务
                .tokenServices(tokenServices());
    }

    /**
     * 配置security的安全机制
     *
     * 上文在认证中心的配置客户端详情那里 {@link com.dwcloud.auth.server.config.AuthorizationServerConfig}，
     * 有一行代码.scopes("all")则是指定了客户端的权限，资源服务可以根据这个scope进行url的拦截。
     * 拦截方式：
     * 1.antMatchers("/**").access("#oauth2.hasScope('all')")：所有请求都需要all权限
     * 2.anyRequest().authenticated()：除了/oauth/token和/oauth/authorize外的其他请求都需要认证
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //#oauth2.hasScope()校验客户端的权限，这个all是在客户端中的scope
        http.authorizeRequests()
                .antMatchers("/**").access("#oauth2.hasScope('all')")
                .anyRequest().authenticated();
    }
}