package org.example.reservation_api.repositories;


import org.example.reservation_api.entities.Token;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends BaseRepository<Token> {

    // 1. Find all valid tokens for a user (useful for the "Logout All" logic)
    @Query("""
        SELECT t FROM Token t 
        INNER JOIN User u ON t.user.id = u.id 
        WHERE u.id = :userId AND (t.expired = false OR t.revoked = false)
    """)
    List<Token> findAllValidTokensByUser(UUID userId);

    // 2. Find a specific token string to check if it's been revoked
    Optional<Token> findByToken(String token);
}


