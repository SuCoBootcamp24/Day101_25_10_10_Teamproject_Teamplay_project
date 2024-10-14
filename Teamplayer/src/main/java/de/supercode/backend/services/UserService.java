package de.supercode.backend.services;

import de.supercode.backend.dtos.token.TokenDTO;
import de.supercode.backend.dtos.user.UserDashDTO;
import de.supercode.backend.entities.Team;
import de.supercode.backend.entities.User;
import de.supercode.backend.mapper.TeamMapper;
import de.supercode.backend.repositorys.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private AuthService authService;
    private TeamMapper teamMapper;


    public UserService(UserRepository userRepository, AuthService authService, TeamMapper teamMapper) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.teamMapper = teamMapper;
    }

    public TokenDTO getTokenByLogin(Authentication authentication) {
        getUserByEmail(authentication.getName());
        String token = authService.getToken(authentication);
        return new TokenDTO(token);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user with '" + email + "' not found."));
    }

    public User findUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("user with ID '" + userId + "' not found."));
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
        User user = getUserByEmail(authentication.getName());
        int totalRatio = calculateRatio(user.getWins(), user.getLosses());

        if (user.getTeam() != null) {
            Team team = user.getTeam();
            int teamRatio = calculateRatio(team.getWins(), team.getLosses());
            return new UserDashDTO(
                    user.getId(),
                    user.getName(),
                    totalRatio,
                    teamMapper.toDTO(team),
                    teamRatio
            );
        } else {
            return new UserDashDTO(
                    user.getId(),
                    user.getName(),
                    totalRatio,
                    null,
                    0
            );
        }
    }

    private int calculateRatio(int wins, int losses) {
        int totalGames = wins + losses;
        if (totalGames == 0) {
            return 0;
        }
        return (int) (((double) wins / totalGames) * 100);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("User with '" + name + "' not found."));
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
