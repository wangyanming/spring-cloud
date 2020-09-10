package com.hds.oauth2.config;

import com.hds.oauth2.component.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.annotation.Resource;

@EnableWebFluxSecurity
@Configuration
public class ResourceServerConfig {

    @Resource
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public ResourceServerConfig(RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http.oauth2ResourceServer().authenticationEntryPoint(restAuthenticationEntryPoint);

        http.authorizeExchange()
                .pathMatchers("/actuator/**").permitAll()
                .anyExchange().authenticated()
                .and().exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint);

        http.oauth2ResourceServer().jwt();
        return http.build();
    }
}
