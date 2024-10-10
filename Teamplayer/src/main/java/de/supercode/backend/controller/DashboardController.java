package de.supercode.backend.controller;


import de.supercode.backend.dtos.team.TeamCreateRequestDTO;
import de.supercode.backend.dtos.team.TeamCreateResponseDTO;
import de.supercode.backend.services.TeamService;
import de.supercode.backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/dash")
public class DashboardController {

    UserService userService;

    TeamService teamService;

    public DashboardController(UserService userService, TeamService teamService) {
        this.userService = userService;
        this.teamService = teamService;
    }

    @PostMapping("/team")
    public TeamCreateResponseDTO createNewTeam(@RequestBody @Valid TeamCreateRequestDTO dto, Authentication authentication) {
        return teamService.createTeam(dto, authentication);
    }



}
