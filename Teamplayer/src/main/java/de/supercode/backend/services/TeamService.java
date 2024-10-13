package de.supercode.backend.services;

import de.supercode.backend.dtos.player.PlayerListResponseDTO;
import de.supercode.backend.dtos.team.TeamCreateRequestDTO;
import de.supercode.backend.dtos.team.TeamResponseDTO;
import de.supercode.backend.entities.Team;
import de.supercode.backend.entities.User;
import de.supercode.backend.mapper.PlayerMapper;
import de.supercode.backend.repositorys.TeamRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class TeamService {
    TeamRepository teamRepository;
    UserService userService;

    PlayerService playerService;

    PlayerMapper playerMapper;


    public TeamService(TeamRepository teamRepository, UserService userService, PlayerService playerService, PlayerMapper playerMapper) {
        this.teamRepository = teamRepository;
        this.userService = userService;
        this.playerService = playerService;
        this.playerMapper = playerMapper;
    }

    @Transactional
    public TeamResponseDTO createTeam(TeamCreateRequestDTO dto, Authentication authentication) {
        User initUser = userService.getUserByEmail(authentication.getName());
        User user = userService.findUserById(dto.userId());

        if (user == null || user.getId() != initUser.getId()) throw new RuntimeException(" your can't create a team for other User");

        if (user.getTeam() != null) {
            Team oldTeam = user.getTeam();
            userService.deleteTeam(user.getId());
            teamRepository.delete(oldTeam);
        }

        Team team = new Team();
        team.setOwnerId(user.getId());
        team.setName(dto.teamName());
        team.setPlayers(playerService.creatNewPlayerSet(dto));

        team = teamRepository.save(team);

        userService.setTeam(user.getId(), team);

        return new TeamResponseDTO(
                team.getName(),
                team.getPlayers().stream()
                        .map(player -> { return new PlayerListResponseDTO(
                                player.getName(),
                                player.getPlayerType().name(),
                                player.getPowerlevel()
                        );
                        })
                        .collect(Collectors.toList())
        );
    }

    public void setWins(long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found"));
        team.setWins(team.getWins() + 1);
        teamRepository.save(team);
    }

    public void setlosses(long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found"));
        team.setLosses(team.getLosses() + 1);
        teamRepository.save(team);
    }
}
