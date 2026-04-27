package org.example.reservation_api.repositories;

import jakarta.transaction.Transactional;
import org.example.reservation_api.entities.User;
import org.example.reservation_api.projections.GlobalCapabilityProjection;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    @Query(value = """
    SELECT target_id, target_username, has_more_details 
    FROM v_user_access_summary 
    WHERE actor_id = :actorId
""", nativeQuery = true)
    List<Object[]> findUserSummaryList(@Param("actorId") UUID actorId);

    @Query(value = "SELECT * FROM v_user_access_matrix WHERE actor_id = :actorId", nativeQuery = true)
    List<Object[]> findAccessMatrix(@Param("actorId") UUID actorId);

    @Query(value = """
        SELECT can_view_user_list AS canViewUserList 
        FROM v_user_global_capabilities 
        WHERE user_id = :userId
    """, nativeQuery = true)
    Optional<GlobalCapabilityProjection> findGlobalCapabilities(@Param("userId") UUID userId);

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

