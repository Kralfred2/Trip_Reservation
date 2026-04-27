package org.example.reservation_api.DTO;

public record AdminUserUpdateRequest(
        String username,
        String email,
        String role

) {
}
