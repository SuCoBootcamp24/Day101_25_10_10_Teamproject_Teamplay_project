package de.supercode.backend.dtos.fight;

import de.supercode.backend.dtos.player.PlayerListResponseDTO;

public record AnalyticPartDTO(

        PlayerListResponseDTO playerA,
        PlayerListResponseDTO playerB,

        boolean partIsDraw,
        boolean partIsWon
) {
}
