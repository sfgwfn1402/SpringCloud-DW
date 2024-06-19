package com.dwcloud.consumer;

import com.dwcloud.provider.client.DemoHttpService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 服务调用方
 */
@Component
public class DemoConsumer {
    @Resource
    private DemoHttpService demoHttpService;

    public String checkSuccess() {
        return demoHttpService.checkSuccess("param1", "param2");
    }
}
