package de.supercode.backend.dtos.team;

import de.supercode.backend.dtos.player.PlayerListResponseDTO;
import de.supercode.backend.entities.Player;
import de.supercode.backend.entities.Team;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TeamCreateResponseDTO(

        String teamName,

        List<PlayerListResponseDTO> Players

        ) {
}
