package de.supercode.backend.services;

import de.supercode.backend.dtos.UserDTO;
import de.supercode.backend.dtos.UserRegDTO;
import de.supercode.backend.entities.User;
import de.supercode.backend.repositorys.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserRepository userRepository;

    AuthService authService;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO getUserDetailsByLogin(Authentication authentication) {
        User existUser = getUserByEmail(authentication.getName());
        String token = authService.getToken(authentication);
        return new UserDTO(existUser.getId(), existUser.getName(), existUser.getEmail(), existUser.getWinRatio(), existUser.getTeam(), token);

    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
