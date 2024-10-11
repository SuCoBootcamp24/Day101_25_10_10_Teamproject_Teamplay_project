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
public class TeamMapper {

    PlayerMapper playerMapper;


    public TeamMapper(PlayerMapper playerMapper) {
        this.playerMapper = playerMapper;
    }

    public TeamResponseDTO toDTO(Team team) {
        Set<Player> players = team.getPlayers();
        List<PlayerListResponseDTO> playerListDTOS = players.stream()
               .map(playerMapper::toDTO)
               .collect(Collectors.toList());
        return new TeamResponseDTO(
                team.getName(),
                playerListDTOS
        );
    }
}
