package org.example.reservation_api.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "api_log") // Best practice to name the table explicitly
public class APILog extends BaseEntity  {



    @Column(nullable = false)
    private LocalDateTime createdAt;

    // I noticed 'name' in your snippet—if this is the Log Name/ID,
    // you can set it in the constructor too!
    private String name;

    private String userId;
    private String action;
    private String status;
    private long durationMs;

    // The Main Constructor for your APILogger
    public APILog(String userId, String action, String status, long durationMs) {
        this.createdAt = LocalDateTime.now(); // Don't forget to initialize this!
        this.userId = userId;
        this.action = action;
        this.status = status;
        this.durationMs = durationMs;
        this.name = "LOG-";
    }

    // Required by Hibernate
    protected APILog() {}

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }



    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public String getAction() {
        return action;
    }

    public String getStatus() {
        return status;
    }

    public long getDurationMs() {
        return durationMs;
    }

}
