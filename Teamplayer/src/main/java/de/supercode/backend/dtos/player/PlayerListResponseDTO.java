package de.supercode.backend.dtos.player;

import de.supercode.backend.entities.Player;

public record PlayerListResponseDTO(

        String name,
        String type,
        int powerlevel
) {
}
