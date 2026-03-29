package org.example.reservation_api.repositories;


import org.example.reservation_api.entities.Token;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends BaseRepository<Token> {

    @Query("SELECT t FROM Token t WHERE t.ownerId = :userId")
    List<Token> findAllValidTokensByUser(@Param("userId") UUID userId);

    List<Token> findAllByOwnerId(UUID ownerId);
    // 2. Find a specific token string to check if it's been revoked

}


