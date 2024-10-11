package de.supercode.backend.controller;

import de.supercode.backend.dtos.enemies.EnemyListDTO;
import de.supercode.backend.services.GameService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/game")
public class GameController {
    GameService gameService;


    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    //----------- ENEMY ---------
    @GetMapping("/enemies")
    public List<EnemyListDTO> getEnemies(Authentication authentication) {
        return gameService.getEnemies(authentication);
    }

    //-----------Choice fight-----------

    @GetMapping("/fight/random/{choice}")
    public boolean FightWithRandomEnemy(@PathVariable int choice, Authentication authentication) {
        return gameService.randomFightChoice(choice, authentication);

    }
}
