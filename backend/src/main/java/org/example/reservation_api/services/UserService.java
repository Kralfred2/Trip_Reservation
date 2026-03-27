package org.example.reservation_api.services;

import org.example.reservation_api.DTO.RegistrationRequest;
import org.example.reservation_api.entities.User;
import org.example.reservation_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service // Essential! This makes it a Bean.
public class UserService extends BaseService<User, UserRepository> {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.passwordEncoder = passwordEncoder;
    }

    public UUID registerNewUser(RegistrationRequest request) {
        // 1. Logic: Check if username exists using the repository inherited from BaseService
        if (repository.existsByUsername(request.username()).isPresent()) { // Note: request.username() not getUsername()
            throw new RuntimeException("Username already taken!");
        }

        // 2. Mapping: Convert Record data to Entity
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());

        // 3. Security: Hash the password
        user.setPassword(passwordEncoder.encode(request.password()));


        // 4. Persistence: Save and return the ID
        return repository.save(user).getId();
    }
}