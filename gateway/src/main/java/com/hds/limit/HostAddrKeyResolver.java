package com.hds.limit;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class HostAddrKeyResolver implements KeyResolver {
    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        System.out.println(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
        return Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
}
