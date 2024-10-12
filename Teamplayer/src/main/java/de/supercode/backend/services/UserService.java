package de.supercode.backend.services;

import de.supercode.backend.dtos.team.TeamResponseDTO;
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

import java.util.List;

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
        int totalRatio = 0;
        if (user.getWins() > 0 || user.getLosses() > 0) {
            totalRatio = (int) (((double) user.getWins() / (user.getWins() + user.getLosses())) * 100);
        }
        if (user.getTeam() != null) {
            Team team = user.getTeam();;
            int teamRatio = 0;
            if (team.getWins() > 0 || team.getLosses() > 0) {
                teamRatio = (int) (((double) team.getWins() / (team.getWins() + team.getLosses())) * 100);
            }
            return new UserDashDTO(user.getId(), user.getName(), totalRatio, teamMapper.toDTO(team), teamRatio);
        }
        else return new UserDashDTO(user.getId(), user.getName(), totalRatio, null, 0);

    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByName(String name) {
        return userRepository.findByName(name).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void setWins(long userid) {
        User user = findUserById(userid);
        user.setWins(user.getWins() + 1);
        userRepository.save(user);
    }

    public void setLosses(long userId) {
        User user = findUserById(userId);
        user.setLosses(user.getLosses() + 1);
        userRepository.save(user);
    }
}
