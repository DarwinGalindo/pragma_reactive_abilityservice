package com.darwin.abilityservice.infrastructure.output.r2dbc.repository;

import com.darwin.abilityservice.infrastructure.output.r2dbc.entity.AbilityTechnologyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface IAbilityTechnologyRepository extends ReactiveCrudRepository<AbilityTechnologyEntity, Long> {
}
