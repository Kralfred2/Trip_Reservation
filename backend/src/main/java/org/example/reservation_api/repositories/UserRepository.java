package org.example.reservation_api.repositories;

import org.example.reservation_api.entities.User;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseRepository<User> {


    @Procedure(name = "sp_register_user") // Use 'name' instead of 'procedureName'
    UUID sp_register_user(String p_username, String p_password, String p_email);

    Optional<User> findByUsername(String username);
    Optional<User> existsByUsername(String username);

}

