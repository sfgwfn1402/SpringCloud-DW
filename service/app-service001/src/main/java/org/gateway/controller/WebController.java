package org.gateway.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创建一个RestApi，对/order/info路径进行了权限校验
 */
@RestController
@RequestMapping("/order")
public class WebController {

    @PreAuthorize("hasAuthority('orderInfo')")
    @RequestMapping("/info")
    public String info() {
        return "order-service";
    }
}
