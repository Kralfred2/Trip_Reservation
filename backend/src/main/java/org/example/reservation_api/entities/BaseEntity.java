package org.example.reservation_api.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    @Id // Required by JPA
    @GeneratedValue(strategy = GenerationType.AUTO) // Automatically handles ID generation
    private UUID id;
}
