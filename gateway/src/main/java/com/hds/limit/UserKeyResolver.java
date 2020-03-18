package com.hds.limit;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class UserKeyResolver implements KeyResolver {
    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        System.out.println(exchange.getRequest().getId());
        return Mono.just(exchange.getRequest().getId());
    }
}
