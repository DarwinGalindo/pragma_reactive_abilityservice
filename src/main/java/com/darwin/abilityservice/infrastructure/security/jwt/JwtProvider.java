package com.darwin.abilityservice.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.List;

@Slf4j
@Component
public class JwtProvider {
    private static final String ROLES_CLAIM = "roles";

    private Key key;
    private JwtParser jwtParser;

    @Value("${jwt.secret}")
    private String secret;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);

        key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public boolean validateToken(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            log.error("Token validation error {}", e.getMessage());
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        return jwtParser.parseClaimsJws(token).getBody().getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = jwtParser
                .parseClaimsJws(token)
                .getBody();

        Object roles = claims.get(ROLES_CLAIM);
        if (roles instanceof List<?>) {
            return ((List<?>) roles).stream()
                    .map(String::valueOf)
                    .toList();
        }
        return List.of();
    }
}
