package com.dwcloud.starter;

import com.dwcloud.core.HttpRequestValidator;
import com.dwcloud.core.config.WebMvcConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 服务提供方生效
 */
@ConditionalOnClass(WebMvcConfigurer.class)
public class SpringMvcConfigurerAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public WebMvcConfig webMvcConfig() {
        return new WebMvcConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpRequestValidator httpRequestValidator() {
        return new HttpRequestValidator();
    }
}
