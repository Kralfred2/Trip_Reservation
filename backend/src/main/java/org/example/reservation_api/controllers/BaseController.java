package org.example.reservation_api.controllers;

import org.example.reservation_api.entities.BaseEntity;
import org.example.reservation_api.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

public abstract class BaseController<T extends BaseEntity, S extends BaseService<T, ?>> {

    protected final S service;

    protected BaseController(S service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<T>> getAll() {
        // This will now return List<Trip>, List<User>, etc., automatically
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/ids")
    public List<UUID> getAllIds() {
        return service.findAll().stream()
                .map(BaseEntity::getId) // This works because of 'extends BaseEntity'
                .toList();
    }
}