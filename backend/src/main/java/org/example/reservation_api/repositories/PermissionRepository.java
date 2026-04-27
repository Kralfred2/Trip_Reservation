package org.example.reservation_api.repositories;

import org.example.reservation_api.entities.UserPermission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PermissionRepository extends BaseRepository<UserPermission> {

    // Used for the Login process: Does this user have ANY permission to view users?
    @Query(value = "SELECT EXISTS(SELECT 1 FROM user_permissions WHERE user_id = :userId AND permission_name = 'view_user')", nativeQuery = true)
    boolean hasViewUserCapability(@Param("userId") UUID userId);

    // Used for the Detailed Modal: Get all permissions for a specific target
    List<UserPermission> findByUserIdAndTargetId(UUID userId, UUID targetId);

    // Get everything for a user (for caching/logic checks)
    List<UserPermission> findByUserId(UUID userId);
}