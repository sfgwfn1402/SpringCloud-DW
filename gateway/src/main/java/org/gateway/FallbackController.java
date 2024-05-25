package org.gateway;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @Title: 响应超时熔断处理器
 * @Description: 当服务无法访问时会跳转到这里
 */
@RestController
public class FallbackController {


    /**
     * fallbackCmd熔断处理
     *
     * @return
     */
    @RequestMapping("/fallbackCmd")
    public Mono<String> fallbackCmd() {
        return Mono.just("访问超时，请稍后再试!Cmd");
    }

    /**
     * 全局熔断处理
     *
     * @return
     */
    @RequestMapping("/fallback")
    public Mono<String> fallback() {
        return Mono.just("访问超时，请稍后再试!");
    }


}