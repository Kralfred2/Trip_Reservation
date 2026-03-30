package org.example.reservation_api.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "api_log")
public class APILog extends BaseEntity {

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT") // Allows for very long log names
    private String name;

    @Column(columnDefinition = "TEXT") // Prevents "value too long" for long user identifiers
    private String userId;

    @Column(columnDefinition = "TEXT") // Essential for full URLs or detailed error messages
    private String action;

    @Column(columnDefinition = "TEXT")
    private String status;

    private long durationMs;

    // The Main Constructor
    public APILog(String userId, String action, String status, long durationMs) {
        this.createdAt = LocalDateTime.now();
        this.userId = userId;
        this.action = action;
        this.status = status;
        this.durationMs = durationMs;
        this.name = "LOG-" + java.util.UUID.randomUUID().toString().substring(0, 8);
    }

    // Required by Hibernate
    protected APILog() {}

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    // Getters
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getName() { return name; }
    public String getUserId() { return userId; }
    public String getAction() { return action; }
    public String getStatus() { return status; }
    public long getDurationMs() { return durationMs; }
}