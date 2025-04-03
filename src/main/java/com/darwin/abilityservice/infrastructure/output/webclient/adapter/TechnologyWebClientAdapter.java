package com.darwin.abilityservice.infrastructure.output.webclient.adapter;

import com.darwin.abilityservice.domain.model.Technology;
import com.darwin.abilityservice.domain.spi.ITechnologyWebClientPort;
import com.darwin.abilityservice.infrastructure.output.webclient.dto.TechnologyResponse;
import com.darwin.abilityservice.infrastructure.output.webclient.mapper.TechnologyDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.darwin.abilityservice.infrastructure.util.Routes.TECHNOLOGY_RESOURCE_ID;
import static com.darwin.abilityservice.infrastructure.util.Routes.TECHNOLOGY_RESOURCE_ID_EXISTS;

@RequiredArgsConstructor
public class TechnologyWebClientAdapter implements ITechnologyWebClientPort {
    private final WebClient webClient;
    private final TechnologyDtoMapper technologyDtoMapper;

    @Override
    public Mono<Technology> findById(Long id) {
        return webClient.get()
                .uri(TECHNOLOGY_RESOURCE_ID, id)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, response -> Mono.empty())
                .bodyToMono(TechnologyResponse.class)
                .map(technologyDtoMapper::toModel);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return webClient.get()
                .uri(TECHNOLOGY_RESOURCE_ID_EXISTS, id)
                .retrieve()
                .bodyToMono(Boolean.class);
    }
}
