package org.example.reservation_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.reservation_api.entities.Token;
import org.example.reservation_api.repositories.TokenRepository;
import org.example.reservation_api.services.JwtService;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // 1. Get the Authorization Header
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extract the string
        final String jwt = authHeader.substring(7);

        // 3. Technical & Expiry Check via your JwtService
        Token tokenData = jwtService.isTokenValid(jwt);

        if (tokenData.getMessage().equals("CORRECT")) {

            // 4. DATABASE CHECK: Is this specific Token ID revoked or missing?
            // Since we stored the ID in the jti claim, we check the DB
            boolean isBlacklisted = !tokenRepository.existsById(tokenData.getId());

            if (!isBlacklisted) {
                // 5. Tell Spring Security the user is authenticated
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        tokenData.getOwnerId(), // The Principal
                        null,
                        new ArrayList<>() // Add roles/authorities here if you have them
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Move to the next filter/controller
        filterChain.doFilter(request, response);
    }
}
