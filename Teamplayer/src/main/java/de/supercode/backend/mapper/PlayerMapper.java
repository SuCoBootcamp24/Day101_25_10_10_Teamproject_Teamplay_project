package de.supercode.backend.mapper;

import de.supercode.backend.dtos.player.PlayerListResponseDTO;
import de.supercode.backend.dtos.team.TeamResponseDTO;
import de.supercode.backend.entities.Player;
import de.supercode.backend.entities.Team;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PlayerMapper {

    public PlayerListResponseDTO toDTO(Player player) {
        return new PlayerListResponseDTO(
                player.getName(),
                player.getPlayerType().name(),
                player.getPowerlevel()
        );
    }

}
