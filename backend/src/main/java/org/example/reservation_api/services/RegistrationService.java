package org.example.reservation_api.services;

import org.example.reservation_api.DTO.RegistrationRequest;
import org.example.reservation_api.entities.User;
import org.example.reservation_api.entities.UserRole;
import org.example.reservation_api.repositories.UserRepository;
import org.example.reservation_api.entities.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public String tryRegister(RegistrationRequest request) {
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());

        // 1. Encode the password so the Bouncer can read it later
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        // 2. Set the role from the request
        // Ensure you handle the String to Enum conversion safely
        newUser.setRole(UserRole.valueOf(request.getRole().toUpperCase()));

        // 3. Add admin permissions if the role is ADMIN
        Set<String> perms = new HashSet<>();
        perms.add("read_own_profile");
        if ("ADMIN".equals(request.getRole())) {
            perms.add("view_users");
            perms.add("can_modify_users");
        }
        newUser.setPermissions(perms);

        userRepository.save(newUser);
        return "User registered successfully as " + request.getRole();
    }

}