package org.example.reservation_api.repositories;

import jakarta.transaction.Transactional;
import org.example.reservation_api.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseRepository<User> {

    @Transactional
    @Procedure(procedureName = "sp_register_user")
    void sp_register_user(
            @Param("p_username") String p_username,
            @Param("p_email") String p_email,
            @Param("p_password") String p_password
    );

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

