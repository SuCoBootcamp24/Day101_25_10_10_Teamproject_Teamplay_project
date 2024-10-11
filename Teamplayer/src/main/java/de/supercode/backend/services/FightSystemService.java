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


        while (!isDraw) {
            int userWins = 0;
            int enemyWins = 0;

            Collections.shuffle(teamUser);
            Collections.shuffle(teamEnemy);

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

}
