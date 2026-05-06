package org.example.reservation_api.entities;

import jakarta.persistence.Column;

import java.util.UUID;

public class UserGroup extends BaseEntity{

    @Column(unique = true, nullable = false)
    private UUID createdBy;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "target_id" )
    private int authLevel;

    @Column(unique = true, nullable = false)
    private GroupRole defaultRole;

}
