package com.darwin.abilityservice.application.handler;

import com.darwin.abilityservice.application.dto.AbilityRequest;
import com.darwin.abilityservice.application.dto.AbilityResponse;
import com.darwin.abilityservice.application.mapper.AbilityDtoMapper;
import com.darwin.abilityservice.application.validation.LocalValidator;
import com.darwin.abilityservice.domain.api.IAbilityServicePort;
import com.darwin.abilityservice.domain.model.Ability;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class AbilityHandlerTest {
    private IAbilityServicePort abilityServicePort;
    private AbilityDtoMapper abilityDtoMapper;
    private LocalValidator validator;
    private IAbilityHandler abilityHandler;
    private ServerRequest serverRequest;

    @BeforeEach
    void setUp() {
        abilityServicePort = mock(IAbilityServicePort.class);
        abilityDtoMapper = mock(AbilityDtoMapper.class);
        validator = mock(LocalValidator.class);
        abilityHandler = new AbilityHandler(abilityServicePort, abilityDtoMapper, validator);
        serverRequest = mock(ServerRequest.class);
    }

    @Test
    void create() {
        var request = new AbilityRequest("Ability", "Description", List.of(1L));
        var ability = new Ability();
        var abilityResponse = new AbilityResponse(1L, "Ability", "Description", 1, null);

        when(serverRequest.bodyToMono(AbilityRequest.class)).thenReturn(Mono.just(request));
        doNothing().when(validator).validate(request);
        when(abilityDtoMapper.toModel(request)).thenReturn(ability);
        when(abilityServicePort.create(ability)).thenReturn(Mono.just(ability));
        when(abilityDtoMapper.toResponse(ability)).thenReturn(abilityResponse);

        StepVerifier.create(abilityHandler.create(serverRequest))
                .expectNextMatches(response -> {
                    assertEquals(HttpStatus.CREATED, response.statusCode());
                    return true;
                })
                .expectComplete()
                .verify();

        verify(serverRequest).bodyToMono(AbilityRequest.class);
        verify(validator).validate(request);
        verify(abilityDtoMapper).toModel(request);
        verify(abilityServicePort).create(ability);
        verify(abilityDtoMapper).toResponse(ability);
    }

    @Test
    void paginate() {
        when(serverRequest.queryParam(anyString())).thenReturn(Optional.of("0"));
        when(abilityServicePort.paginate(anyInt(), anyInt(), anyString(), anyBoolean()))
                .thenReturn(Flux.just(new Ability(), new Ability()));
        when(abilityDtoMapper.toResponse(any())).thenReturn(new AbilityResponse());

        StepVerifier.create(abilityHandler.paginate(serverRequest))
                .expectNextMatches(response -> {
                    assertEquals(HttpStatus.OK, response.statusCode());
                    return true;
                })
                .expectComplete()
                .verify();

        verify(abilityServicePort).paginate(anyInt(), anyInt(), anyString(), anyBoolean());
        verify(serverRequest).queryParam("page");
        verify(serverRequest).queryParam("size");
        verify(serverRequest).queryParam("sortProperty");
        verify(serverRequest).queryParam("sortAscending");
        verify(abilityDtoMapper, times(2)).toResponse(any());
    }

    @Test
    void findById() {
        var id = 1L;
        var tech = new Ability(id, "Ability", "Description", 1);
        var response = new AbilityResponse(id, tech.getName(), tech.getDescription(), 1, null);

        when(serverRequest.pathVariable("id")).thenReturn(String.valueOf(id));
        when(abilityServicePort.findById(id)).thenReturn(Mono.just(tech));
        when(abilityDtoMapper.toResponse(tech)).thenReturn(response);

        StepVerifier.create(abilityHandler.findById(serverRequest))
                .expectNextMatches(serverResponse -> {
                    assertThat(serverResponse.statusCode()).isEqualTo(HttpStatus.OK);
                    return true;
                })
                .expectComplete()
                .verify();

        verify(abilityServicePort).findById(id);
        verify(abilityDtoMapper).toResponse(tech);
        verify(serverRequest).pathVariable("id");
    }

    @Test
    void existsById() {
        var id = 1L;

        when(serverRequest.pathVariable("id")).thenReturn(String.valueOf(id));
        when(abilityServicePort.existsById(id)).thenReturn(Mono.just(true));

        StepVerifier.create(abilityHandler.existsById(serverRequest))
                .expectNextMatches(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                    return true;
                })
                .expectComplete()
                .verify();

        verify(abilityServicePort).existsById(id);
        verify(serverRequest).pathVariable("id");
    }
}