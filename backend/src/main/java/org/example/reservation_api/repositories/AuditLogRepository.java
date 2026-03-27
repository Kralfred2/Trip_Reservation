package org.example.reservation_api.repositories;

import org.example.reservation_api.entities.APILog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<APILog, UUID> {

}
