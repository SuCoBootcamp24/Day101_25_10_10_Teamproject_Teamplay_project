package de.supercode.backend.dtos.team;

import de.supercode.backend.dtos.player.PlayerListResponseDTO;

import java.util.List;

public record TeamResponseDTO(

        String teamName,

        List<PlayerListResponseDTO> Players

        ) {
}
