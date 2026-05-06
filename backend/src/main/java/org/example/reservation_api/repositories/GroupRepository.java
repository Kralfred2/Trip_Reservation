package org.example.reservation_api.repositories;

import org.example.reservation_api.entities.NestedUserGroup;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.*;

@Repository
public class GroupRepository {

    private final JdbcTemplate jdbcTemplate;

    public GroupRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Fetches a complete snapshot of a nested group including its
     * manageable entities and the actor's permissions
     */
    public Optional<NestedUserGroup> findNestedGroupById(UUID groupId, UUID actorId) {
        String sql = """
            SELECT 
                ug.id, ug.name, ug.description, ug.parent_id, ug.created_by,
                -- Aggregate members into a UUID array
                (SELECT ARRAY_AGG(user_id) FROM group_members WHERE group_id = ug.id) as members,
                -- Aggregate group-specific roles into a UUID array
                (SELECT ARRAY_AGG(id) FROM group_roles WHERE group_id = ug.id) as roles,
                -- Aggregate admin-level permissions for the actor
                (SELECT ARRAY_AGG(permission_id) FROM group_management_permissions 
                 WHERE group_id = ug.id AND user_id = ?) as admin_perms
            FROM user_groups ug
            WHERE ug.id = ?
        """;

        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                NestedUserGroup group = new NestedUserGroup();
                group.setId(rs.getObject("id", UUID.class));
                group.setName(rs.getString("name"));
                group.setDescription(rs.getString("description"));
                group.setParentGroupId(rs.getObject("parent_id", UUID.class));
                group.setCreatedBy(rs.getObject("created_by", UUID.class));

                // Map SQL Arrays to Java Sets
                group.setMemberUserIds(mapSqlArrayToSet(rs.getArray("members")));
                group.setAssignedRoleIds(mapSqlArrayToSet(rs.getArray("roles")));
                group.setPermissions(mapSqlArrayToSet(rs.getArray("admin_perms")));

                return Optional.of(group);
            }
            return Optional.empty();
        }, actorId, groupId);
    }

    /**
     * Helper to convert PostgreSQL UUID arrays to Java Sets
     */
    private Set<UUID> mapSqlArrayToSet(java.sql.Array sqlArray) throws SQLException {
        if (sqlArray == null) return new HashSet<>();
        UUID[] array = (UUID[]) sqlArray.getArray();
        return new HashSet<>(Arrays.asList(array));
    }

    /**
     * Calls a stored procedure to perform batch role updates
     */
    public void batchUpdateGroupRoles(UUID groupId, Set<UUID> userIds, UUID roleId) {
        String sql = "CALL p_update_group_members_role(?, ?, ?)";
        try {
            java.sql.Array userArray = jdbcTemplate.getDataSource().getConnection()
                    .createArrayOf("UUID", userIds.toArray());

            jdbcTemplate.update(sql, groupId, userArray, roleId);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update group roles", e);
        }
    }
}