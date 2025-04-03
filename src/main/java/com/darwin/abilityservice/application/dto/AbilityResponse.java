package com.darwin.abilityservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AbilityResponse {
    private Long id;
    private String name;
    private String description;
    private Integer technologiesCount;
    private List<TechnologyDto> technologies;
}
