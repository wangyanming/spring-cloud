package com.hds.log.filter;

import com.alibaba.fastjson.JSONObject;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author admin
 * @date 2020/3/26
 */
@Component
public class OauthLogFilter implements GlobalFilter, Ordered {

    static final Logger logger = LogManager.getLogger("request");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String token = request.getHeaders().get("Authorization").toString().replace("Bearer ", "").replace("[","").replace("]", "");
        StringBuffer logBuffer = new StringBuffer();
        Map<String, String> params = parseRequest(exchange, logBuffer);
        boolean r = checkSignature(token);

        if(!r) {
            Map map = new HashMap<>();
            map.put("code", 2);
            map.put("message", "签名验证失败");
            String resp = JSONObject.toJSONString(map);
            logBuffer.append(",resp=").append(resp);
            logger.info(logBuffer.toString());
            DataBuffer bodyDataBuffer = response.bufferFactory().wrap(resp.getBytes());
            response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
            return response.writeWith(Mono.just(bodyDataBuffer));
        }

        DataBufferFactory bufferFactory = response.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.map(dataBuffer -> {
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);
                        DataBufferUtils.release(dataBuffer);
                        String resp = new String(content, Charset.forName("UTF-8"));
                        logBuffer.append(",resp=").append(resp);
                        logger.info(logBuffer.toString());
                        byte[] uppedContent = new String(content, Charset.forName("UTF-8")).getBytes();
                        return bufferFactory.wrap(uppedContent);
                    }));
                }
                return super.writeWith(body);
            }
        };
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    private Map<String, String> parseRequest(ServerWebExchange exchange, StringBuffer logBuffer) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String method = serverHttpRequest.getMethodValue().toUpperCase();
        logBuffer.append(method).append(",").append(serverHttpRequest.getURI());
        MultiValueMap<String, String> query = serverHttpRequest.getQueryParams();
        Map<String, String> params = new HashMap<>();
        query.forEach((k, v) -> {
            params.put(k, v.get(0));
        });
        if("POST".equals(method)) {
            String body = exchange.getAttributeOrDefault("cachedRequestBody", "");
            if(StringUtils.isNotBlank(body)) {
                logBuffer.append(",body=").append(body);
                String[] kvArray = body.split("&");
                for (String kv : kvArray) {
                    if (kv.indexOf("=") >= 0) {
                        String k = kv.split("=")[0];
                        String v = kv.split("=")[1];
                        if(!params.containsKey(k)) {
                            try {
                                params.put(k, URLDecoder.decode(v, "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                            }
                        }
                    }
                }
            }
        }
        return params;
    }

    public static void main(String[] args) throws ParseException {
        JWT jwt = JWTParser.parse("");
        System.out.println(jwt.getJWTClaimsSet());
    }

    /**
     * 校验token签名
     * @param token
     * @return
     */
    private boolean checkSignature(String token) {
        try {
            JWT jwt = JWTParser.parse(token);
            System.out.println(jwt.getJWTClaimsSet());
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
