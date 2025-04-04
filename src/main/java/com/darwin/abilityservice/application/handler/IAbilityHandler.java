package com.darwin.abilityservice.application.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface IAbilityHandler {
    Mono<ServerResponse> create(ServerRequest request);
    Mono<ServerResponse> paginate(ServerRequest request);
    Mono<ServerResponse> findById(ServerRequest request);
    Mono<ServerResponse> existsById(ServerRequest request);
}
