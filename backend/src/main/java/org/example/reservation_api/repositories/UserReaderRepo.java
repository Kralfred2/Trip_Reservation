package org.example.reservation_api.repositories;

import org.example.reservation_api.entities.UserViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserReaderRepo extends JpaRepository<UserViewEntity, UUID> {

    Optional<UserViewEntity> findByUsername(String username);
}
