package de.supercode.backend.services;

import de.supercode.backend.dtos.UserDTO;
import de.supercode.backend.dtos.UserRegDTO;
import de.supercode.backend.entities.User;
import de.supercode.backend.repositorys.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private TokenService tokenService;


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
