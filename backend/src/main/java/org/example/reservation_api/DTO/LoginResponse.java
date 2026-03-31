package org.example.reservation_api.DTO;


public record LoginResponse(
        String token,
        long expiresAt,
        String username,
        String email,
        String message
) {}
