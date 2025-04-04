package com.darwin.abilityservice.infrastructure.output.webclient.adapter;

import com.darwin.abilityservice.domain.model.Technology;
import com.darwin.abilityservice.infrastructure.output.webclient.dto.TechnologyResponse;
import com.darwin.abilityservice.infrastructure.output.webclient.mapper.TechnologyDtoMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class TechnologyWebClientAdapterTest {
    private WebClient webClient;
    private TechnologyDtoMapper technologyDtoMapper;
    private TechnologyWebClientAdapter technologyWebClientAdapter;
    private ExchangeFunction exchangeFunction;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        exchangeFunction = mock(ExchangeFunction.class);
        webClient = WebClient.builder().exchangeFunction(exchangeFunction).build();
        technologyDtoMapper = mock(TechnologyDtoMapper.class);
        technologyWebClientAdapter = new TechnologyWebClientAdapter(webClient, technologyDtoMapper);
        objectMapper = new ObjectMapper();
    }

    @Test
    void findById() throws JsonProcessingException {
        TechnologyResponse response = new TechnologyResponse(1L, "Tech", "Backend tech");
        Technology tech = new Technology(1L, "Tech", "Backend tech");

        ClientResponse clientResponse = ClientResponse
                .create(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(response))
                .build();

        when(exchangeFunction.exchange(any(ClientRequest.class))).thenReturn(Mono.just(clientResponse));
        when(technologyDtoMapper.toModel(any(TechnologyResponse.class))).thenReturn(tech);

        Mono<Technology> result = technologyWebClientAdapter.findById(1L);

        StepVerifier.create(result)
                .expectNext(tech)
                .verifyComplete();
    }

    @Test
    void existsById() {
        ClientResponse clientResponse = ClientResponse
                .create(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body("true")
                .build();

        when(exchangeFunction.exchange(any(ClientRequest.class))).thenReturn(Mono.just(clientResponse));

        Mono<Boolean> result = technologyWebClientAdapter.existsById(1L);

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }
}