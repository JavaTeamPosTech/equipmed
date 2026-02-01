package com.postechfiap.apigateway.filter;

import com.postechfiap.apigateway.config.ApiKeyRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ApiKeyGlobalFilter implements GlobalFilter, Ordered {

    private final ApiKeyRegistry registry;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // APIs de transparência (GET) costumam ser públicas,
        // mas o cadastro (POST) precisa de chave.
        if (exchange.getRequest().getMethod().name().equals("GET") && path.contains("/transparencia")) {
            return chain.filter(exchange);
        }

        String apiKey = exchange.getRequest().getHeaders().getFirst("X-API-KEY");

        if (apiKey == null || !registry.getKeys().containsKey(apiKey)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Recupera os dados vinculados à chave
        var unidade = registry.getKeys().get(apiKey);

        // Enriquecemos a requisição com Headers que o Microserviço vai ler
        ServerHttpRequest request = exchange.getRequest().mutate()
                .header("X-Unidade-Nome", unidade.unidadeDeSaude())
                .header("X-Unidade-Cidade", unidade.cidade())
                .header("X-Unidade-Estado", unidade.estado())
                .build();

        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return -1; // Alta prioridade: executa antes de rotear
    }
}