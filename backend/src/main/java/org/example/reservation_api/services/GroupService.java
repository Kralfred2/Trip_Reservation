package org.example.reservation_api.services;

import lombok.RequiredArgsConstructor;
import org.example.reservation_api.DTO.RoleAssignmentRequest;
import org.example.reservation_api.entities.NestedUserGroup;
import org.example.reservation_api.entities.User;
import org.example.reservation_api.repositories.GroupRepository;
import org.example.reservation_api.repositories.PermissionRepository;
import org.example.reservation_api.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    public void updateUserRoles(UUID actorId, UUID groupId, RoleAssignmentRequest request) {
        User actor = userRepository.dbFindById(actorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // 2. Now you can use the object directly
        if (actor.isAdmin()) {
            groupRepository.batchUpdateGroupRoles(groupId, request.userIds(), request.roleId());
            return;
        }

        // Otherwise, continue with dynamic permission check
        NestedUserGroup context = groupRepository.findNestedGroupById(groupId, actorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // ... rest of your validation logic
    }
    public void performGroupAdminAction(UUID actorId, UUID groupId) {
        // Check if the user is a 'Group Admin' for this specific nested group
        boolean isGroupAdmin = permissionRepository.hasRoleInGroup(actorId, groupId, "Group Admin");

        if (!isGroupAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not an admin of this group");
        }

        // Proceed with logic...
    }
}
