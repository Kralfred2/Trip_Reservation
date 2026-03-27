package org.example.reservation_api.repositories;

import org.example.reservation_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserWriterRepo extends JpaRepository<User, UUID> {
    @Procedure(procedureName = "sp_register_user")
    UUID registerUser(@Param("p_username") String u, @Param("p_password") String p, @Param("p_email") String e);

    @Procedure(procedureName = "sp_global_logout")
    void logoutAllDevices(@Param("p_user_id") UUID userId);
}