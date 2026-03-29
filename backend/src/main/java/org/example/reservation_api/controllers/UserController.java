package org.example.reservation_api.controllers;


import jakarta.validation.Valid;
import org.example.reservation_api.DTO.RegistrationRequest;
import org.example.reservation_api.entities.User;
import org.example.reservation_api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController<User, UserService> {

    @Autowired
    public UserController(UserService service) {
        super(service);
    }

}
