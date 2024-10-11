package de.supercode.backend.services;

import de.supercode.backend.dtos.team.TeamCreateRequestDTO;
import de.supercode.backend.entities.Player;
import de.supercode.backend.entities.PlayerTypes;
import de.supercode.backend.repositorys.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
public class PlayerService {

    PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }


    public Set<Player> creatNewPlayerSet(TeamCreateRequestDTO dto) {
        Set<Player> players = new HashSet<>();
        players.add(createNewPlayer(dto.player1Name(), dto.player1Type()));
        players.add(createNewPlayer(dto.player2Name(), dto.player2Type()));
        players.add(createNewPlayer(dto.player3Name(), dto.player3Type()));
        players.add(createNewPlayer(dto.player4Name(), dto.player4Type()));
        players.add(createNewPlayer(dto.player5Name(), dto.player5Type()));
        return players;
    }

    private Player createNewPlayer(String name, String type) {
        Player player = new Player(name, type);
        player.setPowerlevel(randomPowerlevel(player.getPlayerType()));
        return player;
    }

    private int randomPowerlevel(PlayerTypes type) {
        Random random = new Random();
        return random.nextInt((type.getMaxRange() - type.getMinRange()) + 1) + type.getMinRange();
    }
}
