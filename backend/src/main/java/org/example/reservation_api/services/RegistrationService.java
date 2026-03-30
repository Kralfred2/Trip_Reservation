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


    public String tryRegister(RegistrationRequest request){

        try {
            String encodedPassword = passwordEncoder.encode(request.password());
            userRepository.sp_register_user(
                    request.username(),
                    request.email(),
                    encodedPassword
            );
            return "Success";
        } catch (Exception e) {
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }
}