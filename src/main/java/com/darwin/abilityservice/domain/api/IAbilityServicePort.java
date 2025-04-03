package com.darwin.abilityservice.domain.api;

import com.darwin.abilityservice.domain.model.Ability;
import reactor.core.publisher.Mono;

public interface IAbilityServicePort {
    Mono<Ability> createAbility(Ability ability);
}
