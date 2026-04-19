package org.example.reservation_api.services;

import org.example.reservation_api.entities.BaseEntity;
import org.example.reservation_api.repositories.BaseRepository;
import java.util.List;
import java.util.UUID;

public abstract class BaseService<T extends BaseEntity, R extends BaseRepository<T>> {

    protected final R repository;

    protected BaseService(R repository) {
        this.repository = repository;
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    /**
     * The business logic update method.
     */
    public T update(UUID id, T updatedData) {
        // Using your custom 'dbFindById'
        return repository.dbFindById(id)
                .map(existingEntity -> {
                    updatedData.setId(id);

                    // Using your custom 'dbUpdate'
                    return repository.dbUpdate(updatedData);
                })
                .orElseThrow(() -> new RuntimeException("Resource not found with ID: " + id));
    }
}
