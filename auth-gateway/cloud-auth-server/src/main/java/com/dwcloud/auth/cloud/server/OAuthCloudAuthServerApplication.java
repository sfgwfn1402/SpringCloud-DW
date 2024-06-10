package com.dwcloud.auth.cloud.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.dwcloud.auth.cloud.*"})
public class OAuthCloudAuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(OAuthCloudAuthServerApplication.class);
    }
}
