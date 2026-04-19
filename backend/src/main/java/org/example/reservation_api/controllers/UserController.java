package org.example.reservation_api.controllers;


import jakarta.validation.Valid;
import org.example.reservation_api.DTO.RegistrationRequest;
import org.example.reservation_api.entities.User;
import org.example.reservation_api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController<User, UserService> {

    @Autowired
    public UserController(UserService service) {
        super(service);
    }


    @Override
    @PreAuthorize("hasAuthority('view_users')")
    public ResponseEntity<List<User>> getAll() {
        return super.getAll();
    }

    @PostMapping("/{id}/modify")
    @PreAuthorize("hasAuthority('can_modify_users')")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody User user) {
        return ResponseEntity.ok(service.update(id, user));
    }
}
