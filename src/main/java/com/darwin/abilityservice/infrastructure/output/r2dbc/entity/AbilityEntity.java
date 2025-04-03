package com.darwin.abilityservice.infrastructure.output.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(name = "ability")
@AllArgsConstructor
@NoArgsConstructor
public class AbilityEntity {
    @Id
    private Long id;
    private String name;
    private String description;
}
