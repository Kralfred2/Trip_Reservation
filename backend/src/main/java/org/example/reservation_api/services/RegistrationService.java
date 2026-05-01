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

        newUser.setPassword(passwordEncoder.encode(request.getPassword()));


        Set<String> perms = new HashSet<>();
        perms.add("read_own_profile");

        newUser.setRole(UserRole.ROLE_USER);


        userRepository.save(newUser);
        return "User registered successfully";
    }

}