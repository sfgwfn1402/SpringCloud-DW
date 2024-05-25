package org.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Component
public class RouteRecordGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 记录实际调用的URL，// RouteToRequestUrlFilter会把实际路由的URL通过该属性保存
        URI proxyRequestUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        long start = System.currentTimeMillis();

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long end = System.currentTimeMillis();
            log.info("实际调用地址为: {}，调用耗时为：{}ms", proxyRequestUri, (end - start));
        }));
    }

    @Override
    public int getOrder() {
        // 优先级设为最低，先让RouteToRequestUrlFilter先调用
        return Ordered.LOWEST_PRECEDENCE;
    }
}