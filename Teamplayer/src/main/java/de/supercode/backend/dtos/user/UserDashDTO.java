package de.supercode.backend.dtos.user;

import de.supercode.backend.dtos.team.TeamResponseDTO;
import de.supercode.backend.entities.Team;

public record UserDashDTO(

        long id,
        String name,
        int winRatio,

        TeamResponseDTO teamDTO,

        int teamRation
) {
}
