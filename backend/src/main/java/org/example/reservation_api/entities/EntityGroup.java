package org.example.reservation_api.entities;

import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.UUID;


@RequiredArgsConstructor
public class EntityGroup extends BaseEntity {
    private final Set<String> permissions;
    private final Set<UUID> manageableUserIds;

    public boolean canPerform(String requiredPermission, Set<UUID> targetUsers) {
        return permissions.contains(requiredPermission) &&
                manageableUserIds.containsAll(targetUsers);
    }
}
