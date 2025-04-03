package com.darwin.abilityservice.domain.spi;

import com.darwin.abilityservice.domain.model.Ability;
import reactor.core.publisher.Mono;

public interface IAbilityPersistencePort {
    Mono<Ability> createAbility(Ability ability);
}
