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
        players.add(createNewPlayer(dto.Player1Name(), dto.Player1Type()));
        players.add(createNewPlayer(dto.Player2Name(), dto.Player2Type()));
        players.add(createNewPlayer(dto.Player3Name(), dto.Player3Type()));
        players.add(createNewPlayer(dto.Player4Name(), dto.Player4Type()));
        players.add(createNewPlayer(dto.Player5Name(), dto.Player5Type()));
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
