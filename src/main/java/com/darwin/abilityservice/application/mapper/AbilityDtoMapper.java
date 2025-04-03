package com.darwin.abilityservice.application.mapper;

import com.darwin.abilityservice.application.dto.AbilityRequest;
import com.darwin.abilityservice.application.dto.AbilityResponse;
import com.darwin.abilityservice.domain.model.Ability;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE,
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AbilityDtoMapper {
    Ability toModel(AbilityRequest abilityRequest);

    AbilityResponse toResponse(Ability ability);
}
