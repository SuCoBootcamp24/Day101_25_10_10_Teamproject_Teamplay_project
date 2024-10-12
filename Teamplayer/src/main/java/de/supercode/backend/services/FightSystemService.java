package de.supercode.backend.services;


import de.supercode.backend.entities.Player;
import de.supercode.backend.entities.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class FightSystemService {

    UserService userService;

    TeamService teamService;

    public FightSystemService(UserService userService, TeamService teamService) {
        this.userService = userService;
        this.teamService = teamService;
    }

    public boolean standardFightSystem(User initUser, User enemy) {
        List<Player> teamUser = new ArrayList<>(initUser.getTeam().getPlayers());
        List<Player> teamEnemy = new ArrayList<>(enemy.getTeam().getPlayers());



        while (true) {
            int userWins = 0;
            int enemyWins = 0;

            Collections.shuffle(teamUser);
            System.out.println(teamUser);
            Collections.shuffle(teamEnemy);
            System.out.println(teamEnemy);

            for (int i = 0; i < teamUser.size(); i++) {
                Player playerUser = teamUser.get(i);
                Player playerEnemy = teamEnemy.get(i);

                if (playerUser.getPowerlevel() > playerEnemy.getPowerlevel()) {
                    userWins++;
                } else if (playerUser.getPowerlevel() < playerEnemy.getPowerlevel()) {
                    enemyWins++;
                }
            }
            if (userWins > enemyWins) {
                userService.setWins(initUser.getId());
                teamService.setWins(initUser.getTeam().getId());
                userService.setLosses(enemy.getId());
                teamService.setlosses(enemy.getTeam().getId());
                return true;

            } else if (userWins < enemyWins) {
                userService.setWins(enemy.getId());
                teamService.setWins(enemy.getTeam().getId());
                userService.setLosses(initUser.getId());
                teamService.setlosses(enemy.getTeam().getId());
                return false;

            }
        }
    }

    public boolean experimentalFightSystem(User initUser, User enemy) {
        List<String> analyses = new ArrayList<>();

        while (true) {

            analyses = new ArrayList<>();

            List<Player> teamUser = new ArrayList<>();
            for (Player p : initUser.getTeam().getPlayers()) {
                teamUser.add(new Player(p.getId(), p.getName(), p.getPowerlevel(), p.getPlayerType()));
            }
            List<Player> teamEnemy = new ArrayList<>();
            for (Player p : enemy.getTeam().getPlayers()) {
                teamEnemy.add(new Player(p.getId(), p.getName(), p.getPowerlevel(), p.getPlayerType()));
            }

            Collections.shuffle(teamUser);
            System.out.println(teamUser);
            Collections.shuffle(teamEnemy);
            System.out.println(teamEnemy);

            for (int teamUserPlayers = 0; teamUserPlayers < teamUser.size(); teamUserPlayers++) {
                Player userPlayer = teamUser.get(teamUserPlayers);
                if (userPlayer.getPowerlevel() > 0) {
                    for (int teamEnemyPlayers = 0; teamEnemyPlayers < teamEnemy.size(); teamEnemyPlayers++) {
                        Player enemyPlayer = teamEnemy.get(teamEnemyPlayers);

                        if (enemyPlayer.getPowerlevel() > 0) {

                            if (userPlayer.getPowerlevel() == enemyPlayer.getPowerlevel()) {
                                analyses.add(
                                        "- " + enemyPlayer.getPowerlevel() +
                                        " " + userPlayer.getName() + " " + userPlayer.getPowerlevel() +
                                        " <-> " + enemyPlayer.getName() + " " + enemyPlayer.getPowerlevel() +
                                        " - " + userPlayer.getPowerlevel());
                                userPlayer.setPowerlevel(userPlayer.getPowerlevel() - enemyPlayer.getPowerlevel());
                                enemyPlayer.setPowerlevel(0);
                            }

                            if (userPlayer.getPowerlevel() > enemyPlayer.getPowerlevel()) {
                                analyses.add(
                                        "- " + enemyPlayer.getPowerlevel() +
                                        " " + userPlayer.getName() + " " + userPlayer.getPowerlevel() +
                                        " -> " + enemyPlayer.getName() + " " + enemyPlayer.getPowerlevel());
                                userPlayer.setPowerlevel(userPlayer.getPowerlevel() - enemyPlayer.getPowerlevel());
                                enemyPlayer.setPowerlevel(0);

                            } else if (userPlayer.getPowerlevel() < enemyPlayer.getPowerlevel()) {
                                analyses.add(
                                        userPlayer.getName() + userPlayer.getPowerlevel() +
                                        " <- " + enemyPlayer.getName() + " " + userPlayer.getPowerlevel() +
                                        " - " + userPlayer.getPowerlevel());
                                enemyPlayer.setPowerlevel(enemyPlayer.getPowerlevel() - userPlayer.getPowerlevel());
                                userPlayer.setPowerlevel(0);

                                break;
                            }
                        }
                    }
                }
            }

            analyses.forEach(System.out::println);

            // Check if either team has no players left with power
            boolean userTeamAlive = teamUser.stream().anyMatch(player -> player.getPowerlevel() > 0);
            boolean enemyTeamAlive = teamEnemy.stream().anyMatch(player -> player.getPowerlevel() > 0);

            if (!userTeamAlive) {
                System.out.println("fight lose");
                userService.setWins(enemy.getId());
                teamService.setWins(enemy.getTeam().getId());
                userService.setLosses(initUser.getId());
                teamService.setlosses(enemy.getTeam().getId());
                return false;
            } else if (!enemyTeamAlive) {
                System.out.println("fight win");
                userService.setWins(initUser.getId());
                teamService.setWins(initUser.getTeam().getId());
                userService.setLosses(enemy.getId());
                teamService.setlosses(enemy.getTeam().getId());
                return true;
            }
            System.out.println("fight drawn");
        }
    }
}
