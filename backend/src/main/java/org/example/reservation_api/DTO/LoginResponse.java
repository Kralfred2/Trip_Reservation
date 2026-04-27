package org.example.reservation_api.DTO;


import java.util.List;
import java.util.Set;

public record LoginResponse(
        String token,
        long expiresAt,
        String username,
        String email,
        String role,
        boolean canCheckOtherUsers,
        String message
) {}
