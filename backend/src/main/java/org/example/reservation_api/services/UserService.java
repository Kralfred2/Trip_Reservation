package org.example.reservation_api.services;

import org.example.reservation_api.DTO.RegistrationRequest;
import org.example.reservation_api.DTO.UserListResponse;
import org.example.reservation_api.DTO.UserPermissionsResponse;
import org.example.reservation_api.entities.User;
import org.example.reservation_api.entities.UserPermission;
import org.example.reservation_api.entities.UserRole;
import org.example.reservation_api.projections.GlobalCapabilityProjection;
import org.example.reservation_api.repositories.PermissionRepository;
import org.example.reservation_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService extends BaseService<User, UserRepository> {
    private final PermissionRepository permissionRepository;

    @Autowired
    public UserService(UserRepository repository, PermissionRepository permissionRepository) {
        super(repository);
        this.permissionRepository = permissionRepository;
    }
    public User adminUpdateUser(UUID targetId, User updatedData) {
        return repository.dbFindById(targetId).map(existingUser -> {
            existingUser.setUsername(updatedData.getUsername());
            existingUser.setEmail(updatedData.getEmail());
            existingUser.setRole(updatedData.getRole());

            syncPermissions(existingUser, updatedData.getPermissions());

            return repository.dbUpdate(existingUser);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User selfUpdateUser(String currentUsername, User updatedData) {
        User user = repository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(updatedData.getUsername());
        user.setEmail(updatedData.getEmail());

        return repository.dbUpdate(user);
    }
    public boolean canViewList(UUID userId) {
        return repository.findGlobalCapabilities(userId)
                .map(GlobalCapabilityProjection::getCanViewUserList) // Extract the boolean here
                .orElse(false);
    }

    public List<UserListResponse> getAccessibleUsersSummary(User actor) {
        // If Admin, they see everyone with 'hasMoreDetails' as true
        if (actor.getRole().equals(UserRole.ROLE_ADMIN)) {
            return repository.findAll().stream()
                    .map(u -> new UserListResponse(u.getId(), u.getUsername(), true))
                    .toList();
        }

        // Otherwise, use the optimized view
        List<Object[]> rows = repository.findUserSummaryList(actor.getId());

        return rows.stream().map(row -> new UserListResponse(
                (UUID) row[0],
                (String) row[1],
                (Boolean) row[2]
        )).toList();
    }
    private void syncPermissions(User targetUser, Set<UserPermission> newPermissions) {
        // Remove permissions that are no longer in the new set
        targetUser.getPermissions().removeIf(existing ->
                newPermissions.stream().noneMatch(n ->
                        n.getPermissionName().equals(existing.getPermissionName()) &&
                                n.getTargetId().equals(existing.getTargetId())
                )
        );

        // Update or Add new ones
        for (UserPermission newPerm : newPermissions) {
            Optional<UserPermission> existingOpt = targetUser.getPermissions().stream()
                    .filter(e -> e.getPermissionName().equals(newPerm.getPermissionName()) &&
                            e.getTargetId().equals(newPerm.getTargetId()))
                    .findFirst();


        }
    }

    public boolean canPerform(User actor, UUID targetId, String permName, boolean needsWrite) {
        if (actor.getRole().equals(UserRole.ROLE_ADMIN)) return true;

        return actor.getPermissions().stream()
                .filter(p -> p.getPermissionName().equals(permName) &&
                        p.getTargetId().equals(targetId))
                .anyMatch(p -> !needsWrite);
    }
    public User secureUpdate(User actor, UUID targetId, Map<String, Object> updates) {
        User target = repository.dbFindById(targetId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        updates.forEach((field, newValue) -> {
            // Check: Does the actor have a permission record for this field on this target with writable = true?
            boolean canModify = canPerform(actor, targetId, field, true);

            if (canModify || actor.getRole().toString().equals("ROLE_ADMIN")) {
                applyUpdate(target, field, newValue);
            } else {
                System.out.println("Access Denied for field: " + field);
            }
        });

        return repository.dbUpdate(target);
    }

    public void updateUserTargetRelationships(UUID id, List<UUID> targetIds){

    }
    public boolean hasPermission(User user, String action, UUID targetId) {
        return user.getPermissions().stream()
                .anyMatch(p -> p.getPermissionName().equals(action) &&
                        (p.getTargetId() == null || p.getTargetId().equals(targetId)));
    }

    private void applyUpdate(User target, String field, Object value) {
        switch (field) {
            case "email" -> target.setEmail((String) value);
            case "username" -> target.setUsername((String) value);
            case "role" -> target.setRole(UserRole.valueOf((String) value));
        }
    }

}