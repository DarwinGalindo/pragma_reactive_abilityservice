package com.darwin.abilityservice.infrastructure.input.router;

import com.darwin.abilityservice.application.dto.AbilityRequest;
import com.darwin.abilityservice.application.dto.AbilityResponse;
import com.darwin.abilityservice.application.dto.TechnologyDto;
import com.darwin.abilityservice.application.handler.IAbilityHandler;
import com.darwin.abilityservice.infrastructure.util.Routes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(AbilityRouter.class)
@Import(TestSecurityConfig.class)
class AbilityRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private IAbilityHandler abilityHandler;

    @Test
    void testCreate() {
        var request = new AbilityRequest("Tech 1", "Desc", List.of(1L, 2L));
        var response = new AbilityResponse(1L, "Tech 1", "Desc", 3,
                List.of(new TechnologyDto(1L, "Name 1"), new TechnologyDto(2L, "Name 2")));

        when(abilityHandler.create(any(ServerRequest.class)))
                .thenReturn(ServerResponse.status(HttpStatus.CREATED).bodyValue(response));

        when(abilityHandler.create(any(ServerRequest.class)))
                .thenReturn(ServerResponse.status(HttpStatus.CREATED).bodyValue(response));

        webTestClient.post()
                .uri(Routes.ABILITY_RESOURCE)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AbilityResponse.class)
                .isEqualTo(response);
    }

    @Test
    void testPaginate() {
        var response1 = new AbilityResponse(1L, "Ability 1", "Desc", 0, List.of());
        var response2 = new AbilityResponse(2L, "Ability 2", "Desc", 0, List.of());

        when(abilityHandler.paginate(any(ServerRequest.class)))
                .thenReturn(ServerResponse.ok().bodyValue(List.of(response1, response2)));

        webTestClient.get()
                .uri(Routes.ABILITY_RESOURCE)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AbilityResponse.class)
                .hasSize(2)
                .contains(response1, response2);
    }

    @Test
    void testFindById() {
        var techId = 1L;
        var response = new AbilityResponse(techId, "Tech 1", "Desc", 1,
                List.of(new TechnologyDto(1L, "Tech 1")));

        when(abilityHandler.findById(any(ServerRequest.class)))
                .thenReturn(ServerResponse.ok().bodyValue(response));

        webTestClient.get()
                .uri(Routes.ABILITY_RESOURCE_ID, techId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AbilityResponse.class)
                .isEqualTo(response);
    }

    @Test
    void testExistsById() {
        var techId = 1L;
        var exists = true;

        when(abilityHandler.existsById(any(ServerRequest.class)))
                .thenReturn(ServerResponse.ok().bodyValue(exists));

        webTestClient.get()
                .uri(Routes.ABILITY_RESOURCE_ID_EXISTS, techId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(exists);
    }
}