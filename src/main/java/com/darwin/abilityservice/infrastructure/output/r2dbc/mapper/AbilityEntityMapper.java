package com.darwin.abilityservice.infrastructure.output.r2dbc.mapper;

import com.darwin.abilityservice.domain.model.Ability;
import com.darwin.abilityservice.infrastructure.output.r2dbc.entity.AbilityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AbilityEntityMapper {
    Ability toModel(AbilityEntity abilityEntity);

    AbilityEntity toEntity(Ability ability);
}
