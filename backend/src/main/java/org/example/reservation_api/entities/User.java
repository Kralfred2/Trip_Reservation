package org.example.reservation_api.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;


@Data
@Entity // CRITICAL: Tells JPA this is a managed entity
@Table(name = "app_user")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    // REMOVED: private UUID id; (Inherited from BaseEntity)

    private String username;
    private String email;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles_mapping", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role_name")
    private Set<String> usersRoles = new HashSet<>();

    public boolean isAdmin() {
        return usersRoles.contains("ROLE_ADMIN");
    }
    // If UserPermission is another entity, use @OneToMany.
    // If it's a simple DTO, you might need @Transient or @ElementCollection.
    @Transient
    private Set<UserPermission> permissions = new HashSet<>();
}
