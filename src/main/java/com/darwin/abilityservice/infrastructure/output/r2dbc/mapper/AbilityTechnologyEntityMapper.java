package com.darwin.abilityservice.infrastructure.output.r2dbc.mapper;

import com.darwin.abilityservice.domain.model.AbilityTechnology;
import com.darwin.abilityservice.infrastructure.output.r2dbc.entity.AbilityTechnologyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
unmappedSourcePolicy = ReportingPolicy.IGNORE,
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AbilityTechnologyEntityMapper {
    AbilityTechnology toModel(AbilityTechnologyEntity abilityTechnologyEntity);
}
