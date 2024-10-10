package de.supercode.backend.dtos;

import de.supercode.backend.entities.Team;

public record UserDTO(
        long id,
        String name,
        String email,
        int winRatio,

        Team team,

        String token
) {
}
