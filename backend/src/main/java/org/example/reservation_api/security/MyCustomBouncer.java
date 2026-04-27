package org.example.reservation_api.security;


import lombok.RequiredArgsConstructor;
import org.example.reservation_api.DTO.LoginRequest;
import org.example.reservation_api.DTO.LoginResponse;
import org.example.reservation_api.entities.Token;
import org.example.reservation_api.entities.User;
import org.example.reservation_api.projections.GlobalCapabilityProjection;
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

@Service
@RequiredArgsConstructor
public class MyCustomBouncer {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse tryLogin(LoginRequest credentials) {

        User user = userRepository.findByEmail(credentials.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));


        if (!passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        boolean canCheckOtherUsers = userRepository.findGlobalCapabilities(user.getId())
                .map(GlobalCapabilityProjection::getCanViewUserList) // Extract the boolean here
                .orElse(false);
        long expiresInSeconds = 3600;
        String jwt = jwtService.generateTimedToken(user, expiresInSeconds);
        System.out.println("Generated JWT: " + jwt);
        return new LoginResponse(
                jwt,
                3600,
                user.getUsername(),
                user.getEmail(),
                user.getRole().toString(),
                canCheckOtherUsers,
                "Login successful"
        );
    }

    public LoginResponse checkToken(String tokenString) {
        Token responceToken = jwtService.isTokenValid(tokenString);

        if (responceToken.getOwnerId() == null) {
            return new LoginResponse(null, 0, null, null,null,false,  responceToken.getMessage());
        } else {
            // Use findById safely
            return userRepository.findById(responceToken.getOwnerId())
                    .map(user -> {
                        long expiresInSeconds = 3600;
                        String jwt = jwtService.generateTimedToken(user, expiresInSeconds);
                        boolean canCheckOtherUsers = userRepository.findGlobalCapabilities(user.getId())
                                .map(GlobalCapabilityProjection::getCanViewUserList) // Extract the boolean here
                                .orElse(false);
                        return new LoginResponse(
                                jwt,
                                3600,
                                user.getUsername(),
                                user.getEmail(),
                                user.getRole().toString(),
                                canCheckOtherUsers,
                                "Login successful"
                        );
                    })
                    .orElseGet(() -> new LoginResponse(null, 0, null, null,null,false, "User no longer exists"));
        }
    }
}