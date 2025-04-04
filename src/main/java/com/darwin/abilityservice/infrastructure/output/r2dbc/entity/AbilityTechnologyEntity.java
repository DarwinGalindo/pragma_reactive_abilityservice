package com.darwin.abilityservice.infrastructure.output.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("ability_technology")
public class AbilityTechnologyEntity {
    @Id
    private Long id;
    private Long abilityId;
    private Long technologyId;
}
