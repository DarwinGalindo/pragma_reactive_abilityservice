package com.darwin.abilityservice.infrastructure.input.router;

import com.darwin.abilityservice.application.handler.IAbilityHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.darwin.abilityservice.infrastructure.util.Routes.ABILITY_RESOURCE;
import static com.darwin.abilityservice.infrastructure.util.Routes.ABILITY_RESOURCE_ID;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class AbilityRouter {
    private final IAbilityHandler abilityHandler;

    @Bean
    public RouterFunction<ServerResponse> abilityRoutes() {
        return route()
                .POST(ABILITY_RESOURCE, abilityHandler::createAbility)
                .GET(ABILITY_RESOURCE, abilityHandler::filterAbilities)
                .GET(ABILITY_RESOURCE_ID, abilityHandler::findById)
                .build();
    }
}
