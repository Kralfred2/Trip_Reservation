package org.example.reservation_api.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "app_user")
public class User extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;




    public UserRole getRole() {
        return this.role;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}
