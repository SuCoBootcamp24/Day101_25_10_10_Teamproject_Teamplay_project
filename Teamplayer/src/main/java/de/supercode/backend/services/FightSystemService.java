package de.supercode.backend.services;


import de.supercode.backend.entities.Player;
import de.supercode.backend.entities.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        boolean isDraw = true;

        while (isDraw) {
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
                isDraw = false;
                return true;

            } else if (userWins < enemyWins) {
                userService.setWins(enemy.getId());
                teamService.setWins(enemy.getTeam().getId());
                userService.setLosses(initUser.getId());
                teamService.setlosses(enemy.getTeam().getId());
                isDraw = false;
                return false;

            } else {
                isDraw = true;
            }
        }
        return false; // if draw
    }

    public void experimentalFightSystem(User initUser, User enemy){
        List<Player> teamUser = new ArrayList<>(initUser.getTeam().getPlayers());
        List<Player> teamEnemy = new ArrayList<>(enemy.getTeam().getPlayers());

        boolean isDraw = true;

        while (isDraw) {

            Collections.shuffle(teamUser);
            System.out.println(teamUser);
            Collections.shuffle(teamEnemy);
            System.out.println(teamEnemy);

            for (int teamUserPlayers = 0; teamUserPlayers < teamUser.size(); teamUserPlayers++) {
                Player userPlayer = teamUser.get(teamUserPlayers);
                if (userPlayer.getPowerlevel() > 0 ) {
                    for (int teamEnemyPlayers = 0; teamEnemyPlayers < teamEnemy.size(); teamEnemyPlayers++) {
                        Player enemyPlayer = teamEnemy.get(teamEnemyPlayers);
                        System.out.println();
                        System.out.println("before = U: " + userPlayer.getPowerlevel() + " E: " + enemyPlayer.getPowerlevel());
                        if (enemyPlayer.getPowerlevel() > 0 ) {

                            if (userPlayer.getPowerlevel() > enemyPlayer.getPowerlevel()) {
                                userPlayer.setPowerlevel(userPlayer.getPowerlevel() - enemyPlayer.getPowerlevel());
                                enemyPlayer.setPowerlevel(0);
                                System.out.println("after U = U: " + userPlayer.getPowerlevel() + " E: " + enemyPlayer.getPowerlevel());

                            } else if (userPlayer.getPowerlevel() < enemyPlayer.getPowerlevel()) {
                                enemyPlayer.setPowerlevel(enemyPlayer.getPowerlevel() - userPlayer.getPowerlevel());
                                userPlayer.setPowerlevel(0);
                                System.out.println("after E = U: " + userPlayer.getPowerlevel() + " E: " + enemyPlayer.getPowerlevel());
                                break;
                            }
                        }
                    }
                }

            }
            break;

        }
        //return false; // if draw

    }

}
