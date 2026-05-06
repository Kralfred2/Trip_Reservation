package org.example.reservation_api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPermission {
    private UUID id;
    private UUID userId;
    private String permissionName;
    private UUID targetId;
}
