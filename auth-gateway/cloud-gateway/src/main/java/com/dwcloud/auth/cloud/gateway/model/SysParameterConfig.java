package com.dwcloud.auth.cloud.gateway.model;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 系统参数配置
 */
@ConfigurationProperties(prefix = "oauth2.cloud.sys.parameter")
@Data
public class SysParameterConfig {
    /**
     * 白名单
     */
    private List<String> ignoreUrls;
}
