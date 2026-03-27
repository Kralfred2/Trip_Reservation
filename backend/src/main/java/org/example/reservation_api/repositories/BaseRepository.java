package org.example.reservation_api.repositories;

import org.example.reservation_api.entities.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean // This tells Spring: "Don't try to create an instance of THIS specific repo"
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, UUID> {
    // You can add custom methods here that apply to ALL entities
    Optional<T> findById(UUID id);
}
