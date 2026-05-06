package org.example.reservation_api.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.reservation_api.DTO.UserListResponse;
import org.example.reservation_api.entities.User;
import org.example.reservation_api.entities.UserPermission;
import org.example.reservation_api.entities.UserRole;
import org.example.reservation_api.projections.GlobalCapabilityProjection;
import org.example.reservation_api.repositories.BaseRepository;
import org.example.reservation_api.repositories.PermissionRepository;
import org.example.reservation_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService extends BaseService<User, UserRepository> {

    private final PermissionRepository permissionRepository;

    protected UserService(UserRepository repository, PermissionRepository permissionRepository) {
        super(repository);
        this.permissionRepository = permissionRepository;
    }

    public User adminUpdateUser(UUID targetId, User updatedData) {
        // Now 'dbFindById' returns Optional<User> because of the BaseRepository fix
        return repository.dbFindById(targetId).map(existingUser -> {
            existingUser.setUsername(updatedData.getUsername());
            existingUser.setEmail(updatedData.getEmail());
            syncPermissions(existingUser.getId(), updatedData.getPermissions());
            return repository.dbUpdate(existingUser);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Replaces the manual loop with a clean "Wipe and Rewrite" strategy
     */
    private void syncPermissions(UUID userId, Set<UserPermission> newPermissions) {
        // 1. Remove all old records for this user in the DB
        permissionRepository.clearUserPermissions(userId);

        // 2. Insert the new dynamic set
        for (UserPermission perm : newPermissions) {
            permissionRepository.insertPermission(
                    userId,
                    perm.getPermissionName(),
                    perm.getTargetId()
            );
        }
    }

    /**
     * Logic for viewing the user list, checking for the Global Admin role first
     */
    public Set<UserListResponse> getAccessibleUsersSummary(User actor) {
        // Global Admin role check
        if (actor.getUsersRoles().contains("ROLE_ADMIN")) {
            return repository.findAll().stream()
                    .map(u -> new UserListResponse(u.getId(), u.getUsername(), true))
                    .collect(Collectors.toSet());
        }

        // Contextual list based on the access summary view
        List<Object[]> rows = repository.findUserSummaryList(actor.getId());

        return rows.stream()
                .map(row -> new UserListResponse(
                        (UUID) row[0],
                        (String) row[1],
                        (Boolean) row[2]
                ))
                .collect(Collectors.toSet());
    }

    /**
     * Dynamic permission check that considers global vs targeted scope
     */
    public boolean hasPermission(User user, String action, UUID targetId) {
        return user.getPermissions().stream()
                .anyMatch(p -> p.getPermissionName().equals(action) &&
                        (p.getTargetId() == null || p.getTargetId().equals(targetId)));
    }

    @Transactional
    public void updateUserTargetRelationships(UUID userId, List<UUID> targetIds) {
        // 1. Verify the user exists
        User user = repository.dbFindById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Clear existing targeted permissions
        permissionRepository.clearUserPermissions(userId);

        // 3. Insert new relationships with a specific permission name (e.g., "TARGET_ACCESS")
        for (UUID targetId : targetIds) {
            permissionRepository.insertPermission(
                    userId,
                    "TARGET_ACCESS", // You can customize this permission name
                    targetId
            );
        }
    }
    public User selfUpdateUser(String currentUsername, User updatedData) {
        // 1. Fetch user by username (Ensure UserRepository.findByUsername returns Optional<User>)
        return repository.findByUsername(currentUsername).map(existingUser -> {

            // 2. Update allowed fields only (Safety check)
            if (updatedData.getEmail() != null) {
                existingUser.setEmail(updatedData.getEmail());
            }

            // Note: Password updates usually require a PasswordEncoder bean
            // if (updatedData.getPassword() != null) {
            //    existingUser.setPassword(passwordEncoder.encode(updatedData.getPassword()));
            // }

            // 3. Persist changes using the base repository update
            return repository.dbUpdate(existingUser);
        }).orElseThrow(() -> new RuntimeException("User not found: " + currentUsername));
    }
}