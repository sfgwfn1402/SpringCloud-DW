package org.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.handler.AsyncPredicate;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class Route implements Ordered {
   
     
    private final String id;
    private final URI uri;
    private final int order;
    private final AsyncPredicate<ServerWebExchange> predicate;
    private final List<GatewayFilter> gatewayFilters;
    private final Map<String, Object> metadata;

    /**
     * id	标识符，区别于其他 Route
     * uri	路由指向的目的地 uri，即客户端请求最终被转发的目的地
     * order	用于多个 Route 之间的排序，数值越小排序越靠前，匹配优先级越高
     * predicate	谓语，表示匹配该 Route 的前置条件，即满足相应的条件才会被路由到目的地 uri
     * gatewayFilters	过滤器用于处理切面逻辑，如路由转发前修改请求头等
     * metadata	元数据，用于描述路由
     */
    public Route(String id, URI uri, int order, AsyncPredicate<ServerWebExchange> predicate, List<GatewayFilter> gatewayFilters, Map<String, Object> metadata) {
        this.id = id;
        this.uri = uri;
        this.order = order;
        this.predicate = predicate;
        this.gatewayFilters = gatewayFilters;
        this.metadata = metadata;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
