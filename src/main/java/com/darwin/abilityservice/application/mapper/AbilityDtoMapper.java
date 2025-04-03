package com.darwin.abilityservice.application.mapper;

import com.darwin.abilityservice.application.dto.AbilityRequest;
import com.darwin.abilityservice.application.dto.AbilityResponse;
import com.darwin.abilityservice.domain.model.Ability;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE,
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AbilityDtoMapper {
    Ability toModel(AbilityRequest abilityRequest);

    @Mapping(target = "technologies", source = "technologyList")
    AbilityResponse toResponse(Ability ability);
}
