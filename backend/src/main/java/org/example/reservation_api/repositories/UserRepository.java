package org.example.reservation_api.repositories;

import org.example.reservation_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Spring is smart enough to turn this method name into a SQL query!
    Optional<User> findByUsername(String username);
}