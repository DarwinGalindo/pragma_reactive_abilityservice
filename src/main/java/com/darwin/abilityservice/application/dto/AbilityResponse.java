package com.darwin.abilityservice.application.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbilityResponse {
    private Long id;
    private String name;
    private String description;
    private Integer technologiesCount;
    private List<TechnologyDto> technologies;
}
