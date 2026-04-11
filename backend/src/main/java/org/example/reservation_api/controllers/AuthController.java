package org.example.reservation_api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.reservation_api.DTO.LoginRequest;
import org.example.reservation_api.DTO.LoginResponse;
import org.example.reservation_api.DTO.RegistrationRequest;
import org.example.reservation_api.security.AppSecurityProperties;
import org.example.reservation_api.security.MyCustomBouncer;
import org.example.reservation_api.services.JwtService;
import org.example.reservation_api.services.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final MyCustomBouncer bouncer;
    private final RegistrationService registrationService;
    private final AppSecurityProperties props;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

            return ResponseEntity.ok(bouncer.tryLogin(request));

        }


    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequest request) {

        String result = registrationService.tryRegister(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/validate")
    public ResponseEntity<LoginResponse> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        System.out.println("Generated header: " + authHeader);
        String token = authHeader.substring(7);
        System.out.println("Generated token without header: " + token);
        return ResponseEntity.ok(bouncer.checkToken(token));
    }
}
