package org.example.reservation_api.controllers;

import jakarta.validation.Valid;
import org.example.reservation_api.DTO.LoginRequest;
import org.example.reservation_api.DTO.LoginResponse;
import org.example.reservation_api.DTO.RegistrationRequest;
import org.example.reservation_api.services.JwtService;
import org.example.reservation_api.services.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final JwtService jwtService;
    private final AuthenticationManager bouncer;
    private final RegistrationService registrationService;

    public AuthController(AuthenticationManager bouncer, JwtService jwtService, RegistrationService registrationService) {
        this.bouncer = bouncer;
        this.jwtService = jwtService;
        this.registrationService = registrationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            bouncer.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            String token = jwtService.generateToken(request.getUsername());
            return ResponseEntity.ok(new LoginResponse(true, token, "Welcome, have fun."));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401)
                    .body(new LoginResponse(false, null, "Get out."));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequest request) {
        // This triggers your Java Validation -> Service Logic -> DB Stored Procedure
        UUID newUserId = registrationService.register(request);

        return ResponseEntity.ok("User registered successfully with ID: " + newUserId);
    }
}
