package org.example.reservation_api.services;


import org.example.reservation_api.entities.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// T must be a BaseEntity, and ID must be a UUID
public abstract class BaseService<T extends BaseEntity, R extends JpaRepository<T, UUID>> {

    protected final R repository;

    protected BaseService(R repository) {
        this.repository = repository;
    }

    public List<T> findAll() {
        return repository.findAll();
    }
}
