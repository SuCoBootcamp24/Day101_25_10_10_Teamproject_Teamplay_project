package de.supercode.backend.services;

import de.supercode.backend.dtos.enemies.EnemyListDTO;
import de.supercode.backend.entities.Team;
import de.supercode.backend.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {

    UserService userService;

    
    public GameService(UserService userService) {
        this.userService = userService;
    }

    public List<EnemyListDTO> getEnemies(Authentication authentication) {
        User initUser = userService.getUserByEmail(authentication.getName());
        List<User> allUsers = userService.getAllUsers();
        return allUsers.stream()
                .filter(user -> user.getId() != initUser.getId())
                .filter(user -> user.getTeam() != null)
                .map(user -> {
                    Team team = user.getTeam();

                    int teamRatio = 0;
                    if (team.getWins() > 0 || team.getLosses() > 0) {
                        teamRatio = team.getWins() / (team.getWins() + team.getLosses()) * 100;
                    }

                    int totalRatio = 0;
                    if (user.getWins() > 0 || user.getLosses() > 0) {
                        totalRatio = user.getWins() / (user.getWins() + user.getLosses()) * 100;
                    }

                    return new EnemyListDTO(
                            team.getName(),
                            teamRatio,
                            user.getName(),
                            totalRatio
                            );
                })
                .collect(Collectors.toList());
    }
}
