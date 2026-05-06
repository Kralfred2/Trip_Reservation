package org.example.reservation_api.entities;

import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class NestedUserGroup extends BaseEntity{
    private String name;
    private String description;
    private UUID parentGroupId;
    private UUID createdBy;

    // Use Set for O(1) lookups in your EntityGroup.canPerform() logic
    private Set<UUID> memberUserIds;
    private Set<UUID> assignedRoleIds;
    private Set<UUID> permissions; // These are the 'Available Actions' for the Admin
}
