package com.darwin.abilityservice.infrastructure.security.configuration;

import com.darwin.abilityservice.infrastructure.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorizationService implements ReactiveAuthenticationManager {
    private final JwtProvider jwtProvider;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = (String) authentication.getCredentials();

        if (!jwtProvider.validateToken(token)) {
            return Mono.empty();
        }
        String username = jwtProvider.getUsernameFromToken(token);
        List<String> roles = jwtProvider.getRolesFromToken(token);

        var authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();

        return Mono.just(new UsernamePasswordAuthenticationToken(username, token, authorities));
    }
}
