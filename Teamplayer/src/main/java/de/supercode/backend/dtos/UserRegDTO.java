package de.supercode.backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRegDTO(
        @NotBlank
        String name,
        @Email
        String email,
        @NotBlank
        String password
) {
}
