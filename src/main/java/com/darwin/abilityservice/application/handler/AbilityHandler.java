package com.darwin.abilityservice.application.handler;

import com.darwin.abilityservice.application.dto.AbilityRequest;
import com.darwin.abilityservice.application.mapper.AbilityDtoMapper;
import com.darwin.abilityservice.application.validation.LocalValidator;
import com.darwin.abilityservice.domain.api.IAbilityServicePort;
import com.darwin.abilityservice.shared.Pagination;
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
    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(AbilityRequest.class)
                .doOnNext(localValidator::validate)
                .map(abilityDtoMapper::toModel)
                .flatMap(abilityServicePort::create)
                .flatMap(ability -> ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(abilityDtoMapper.toResponse(ability)));
    }

    @Override
    public Mono<ServerResponse> paginate(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam(Pagination.PAGE_PARAM).orElse(Pagination.DEFAULT_PAGE));
        int size = Integer.parseInt(request.queryParam(Pagination.SIZE_PARAM).orElse(Pagination.DEFAULT_SIZE));
        boolean sortAscending = request.queryParam(Pagination.SORT_ASCENDING).orElse(Pagination.DEFAULT_ASCENDING)
                .equals(Pagination.ASCENDING_TRUE);
        String sortProperty = request.queryParam(Pagination.SORT_PROPERTY).orElse("name");

        return abilityServicePort.paginate(page, size, sortProperty, sortAscending)
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

    @Override
    public Mono<ServerResponse> existsById(ServerRequest request) {
        return abilityServicePort.existsById(Long.parseLong(request.pathVariable("id")))
                .flatMap(exist -> ServerResponse.ok().bodyValue(exist));
    }
}
