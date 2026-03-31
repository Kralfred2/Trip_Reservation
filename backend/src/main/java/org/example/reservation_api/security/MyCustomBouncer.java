package org.example.reservation_api.security;


import lombok.RequiredArgsConstructor;
import org.example.reservation_api.DTO.LoginRequest;
import org.example.reservation_api.DTO.LoginResponse;
import org.example.reservation_api.entities.User;
import org.example.reservation_api.repositories.UserRepository;
import org.example.reservation_api.services.JwtService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service // Changed from @Component to @Service for clarity
@RequiredArgsConstructor
public class MyCustomBouncer {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    /**
     * This is your main door. It handles the 'How' of the login.
     */
    public LoginResponse tryLogin(LoginRequest credentials) {
        // 1. Find User
        User user = userRepository.findByEmail(credentials.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        // 2. Check Password
        if (!passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        long expiresInSeconds = 3600;
        String jwt = jwtService.generateTimedToken(user, expiresInSeconds);
        System.out.println("Generated JWT: " + jwt);
        return new LoginResponse(jwt, expiresInSeconds, user.getUsername(), user.getEmail(), "Login successful");
    }
}