package de.supercode.backend.dtos.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TeamCreateRequestDTO(

        @NotBlank
        String teamName,

        @NotBlank
        String player1Name,

        String player1Type,

        @NotBlank
        String player2Name,

        String player2Type,

        @NotBlank
        String player3Name,

        String player3Type,

        @NotBlank
        String player4Name,

        String player4Type,

        @NotBlank
        String player5Name,

        String player5Type
        ) {
}
