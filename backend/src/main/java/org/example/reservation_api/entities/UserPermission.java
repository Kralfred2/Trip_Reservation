package org.example.reservation_api.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "user_permissions")
public class UserPermission extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "permission_name", nullable = false)
    private String permissionName; // e.g., "email", "view_user"

    @Column(name = "access_level", nullable = false)
    private String accessLevel;

    @Column(name = "target_id" )
    private UUID targetId; // Optional: The specific ID this applies to (null = global)

}
