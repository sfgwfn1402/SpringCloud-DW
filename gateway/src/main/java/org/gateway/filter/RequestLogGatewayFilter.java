package org.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * 但是这种方式需要用JAVA代码编写路由信息添加过滤器，所以不是很推荐使用
 */
@Slf4j
public class RequestLogGatewayFilter implements GatewayFilter, Ordered {
    /**
     * @param exchange 网络交换机
     * @param chain    过滤器链
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求路径
        URI uri = exchange.getRequest().getURI();
        log.info("RequestLogGatewayFilter,获取到请求路径：{}", uri.toString());
        return chain.filter(exchange);
    }

    /**
     * 设置过滤器执行顺序，数值越低，优先级越搞
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
