package com.darwin.abilityservice.infrastructure.output.webclient.mapper;

import com.darwin.abilityservice.domain.model.Technology;
import com.darwin.abilityservice.infrastructure.output.webclient.dto.TechnologyResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface TechnologyDtoMapper {
    Technology toModel(TechnologyResponse technologyResponse);
}
