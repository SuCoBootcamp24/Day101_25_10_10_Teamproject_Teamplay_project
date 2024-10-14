package de.supercode.backend.services;


import de.supercode.backend.dtos.fight.AnalyticDTO;
import de.supercode.backend.dtos.fight.AnalyticPartDTO;
import de.supercode.backend.entities.Player;
import de.supercode.backend.entities.User;
import de.supercode.backend.mapper.PlayerMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class FightSystemService {

    UserService userService;

    TeamService teamService;
    PlayerMapper playerMapper;

    public FightSystemService(UserService userService, TeamService teamService, PlayerMapper playerMapper) {
        this.userService = userService;
        this.teamService = teamService;
        this.playerMapper = playerMapper;
    }

    public AnalyticDTO standardFightSystem(User initUser, User enemy) {
        List<AnalyticPartDTO> parts = new ArrayList<>();
        List<Player> teamUser = new ArrayList<>(initUser.getTeam().getPlayers());
        List<Player> teamEnemy = new ArrayList<>(enemy.getTeam().getPlayers());

        while (true) {
            parts.clear();
            int userWins = 0;
            int enemyWins = 0;

            Collections.shuffle(teamUser);
            Collections.shuffle(teamEnemy);

            for (int i = 0; i < teamUser.size(); i++) {
                Player playerUser = teamUser.get(i);
                Player playerEnemy = teamEnemy.get(i);

                if (playerUser.getPowerlevel() > playerEnemy.getPowerlevel()) {
                    parts.add(new AnalyticPartDTO(playerMapper.toDTO(playerUser), playerMapper.toDTO(playerEnemy), false, true));
                    userWins++;
                } else if (playerUser.getPowerlevel() < playerEnemy.getPowerlevel()) {
                    parts.add(new AnalyticPartDTO(playerMapper.toDTO(playerUser), playerMapper.toDTO(playerEnemy), false, false));
                    enemyWins++;
                } else {
                    parts.add(new AnalyticPartDTO(playerMapper.toDTO(playerUser), playerMapper.toDTO(playerEnemy), true, false));
                }
            }
            if (userWins > enemyWins) {
                userService.setWins(initUser.getId());
                teamService.setWins(initUser.getTeam().getId());
                userService.setLosses(enemy.getId());
                teamService.setlosses(enemy.getTeam().getId());
                return new AnalyticDTO(true, parts);

            } else if (userWins < enemyWins) {
                userService.setWins(enemy.getId());
                teamService.setWins(enemy.getTeam().getId());
                userService.setLosses(initUser.getId());
                teamService.setlosses(initUser.getTeam().getId());
                return new AnalyticDTO(false, parts);

            }
        }
    }

    public AnalyticDTO experimentalFightSystem(User initUser, User enemy) {
        List<AnalyticPartDTO> parts = new ArrayList<>();

        while (true) {

            parts.clear();

            List<Player> teamUser = new ArrayList<>();
            for (Player p : initUser.getTeam().getPlayers()) {
                teamUser.add(new Player(p.getId(), p.getName(), p.getPowerlevel(), p.getPlayerType()));
            }
            List<Player> teamEnemy = new ArrayList<>();
            for (Player p : enemy.getTeam().getPlayers()) {
                teamEnemy.add(new Player(p.getId(), p.getName(), p.getPowerlevel(), p.getPlayerType()));
            }

            Collections.shuffle(teamUser);
            Collections.shuffle(teamEnemy);

            for (int teamUserPlayers = 0; teamUserPlayers < teamUser.size(); teamUserPlayers++) {
                Player userPlayer = teamUser.get(teamUserPlayers);
                if (userPlayer.getPowerlevel() > 0) {
                    for (int teamEnemyPlayers = 0; teamEnemyPlayers < teamEnemy.size(); teamEnemyPlayers++) {
                        Player enemyPlayer = teamEnemy.get(teamEnemyPlayers);

                        if (enemyPlayer.getPowerlevel() > 0) {
                            System.out.println("u " + userPlayer.getPowerlevel() + " E" + enemyPlayer.getPowerlevel());

                            if (userPlayer.getPowerlevel() > enemyPlayer.getPowerlevel()) {
                                parts.add(new AnalyticPartDTO(playerMapper.toDTO(userPlayer), playerMapper.toDTO(enemyPlayer), false, true));
                                userPlayer.setPowerlevel(userPlayer.getPowerlevel() - enemyPlayer.getPowerlevel());
                                enemyPlayer.setPowerlevel(0);

                            } else if (userPlayer.getPowerlevel() < enemyPlayer.getPowerlevel()) {
                                parts.add(new AnalyticPartDTO(playerMapper.toDTO(userPlayer), playerMapper.toDTO(enemyPlayer), false, false));
                                enemyPlayer.setPowerlevel(enemyPlayer.getPowerlevel() - userPlayer.getPowerlevel());
                                userPlayer.setPowerlevel(0);
                                break;
                            } else {
                                parts.add(new AnalyticPartDTO(playerMapper.toDTO(userPlayer), playerMapper.toDTO(enemyPlayer), true, false));
                                userPlayer.setPowerlevel(0);
                                enemyPlayer.setPowerlevel(0);
                                break;
                            }

                        }
                    }
                }
            }

            boolean userTeamAlive = teamUser.stream().anyMatch(player -> player.getPowerlevel() > 0);
            boolean enemyTeamAlive = teamEnemy.stream().anyMatch(player -> player.getPowerlevel() > 0);

            if (!userTeamAlive) {
                userService.setWins(enemy.getId());
                teamService.setWins(enemy.getTeam().getId());
                userService.setLosses(initUser.getId());
                teamService.setlosses(initUser.getTeam().getId());
                return new AnalyticDTO(false, parts);
            } else if (!enemyTeamAlive) {
                userService.setWins(initUser.getId());
                teamService.setWins(initUser.getTeam().getId());
                userService.setLosses(enemy.getId());
                teamService.setlosses(enemy.getTeam().getId());
                return new AnalyticDTO(true, parts);
            }
        }
    }
}
