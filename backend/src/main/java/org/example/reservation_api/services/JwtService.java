package org.example.reservation_api.services;

import io.jsonwebtoken.*;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.reservation_api.entities.Token;
import org.example.reservation_api.entities.User;
import org.example.reservation_api.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

import static javax.crypto.Cipher.SECRET_KEY;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final TokenRepository tokenRepository;

    // Pull this from your .env file for safety!
    @Value("${JWT_SECRET}")
    private String secretKey;

    public String generateTimedToken(User user, int timeInMin) {
        // 1. Prepare dates
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 1000L * 60 * timeInMin);

        // 2. Create and Save the Token Entity first
        // This allows @GeneratedValue to create the UUID
        Token tokenEntity = new Token(user.getId(), expiry, "ACTIVE");
        tokenEntity = tokenRepository.save(tokenEntity); // Now tokenEntity.getId() is populated!

        // 3. Build the JWT using the DB-generated ID
        return Jwts.builder()
                .header().add("typ", "JWT").and()
                .id(tokenEntity.getId().toString()) // The DB ID is now the 'jti'
                .subject(user.getUsername())
                .claim("userId", user.getId().toString())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSignInKey())
                .compact();
    }

    public Token isTokenValid(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // Get the ID from the 'jti' claim (this is the UUID from your BaseEntity)
            UUID databaseTokenId = UUID.fromString(claims.getId());
            UUID ownerId = UUID.fromString(claims.get("userId", String.class));

            // Create a temporary object to pass back the data
            Token returnVal = new Token(ownerId, claims.getExpiration(), "CORRECT");
            returnVal.setId(databaseTokenId); // Set the ID so we can check the blacklist later

            return returnVal;

        } catch (ExpiredJwtException e) {
            return new Token(null, null, "Expired");
        } catch (Exception e) {
            return new Token(null, null, "Invalid");
        }
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
