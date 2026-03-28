package org.example.reservation_api.services;

import io.jsonwebtoken.*;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.reservation_api.entities.Token;
import org.example.reservation_api.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

import static javax.crypto.Cipher.SECRET_KEY;

@Service
public class JwtService {

    // Pull this from your .env file for safety!
    @Value("${JWT_SECRET}")
    private String secretKey;

    public String generateTimedToken(User user, int time) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60 * time)) // 24 hours
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Token isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) getSignInKey())
                    .build()
                    .parseSignedClaims(token);



            return true; // Everything passed!

        } catch (SignatureException e) {
            // Someone tried to fake the token!
            System.out.println("LOG: Fraudulent token attempt detected.");
        } catch (ExpiredJwtException e) {
            // Token is just old.
            System.out.println("LOG: User session expired.");
        } catch (MalformedJwtException e) {
            // Not even a real JWT.
            System.out.println("LOG: Malformed token received.");
        } catch (Exception e) {
            System.out.println("LOG: Token rejected for unknown reason.");
        }
        return false;
    }

    // Helper methods for parsing...
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode("${JWT_SECRET}");
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
