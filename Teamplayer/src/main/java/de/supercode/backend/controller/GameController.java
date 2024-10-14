package de.supercode.backend.controller;

import de.supercode.backend.dtos.enemies.EnemyListDTO;
import de.supercode.backend.dtos.fight.AnalyticDTO;
import de.supercode.backend.services.GameService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public AnalyticDTO FightWithRandomEnemy(@PathVariable int choice, Authentication authentication) {
        return gameService.randomFightChoice(choice, authentication);
    }

    @PostMapping("/fight/{choice}/{enemyName}")
    public AnalyticDTO FightWithEnemy(@PathVariable int choice, @PathVariable String enemyName, Authentication authentication) {
        return gameService.fightWithEnemy(choice, enemyName, authentication);
    }
}
