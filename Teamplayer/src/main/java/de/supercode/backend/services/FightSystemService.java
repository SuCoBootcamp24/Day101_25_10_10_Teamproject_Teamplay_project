package de.supercode.backend.services;


import de.supercode.backend.dtos.fight.AnalyticDTO;
import de.supercode.backend.dtos.fight.AnalyticPartDTO;
import de.supercode.backend.entities.Player;
import de.supercode.backend.entities.User;
import de.supercode.backend.mapper.PlayerMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
        int maxAttempts = 10;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            parts.clear();
            int userWins = 0;
            int enemyWins = 0;

            Collections.shuffle(teamUser);
            Collections.shuffle(teamEnemy);

            for (int i = 0; i < teamUser.size(); i++) {
                Player playerUser = teamUser.get(i);
                Player playerEnemy = teamEnemy.get(i);

                AnalyticPartDTO part = createAnalyticPart(playerUser, playerEnemy);
                parts.add(part);

                if (part.partIsDraw()) {
                    continue;
                }
                if (part.partIsWon()) {
                    userWins++;
                } else {
                    enemyWins++;
                }
            }

            if (userWins != enemyWins) {
                boolean userWon = userWins > enemyWins;
                updateUserAndTeamStats(initUser, enemy, userWon);
                return new AnalyticDTO(userWon, new ArrayList<>(parts));
            }
        }
        return new AnalyticDTO(false, new ArrayList<>(parts));
    }



    public AnalyticDTO experimentalFightSystem(User initUser, User enemy) {
        List<AnalyticPartDTO> parts = new ArrayList<>();

        while (true) {
            parts.clear();
            List<Player> teamUser = clonePlayers(initUser.getTeam().getPlayers());
            List<Player> teamEnemy = clonePlayers(enemy.getTeam().getPlayers());

            Collections.shuffle(teamUser);
            Collections.shuffle(teamEnemy);

            for (Player userPlayer : teamUser) {
                if (userPlayer.getPowerlevel() <= 0) {
                    continue;
                }
                for (Player enemyPlayer : teamEnemy) {
                    if (enemyPlayer.getPowerlevel() <= 0) {
                        continue;
                    }
                    AnalyticPartDTO part = createAnalyticPart(userPlayer, enemyPlayer);
                    parts.add(part);

                    int powerDifference = userPlayer.getPowerlevel() - enemyPlayer.getPowerlevel();

                    if (powerDifference > 0) {
                        userPlayer.setPowerlevel(powerDifference);
                        enemyPlayer.setPowerlevel(0);
                    } else if (powerDifference < 0) {
                        enemyPlayer.setPowerlevel(-powerDifference);
                        userPlayer.setPowerlevel(0);
                        break;
                    } else {
                        userPlayer.setPowerlevel(0);
                        enemyPlayer.setPowerlevel(0);
                        break;
                    }
                }
            }
            boolean userTeamAlive = teamUser.stream().anyMatch(player -> player.getPowerlevel() > 0);
            boolean enemyTeamAlive = teamEnemy.stream().anyMatch(player -> player.getPowerlevel() > 0);

            if (!userTeamAlive || !enemyTeamAlive) {
                boolean userWon = !enemyTeamAlive;
                updateUserAndTeamStats(initUser, enemy, userWon);
                return new AnalyticDTO(userWon, new ArrayList<>(parts));
            }
        }
    }


    private AnalyticPartDTO createAnalyticPart(Player userPlayer, Player enemyPlayer) {
        boolean partIsDraw = userPlayer.getPowerlevel() == enemyPlayer.getPowerlevel();
        boolean partIsWon = userPlayer.getPowerlevel() > enemyPlayer.getPowerlevel();
        return new AnalyticPartDTO(
                playerMapper.toDTO(userPlayer),
                playerMapper.toDTO(enemyPlayer),
                partIsDraw,
                partIsWon
        );
    }

    private void updateUserAndTeamStats(User initUser, User enemy, boolean userWon) {
        if (userWon) {
            userService.setWins(initUser.getId());
            teamService.setWins(initUser.getTeam().getId());
            userService.setLosses(enemy.getId());
            teamService.setLosses(enemy.getTeam().getId());
        } else {
            userService.setWins(enemy.getId());
            teamService.setWins(enemy.getTeam().getId());
            userService.setLosses(initUser.getId());
            teamService.setLosses(initUser.getTeam().getId());
        }
    }

    private List<Player> clonePlayers(Set<Player> players) {
        List<Player> clonedPlayers = new ArrayList<>();
        for (Player player : players) {
            clonedPlayers.add(new Player(
                    player.getId(),
                    player.getName(),
                    player.getPowerlevel(),
                    player.getPlayerType()
            ));
        }
        return clonedPlayers;
    }

}
