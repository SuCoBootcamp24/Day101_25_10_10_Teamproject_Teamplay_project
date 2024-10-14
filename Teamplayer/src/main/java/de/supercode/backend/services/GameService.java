package de.supercode.backend.services;

import de.supercode.backend.dtos.enemies.EnemyListDTO;
import de.supercode.backend.dtos.fight.AnalyticDTO;
import de.supercode.backend.entities.Team;
import de.supercode.backend.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class GameService {

    private UserService userService;
    private FightSystemService fightSystemService;


    public GameService(UserService userService, FightSystemService fightSystemService) {
        this.userService = userService;
        this.fightSystemService = fightSystemService;
    }

    public List<User> getEnemyUsers(Authentication authentication) {
        User initUser = userService.getUserByEmail(authentication.getName());
        List<User> allUsers = userService.getAllUsers();

        return allUsers.stream()
                .filter(user -> user.getId() != initUser.getId())
                .filter(user -> user.getTeam() != null)
                .sorted(Comparator.comparingInt(u -> u.getTeam().getWins() - u.getTeam().getLosses()))
                .collect(Collectors.toList());
    }

    public List<EnemyListDTO> convertToEnemyDTOs(List<User> enemies) {
        return enemies.stream()
                .map(user -> {
                    Team team = user.getTeam();;
                    int teamRatio = calculateRatio(team.getWins(), team.getLosses());
                    int totalRatio = calculateRatio(user.getWins(), user.getLosses());

                    return new EnemyListDTO(
                            team.getName(),
                            teamRatio,
                            user.getName(),
                            totalRatio
                    );
                })
                .collect(Collectors.toList());
    }

    private int calculateRatio(int wins, int losses) {
        int totalGames = wins + losses;
        if (totalGames == 0) {
            return 0;
        }
        return (int) (((double) wins / totalGames) * 100);
    }

    public List<EnemyListDTO> getEnemies(Authentication authentication) {
        return convertToEnemyDTOs(getEnemyUsers(authentication));
    }


    public AnalyticDTO randomFightChoice(int choice, Authentication authentication) {
        User initUser = userService.getUserByEmail(authentication.getName());
        List<User> enemies = getEnemies(authentication).stream()
                .map(enemyDTO -> userService.getUserByName(enemyDTO.ownerName()))
                .toList();

        if (enemies.isEmpty()) {
            throw new IllegalStateException("Keine Gegner verfügbar.");
        }

        User enemy = enemies.get(new Random().nextInt(enemies.size()));
        return performFight(choice, initUser, enemy);
    }

    public AnalyticDTO fightWithEnemy(int choice, String enemyName, Authentication authentication) {
        User initUser = userService.getUserByEmail(authentication.getName());
        User enemy = userService.getUserByName(enemyName);

        if (enemy == null) {
            throw new IllegalArgumentException("Gegner nicht gefunden: " + enemyName);
        }

        return performFight(choice, initUser, enemy);
    }


    private AnalyticDTO performFight(int choice, User initUser, User enemy) {
        switch (choice) {
            case 0:
                return fightSystemService.standardFightSystem(initUser, enemy);
            case 1:
                return fightSystemService.experimentalFightSystem(initUser, enemy);
            default:
                throw new IllegalArgumentException("Unbekannte Auswahl für das Kampfsystem: " + choice);
        }
    }

}
