//package com.dwcloud.auth.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.web.access.AccessDeniedHandler;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * 配置资源服务器
// */
//@Configuration
//@EnableResourceServer
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class OAuth2ResourceServer extends ResourceServerConfigurerAdapter {
//
//
//    @Autowired
//    @Qualifier("jwtTokenStore")
//    TokenStore jwtTokenStore;
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                //.authorizeRequests(request -> request.anyRequest().access("@checker.check(authentication,request)"))
//                //.and()
//                //放行
//                .antMatchers("/oauth/**", "/login/**", "/logout/**", "/sse/**").permitAll()
//                //其他路径需要role_admin
//                .anyRequest().hasAnyAuthority("role_admin")
//                .and()
//                //表单提交放行
//                .formLogin().permitAll()
//                .and()
//                //csrf关闭
//                .exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
////                    @Override
//                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//                        System.out.println(accessDeniedException);
//                    }
//                });
//
//    }
//
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) {
//        resources.resourceId("oauth2-server")
//                .tokenServices(tokenDefaultServices());
//    }
//
//
//    /**
//     * 配置资源服务器如何校验token
//     * 1. DefaultTokenServices
//     * 如果认证服务器和资源服务器在同一个服务，则直接采用默认服务验证
//     * 2.RemoteTokenServices
//     * 当认证服务器和资源服务器不在同一个服务，要使用此服务器去远程认证服务器验证
//     *
//     * @return
//     */
//    @Primary
//    @Bean
//    public DefaultTokenServices tokenDefaultServices() {
//        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//        defaultTokenServices.setTokenStore(jwtTokenStore);
//        defaultTokenServices.setSupportRefreshToken(true);
//        return defaultTokenServices;
//    }
//
////    @Primary
////    @Bean
////    public RemoteTokenServices tokenServices() {
////        //资源服务器去远程认证服务器验证 token 是否有效
////        final RemoteTokenServices tokenService = new RemoteTokenServices();
////        //请求认证服务器验证URL，注意：默认这个端点是拒绝访问的，要设置认证后可访问
////        tokenService.setCheckTokenEndpointUrl("http://localhost:8899/oauth/check_token");
////        //在认证服务器配置的客户端id
////        tokenService.setClientId("test-pc");
////        //在认证服务器配置的客户端密码
////        tokenService.setClientSecret("123456");
////        return tokenService;
////    }
//}