package de.supercode.backend.dtos.team;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record TeamCreateRequestDTO(
        @NotNull
        long userId,

        @NotBlank
        String teamName,

        @NotBlank
        String Player1Name,

        String Player1Type,

        @NotBlank
        String Player2Name,

        String Player2Type,

        @NotBlank
        String Player3Name,

        String Player3Type,

        @NotBlank
        String Player4Name,

        String Player4Type,

        @NotBlank
        String Player5Name,

        String Player5Type
        ) {
}
