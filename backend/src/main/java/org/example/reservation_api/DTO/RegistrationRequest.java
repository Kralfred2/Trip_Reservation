package org.example.reservation_api.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @NotBlank(message = "Username cannot be empty")
        @Size(min = 3, max = 50)
        String username,

        @Email(message = "Invalid email format")
        String email,

        @NotBlank
        @Size(min = 8)
        String password
) {}
