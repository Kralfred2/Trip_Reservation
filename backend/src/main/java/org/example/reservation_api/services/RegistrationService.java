package org.example.reservation_api.services;

import org.example.reservation_api.DTO.RegistrationRequest;
import org.example.reservation_api.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UUID register(RegistrationRequest request) {
        // 1. Logic: Hash the password in Java
        String encodedPassword = passwordEncoder.encode(request.password());

        // 2. Call the Stored Procedure (The Transaction/Rollback happens here)
        try {
            return userRepository.sp_register_user(
                    request.username(),
                    encodedPassword,
                    request.email()
            );
        } catch (Exception e) {
            // This catches the RAISE EXCEPTION from your Postgres Procedure
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }
}