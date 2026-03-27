package org.example.reservation_api.controllers;

import org.example.reservation_api.DTO.LoginRequest;
import org.example.reservation_api.entities.User;
import org.example.reservation_api.repositories.UserRepository;
import org.example.reservation_api.services.JwtService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    public AuthController(UserRepository userRepository,
                          JwtService jwtService,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return jwtService.generateToken(user.getUsername());
            } else {
                return "Invalid password!";
            }
        }
        return "User not found!";
    }
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        userRepository.save(user);

        return "User registered successfully with hashed password!";
    }

}
