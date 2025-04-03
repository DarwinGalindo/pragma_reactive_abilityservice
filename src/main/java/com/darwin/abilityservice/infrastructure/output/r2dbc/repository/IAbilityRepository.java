package com.darwin.abilityservice.infrastructure.output.r2dbc.repository;

import com.darwin.abilityservice.infrastructure.output.r2dbc.entity.AbilityEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface IAbilityRepository extends ReactiveCrudRepository<AbilityEntity, Long> {
}
