package org.example.reservation_api.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;


@Getter
@Setter
public class UserRole {
    private String name;
    private Set<UUID> rolePermissions;
}
