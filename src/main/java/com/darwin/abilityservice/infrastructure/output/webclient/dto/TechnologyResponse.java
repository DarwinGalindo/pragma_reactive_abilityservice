package com.darwin.abilityservice.infrastructure.output.webclient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TechnologyResponse {
    private Long id;
    private String name;
    private String description;
}
