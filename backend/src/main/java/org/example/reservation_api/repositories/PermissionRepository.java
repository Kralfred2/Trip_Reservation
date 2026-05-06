package org.example.reservation_api.repositories;

import lombok.RequiredArgsConstructor;
import org.example.reservation_api.entities.UserPermission;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PermissionRepository {
    private final JdbcTemplate jdbcTemplate;

    public UUID getPermissionIdByName(String name) {
        String sql = "SELECT id FROM user_permissions WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, UUID.class, name);
    }

    /**
     * This was missing. It uses SQL to insert the permission relationship.
     */
    public void insertPermission(UUID userId, String permissionName, UUID targetId) {
        String sql = "INSERT INTO user_target_permissions (user_id, permission_name, target_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userId, permissionName, targetId);
    }

    /**
     * Replaces the non-functional @Query with actual JDBC logic.
     */
    public boolean hasRoleInGroup(UUID userId, UUID groupId, String roleName) {
        String sql = """
            SELECT EXISTS (
                SELECT 1 FROM group_members gm
                JOIN group_roles gr ON gm.group_role_id = gr.id
                WHERE gm.user_id = ? 
                AND gm.group_id = ? 
                AND gr.name = ?
            )
        """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, userId, groupId, roleName));
    }

    public List<UserPermission> findByUserId(UUID userId) {
        String sql = "SELECT id, user_id, permission_name, target_id FROM user_target_permissions WHERE user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new UserPermission(
                (UUID) rs.getObject("id"),
                (UUID) rs.getObject("user_id"),
                rs.getString("permission_name"),
                (UUID) rs.getObject("target_id")
        ), userId);
    }

    public void clearUserPermissions(UUID userId) {
        jdbcTemplate.update("DELETE FROM user_target_permissions WHERE user_id = ?", userId);
    }
}