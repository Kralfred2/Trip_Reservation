package org.example.reservation_api.DTO;

import java.util.Set;
import java.util.UUID;

public record RoleAssignmentRequest(
        UUID targetGroupId,
        Set<UUID> userIds,
        UUID roleId // Use UUID instead of String for dynamic lookup
) {}