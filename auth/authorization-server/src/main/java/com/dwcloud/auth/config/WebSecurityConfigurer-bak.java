//package com.dwcloud.auth.config;
//
//import lombok.SneakyThrows;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
//
///**
// * 实现了一个获取用户信息接口，以及自定义login登陆处理， 用于OAUTH的验证。
// */
//@Primary
//@Order(90)
//@Configuration
//public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private UserDetailsService authStockUserDetailService;
//
//    @Autowired
//    private StockPasswordEncoder stockPasswordEncoder;
//
//    /**
//     * Web服务认证配置
//     *
//     * @param http
//     */
//    @Override
//    @SneakyThrows
//    protected void configure(HttpSecurity http) {
//        http
//                .formLogin()
//                .loginPage("/token/login")
//                .loginProcessingUrl("/token/form")
//                .defaultSuccessUrl("/token/success")
//                .and()
//                .authorizeRequests()
//                .antMatchers(
//                        "/token/**",
//                        "/actuator/**",
//                        "/druid/**").permitAll()
//                .anyRequest().authenticated()
//                .and().csrf().disable();
//    }
//
//    /**
//     * 不拦截静态资源
//     *
//     * @param web
//     */
//    @Override
//    public void configure(WebSecurity web) {
//        web.ignoring().antMatchers(" / css/**");
//
//    }
//
//
//    @Bean
//    @Override
//    @SneakyThrows
//    public AuthenticationManager authenticationManagerBean() {
//        return super.authenticationManagerBean();
//    }
//
//    @Autowired
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(authStockUserDetailService).passwordEncoder(stockPasswordEncoder);
//    }
//}