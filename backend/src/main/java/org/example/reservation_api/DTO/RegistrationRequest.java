package org.example.reservation_api.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(


        @NotBlank
        String username,


        String email,

        @NotBlank
        String password
) {}
