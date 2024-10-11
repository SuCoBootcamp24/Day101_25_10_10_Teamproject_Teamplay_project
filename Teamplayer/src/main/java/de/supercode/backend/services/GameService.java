package de.supercode.backend.services;

import de.supercode.backend.dtos.enemies.EnemyListDTO;
import de.supercode.backend.entities.Team;
import de.supercode.backend.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class GameService {

    UserService userService;

    FightSystemService fightSystemService;

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
                .collect(Collectors.toList());
    }

    public List<EnemyListDTO> convertToEnemyDTOs(List<User> enemies) {
        return enemies.stream()
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

    public List<EnemyListDTO> getEnemies(Authentication authentication) {
        return convertToEnemyDTOs(getEnemyUsers(authentication));
    }



//    public List<EnemyListDTO> getEnemies(Authentication authentication) {
//        User initUser = userService.getUserByEmail(authentication.getName());
//        List<User> allUsers = userService.getAllUsers();
//        return allUsers.stream()
//                .filter(user -> user.getId() != initUser.getId())
//                .filter(user -> user.getTeam() != null)
//                .map(user -> {
//                    Team team = user.getTeam();
//
//                    int teamRatio = 0;
//                    if (team.getWins() > 0 || team.getLosses() > 0) {
//                        teamRatio = team.getWins() / (team.getWins() + team.getLosses()) * 100;
//                    }
//
//                    int totalRatio = 0;
//                    if (user.getWins() > 0 || user.getLosses() > 0) {
//                        totalRatio = user.getWins() / (user.getWins() + user.getLosses()) * 100;
//                    }
//
//                    return new EnemyListDTO(
//                            team.getName(),
//                            teamRatio,
//                            user.getName(),
//                            totalRatio
//                            );
//                })
//                .collect(Collectors.toList());
//    }

    public boolean randomFightChoice(int choice, Authentication authentication) {
        User initUser = userService.getUserByEmail(authentication.getName());
        List<User> enemies = getEnemyUsers(authentication);

        if (enemies.isEmpty()) return false;
        User enemy = enemies.get(new Random().nextInt(enemies.size()));

        return fightSystemChoicer(choice, initUser, enemy);
    }


    public boolean fightWithEnemy(int choice, String enemyName, Authentication authentication) {
        User initUser = userService.getUserByEmail(authentication.getName());
        User enemy = userService.getUserByName(enemyName);

        return fightSystemChoicer(choice, initUser, enemy);
    }

    private boolean fightSystemChoicer(int choice, User initUser, User enemy) {
        switch (choice) {
            case 0: return fightSystemService.standardFightSystem(initUser, enemy);
            case 1: break;//system 2
            default: throw new RuntimeException("Unknown choice (Fightsystem)");
        }
        return false;
    }



}
