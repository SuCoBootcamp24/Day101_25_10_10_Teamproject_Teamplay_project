package de.supercode.backend.services;

import de.supercode.backend.dtos.token.TokenDTO;
import de.supercode.backend.dtos.user.UserDTO;
import de.supercode.backend.dtos.user.UserDashDTO;
import de.supercode.backend.entities.Team;
import de.supercode.backend.entities.User;
import de.supercode.backend.mapper.PlayerMapper;
import de.supercode.backend.mapper.TeamMapper;
import de.supercode.backend.repositorys.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserRepository userRepository;

    AuthService authService;

    TeamMapper teamMapper;

    public UserService(UserRepository userRepository, AuthService authService, TeamMapper teamMapper) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.teamMapper = teamMapper;
    }

    public TokenDTO getTokenByLogin(Authentication authentication) {
        User existUser = getUserByEmail(authentication.getName());
        String token = authService.getToken(authentication);
        return new TokenDTO(token);
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

    public UserDashDTO getUserDashboard(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDashDTO(user.getId(), user.getName(), user.getWinRatio(), teamMapper.toDTO(user.getTeam()));
    }
}
