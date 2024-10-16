package de.supercode.backend.controller;

import de.supercode.backend.dtos.token.TokenDTO;
import de.supercode.backend.dtos.user.UserDTO;
import de.supercode.backend.dtos.user.UserRegDTO;
import de.supercode.backend.services.AuthService;
import de.supercode.backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    AuthService authService;

    UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/signin")
    public TokenDTO login(Authentication authentication) {
        return userService.getTokenByLogin(authentication);
    }

    @PostMapping("/register")
    public UserDTO register(@RequestBody @Valid UserRegDTO userRegDTO) {
        return authService.registerUser(userRegDTO);
    }

    // "/logout" Frontend intern routing to del JWT
}
