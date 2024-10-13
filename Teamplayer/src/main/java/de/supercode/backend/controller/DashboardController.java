package de.supercode.backend.controller;


import de.supercode.backend.dtos.enemies.EnemyListDTO;
import de.supercode.backend.dtos.team.TeamCreateRequestDTO;
import de.supercode.backend.dtos.team.TeamResponseDTO;
import de.supercode.backend.dtos.user.UserDashDTO;
import de.supercode.backend.services.TeamService;
import de.supercode.backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/dash")
public class DashboardController {

    UserService userService;

    TeamService teamService;

    public DashboardController(UserService userService, TeamService teamService) {
        this.userService = userService;
        this.teamService = teamService;
    }

    @GetMapping
    public UserDashDTO getUserDashboard(Authentication authentication) {
        return userService.getUserDashboard(authentication);
    }


    //----------- TEAM ---------
    @PostMapping("/team")
    public TeamResponseDTO createNewTeam(@RequestBody @Valid TeamCreateRequestDTO dto, Authentication authentication) {
        return teamService.createTeam(dto, authentication);
    }

    @DeleteMapping("/team")
    public void deleteTeam(Authentication authentication) {
        System.out.println("jeep");
        userService.deleteTeam(userService.getUserByEmail(authentication.getName()).getId());
    }



}
