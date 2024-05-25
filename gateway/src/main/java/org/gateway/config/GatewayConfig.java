package org.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import org.gateway.filter.RequestLogGatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class GatewayConfig {

    @PostConstruct
    public void init() {
        BlockRequestHandler blockRequestHandler = new BlockRequestHandler() {
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
                Map<String, String> result = new HashMap<>();
                result.put("code", HttpStatus.TOO_MANY_REQUESTS.toString());
                result.put("msg", "服务压力过大，请稍后重试!");
                return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(result));
            }
        };
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }

    @Bean
    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes().route(r -> r.path("/app1/**").filters(f -> f.filter(new RequestLogGatewayFilter())).uri("http://localhost:9001")).build();
    }
    // static imports from GatewayFilters and RoutePredicates
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, ThrottleGatewayFilterFactory throttle) {
//
//
//        return builder.routes()
//                .route(r -> r.host("**.abc.org").and().path("/image/png")
//                        .filters(f ->
//                                f.addResponseHeader("X-TestHeader", "foobar"))
//                        .uri("http://httpbin.org:80")
//                )
//                .route(r -> r.path("/image/webp")
//                        .filters(f ->
//                                f.addResponseHeader("X-AnotherHeader", "baz"))
//                        .uri("http://httpbin.org:80")
//                        .metadata("key", "value")
//                )
//                .route(r -> r.order(-1)
//                        .host("**.throttle.org").and().path("/get")
//                        .filters(f -> f.filter(throttle.apply(1,
//                                1,
//                                10,
//                                TimeUnit.SECONDS)))
//                        .uri("http://httpbin.org:80")
//                        .metadata("key", "value")
//                )
//                .build();
//    }
}