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
    private TeamRepository teamRepository;
    private UserService userService;
    private PlayerService playerService;
    private PlayerMapper playerMapper;


    public TeamService(TeamRepository teamRepository, UserService userService, PlayerService playerService, PlayerMapper playerMapper) {
        this.teamRepository = teamRepository;
        this.userService = userService;
        this.playerService = playerService;
        this.playerMapper = playerMapper;
    }

    @Transactional
    public TeamResponseDTO createTeam(TeamCreateRequestDTO dto, Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());

        if (user.getTeam() != null) {
            deleteExistingTeam(user);
        }

        Team team = buildTeam(dto, user);
        team = teamRepository.save(team);
        userService.setTeam(user.getId(), team);

        return mapToTeamResponseDTO(team);
    }


    private void deleteExistingTeam(User user) {
        Team oldTeam = user.getTeam();
        userService.deleteTeam(user.getId());
        teamRepository.delete(oldTeam);
    }

    private Team buildTeam(TeamCreateRequestDTO dto, User user) {
        Team team = new Team();
        team.setOwnerId(user.getId());
        team.setName(dto.teamName());
        team.setPlayers(playerService.creatNewPlayerSet(dto));
        return team;
    }

    private TeamResponseDTO mapToTeamResponseDTO(Team team) {
        return new TeamResponseDTO(
                team.getName(),
                team.getPlayers().stream()
                        .map(player -> new PlayerListResponseDTO(
                                player.getName(),
                                player.getPlayerType().name(),
                                player.getPowerlevel()
                        ))
                        .collect(Collectors.toList())
        );
    }


    public void setWins(long teamId) {
        Team team = getTeamById(teamId);
        team.setWins(team.getWins() + 1);
        teamRepository.save(team);
    }

    public void setLosses(long teamId) {
        Team team = getTeamById(teamId);
        team.setLosses(team.getLosses() + 1);
        teamRepository.save(team);
    }

    private Team getTeamById(long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team nicht gefunden mit ID: " + teamId));
    }

}
