package com.darwin.abilityservice.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfiguration {

    @Value("${microservices.tech-service.api-base}")
    private String techServiceApiBase;

    @Bean("technologies")
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl(techServiceApiBase)
                .filter(jwtPropagationFilter())
                .build();
    }

    private ExchangeFilterFunction jwtPropagationFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(request ->
                ReactiveSecurityContextHolder.getContext()
                        .flatMap(ctx -> {
                            Authentication auth = ctx.getAuthentication();
                            if (auth != null && auth.getCredentials() instanceof String token) {
                                return Mono.just(ClientRequest.from(request)
                                        .headers(headers -> headers.setBearerAuth(token))
                                        .build());
                            }
                            return Mono.just(request);
                        })
                        .switchIfEmpty(Mono.just(request))
        );
    }
}
