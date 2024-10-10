package de.supercode.backend.services;

import de.supercode.backend.dtos.user.UserDTO;
import de.supercode.backend.entities.Team;
import de.supercode.backend.entities.User;
import de.supercode.backend.repositorys.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserRepository userRepository;

    AuthService authService;

    public UserService(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    public UserDTO getUserDetailsByLogin(Authentication authentication) {
        User existUser = getUserByEmail(authentication.getName());
        String token = authService.getToken(authentication);
        return new UserDTO(existUser.getId(), existUser.getName(), existUser.getEmail(), existUser.getWinRatio(), existUser.getTeam(), token);

    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void setTeam(long id, Team team) {
        User user = findUserById(id);
        user.setTeam(team);
        userRepository.save(user);
    }

    public void deleteTeam(long userId) {
        User user = findUserById(userId);
        user.setTeam(null);
        userRepository.save(user);
    }
}
