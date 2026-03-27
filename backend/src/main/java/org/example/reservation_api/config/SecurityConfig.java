package org.example.reservation_api.config;

import org.example.reservation_api.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Change the type here to the Interface (org.springframework.security.core.userdetails.UserDetailsService)
    private final UserDetailsService guestListManager;

    // Remove PasswordEncoder from the constructor to avoid circular dependency
    public SecurityConfig(UserDetailsService guestListManager) {
        this.guestListManager = guestListManager;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // Pass the guestListManager directly into the constructor here
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(guestListManager);

        // Set the password encoder using the setter
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean(name = "bouncer")
    public AuthenticationManager bouncer(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable for APIs
                .authorizeHttpRequests(auth -> auth
                        // 1. PUBLIC PATHS (No login required)
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()

                        // 2. PROTECTED PATHS (Must be logged in)
                        .requestMatchers("/api/trips/**").authenticated()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 3. THE CATCH-ALL
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

}
