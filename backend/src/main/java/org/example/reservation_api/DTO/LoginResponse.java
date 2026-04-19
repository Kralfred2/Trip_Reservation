package org.example.reservation_api.DTO;


import java.util.Set;

public record LoginResponse(
        String token,
        long expiresAt,
        String username,
        String email,
        String role,
        Set<String> permissions,
        String message
) {}
