package com.dwcloud.consumer;

import com.dwcloud.client.annotation.HttpConsumer;
import com.dwcloud.provider.client.DemoHttpService;
import org.springframework.context.annotation.Configuration;

/**
 * 服务调用方
 */
@Configuration
public class HttpConfig {

    @HttpConsumer(domain = "localhost", port = "8080")
    private DemoHttpService demoHttpService;
}
