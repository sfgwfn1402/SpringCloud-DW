package com.dwcloud.provider.client;


import com.dwcloud.client.annotation.HttpRequest;
import com.dwcloud.client.annotation.MultiRequestBody;

/**
 * 服务提供方
 */
@HttpRequest("demo")
public interface DemoHttpService {

    @HttpRequest("checkSuccess")
    String checkSuccess(@MultiRequestBody("param1") String param1, @MultiRequestBody("param2") String param2);
}
