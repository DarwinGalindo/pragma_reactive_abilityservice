package com.darwin.abilityservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.darwin.abilityservice.application.util.Messages.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AbilityRequest {
    @NotBlank(message = ABILITY_NAME_NOT_BLANK)
    @Size(max = 50, message = ABILITY_NAME_SIZE)
    private String name;

    @NotBlank(message = ABILITY_DESCRIPTION_NOT_BLANK)
    @Size(max = 90, message = ABILITY_DESCRIPTION_SIZE)
    private String description;

    @NotNull(message = ABILITY_TECHNOLOGY_NOT_NULL)
    @Size(min = 3, max = 20, message = ABILITY_TECHNOLOGY_SIZE)
    private List<Long> technologyIds;
}
