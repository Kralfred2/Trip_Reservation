package org.example.reservation_api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Set;
import java.util.UUID;

public class GroupRole extends BaseEntity{

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "permission")
    private Set<String> permissions;

    @Column(name = "clearance")
    private Set<Integer> clearance;

}
