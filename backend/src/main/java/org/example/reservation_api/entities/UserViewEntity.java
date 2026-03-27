package org.example.reservation_api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.annotation.Immutable;

import java.util.UUID;

@Entity
@Immutable // Tells JPA this is read-only (since it's a View)
@Table(name = "v_user_summary")
public class UserViewEntity {
    @Id
    private UUID id;
    private String username;
    private String email;
    private String roleName;
    // Getters only (no setters needed for a View)
}
