package com.darwin.abilityservice.infrastructure.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.darwin.abilityservice.infrastructure.security.util.Constants.AUTH_HEADER;
import static com.darwin.abilityservice.infrastructure.security.util.Constants.BEARER_PREFIX;

@Component
@RequiredArgsConstructor
public class JwtSecurityContextRepository implements ServerSecurityContextRepository {
    private final ReactiveAuthenticationManager authenticationManager;

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String token = extractToken(exchange);
        if (token == null) return Mono.empty();

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(token, token);
        return authenticationManager.authenticate(auth).map(SecurityContextImpl::new);
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    private String extractToken(ServerWebExchange exchange) {
        var authHeader = exchange.getRequest().getHeaders().getFirst(AUTH_HEADER);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.replace(BEARER_PREFIX, "");
        }
        return null;
    }
}
