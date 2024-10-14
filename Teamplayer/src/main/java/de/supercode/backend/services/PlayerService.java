package de.supercode.backend.services;

import de.supercode.backend.dtos.team.TeamCreateRequestDTO;
import de.supercode.backend.entities.Player;
import de.supercode.backend.entities.PlayerTypes;
import de.supercode.backend.repositorys.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    private PlayerRepository playerRepository;

    private final Random random = new Random();


    private final int MAX_ROOKIES = 1;
    private final int MAX_NORMALS = 2;
    private final int MAX_VETERANS = 1;
    private final int MAX_LEGENDARYS = 1;


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

        // Überprüfe die Anzahl der Spielertypen
        Map<PlayerTypes, Long> typeCounts = players.stream()
                .collect(Collectors.groupingBy(Player::getPlayerType, Collectors.counting()));

        // Bedingungen überprüfen
        if (typeCounts.getOrDefault(PlayerTypes.ROOKIE, 0L) != MAX_ROOKIES ||
                typeCounts.getOrDefault(PlayerTypes.NORMAL, 0L) != MAX_NORMALS ||
                typeCounts.getOrDefault(PlayerTypes.VETERAN, 0L) != MAX_VETERANS ||
                typeCounts.getOrDefault(PlayerTypes.LEGENDARY, 0L) != MAX_LEGENDARYS) {
            throw new IllegalArgumentException("Ungültige Teamzusammenstellung: Es muss 1 Rookie, 2 Normal, 1 Veteran und 1 Legendär geben.");
        }

        return players;
    }

    private Player createNewPlayer(String name, String type) {
        Player player = new Player(name, type);
        player.setPowerlevel(randomPowerlevel(player.getPlayerType()));
        return player;
    }

    private int randomPowerlevel(PlayerTypes type) {
        return random.nextInt(type.getMaxRange() - type.getMinRange() + 1) + type.getMinRange();
    }
}
