package org.example.reservation_api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;


@Table(name = "user_token")
@Entity
@Getter
@NoArgsConstructor
public class Token extends BaseEntity {

    @Column(nullable = false)
    private UUID ownerId;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private Date expiration;

    public Token(UUID ownerId, Date expiration, String message) {
        this.ownerId = ownerId;
        this.expiration = expiration;
        this.message = message;
    }
}
