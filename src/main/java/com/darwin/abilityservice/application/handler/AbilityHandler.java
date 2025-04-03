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

    @Override
    public Mono<ServerResponse> filterAbilities(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
        String sortProperty = request.queryParam("sort").orElse("name");
        boolean sortAscending = request.queryParam("ascending").orElse("1").equals("1");

        return abilityServicePort.filterAbilities(page, size, sortProperty, sortAscending)
                .map(abilityDtoMapper::toResponse)
                .collectList()
                .flatMap(abilityResponses -> ServerResponse.ok().bodyValue(abilityResponses));
    }

    @Override
    public Mono<ServerResponse> findById(ServerRequest request) {
        return abilityServicePort.findById(Long.parseLong(request.pathVariable("id")))
                .map(abilityDtoMapper::toResponse)
                .flatMap(response -> ServerResponse.ok().bodyValue(response));
    }
}
