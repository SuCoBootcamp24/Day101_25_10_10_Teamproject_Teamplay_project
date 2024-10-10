package de.supercode.backend.services;

import de.supercode.backend.dtos.user.UserDTO;
import de.supercode.backend.dtos.user.UserRegDTO;
import de.supercode.backend.entities.User;
import de.supercode.backend.repositorys.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private TokenService tokenService;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public UserDTO registerUser(UserRegDTO newUserDTO) {
        User newUser = new User();
        newUser.setName(newUserDTO.name());
        newUser.setEmail(newUserDTO.email());
        newUser.setPassword(passwordEncoder.encode(newUserDTO.password()));

        newUser = userRepository.save(newUser);

        return new UserDTO(newUser.getId(), newUser.getName() , newUser.getEmail(), 0, null, "");

    }


    public String getToken(Authentication authentication) {
        return tokenService.generateToken(authentication);
    }
}
