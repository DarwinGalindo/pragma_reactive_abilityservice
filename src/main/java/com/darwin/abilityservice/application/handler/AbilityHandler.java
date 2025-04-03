package com.darwin.abilityservice.application.handler;

import com.darwin.abilityservice.application.dto.AbilityRequest;
import com.darwin.abilityservice.application.mapper.AbilityDtoMapper;
import com.darwin.abilityservice.application.validation.LocalValidator;
import com.darwin.abilityservice.domain.api.IAbilityServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AbilityHandler implements IAbilityHandler {
    private final IAbilityServicePort abilityServicePort;
    private final AbilityDtoMapper abilityDtoMapper;
    private final LocalValidator localValidator;

    @Override
    public Mono<ServerResponse> createAbility(ServerRequest request) {
        return request.bodyToMono(AbilityRequest.class)
                .doOnNext(localValidator::validate)
                .map(abilityDtoMapper::toModel)
                .flatMap(abilityServicePort::createAbility)
                .flatMap(ability -> ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(abilityDtoMapper.toResponse(ability)));
    }
}
