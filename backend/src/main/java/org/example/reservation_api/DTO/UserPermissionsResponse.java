package org.example.reservation_api.DTO;

import org.example.reservation_api.entities.User;

import java.util.List;
import java.util.UUID;

public record UserPermissionsResponse(
        User targetUser,
        List<String> myActions // e.g., ["VIEW", "MODIFY"]
) {}
