package com.example.apigateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();
      ServerHttpResponse response = (ServerHttpResponse) exchange.getResponse();

      log.info("Custom PRE filter : request uri -> {}", request.getId());
      return chain.filter(exchange).then(Mono.fromRunnable(() -> {
        log.info("Custom POST filter : response code -> {}", response.getStatusCode());
      }));
    };
  }

  public static class Config {
    // put the configuration properties
  }

  public CustomFilter() {
    super(Config.class);
  }


}
