package org.example.reservation_api.repositories;

import org.example.reservation_api.entities.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, UUID> {

    default List<T> dbFindAll() {
        return this.findAll();
    }

    default Optional<T> dbFindById(UUID id) {
        return this.findById(id);
    }

    default T dbUpdate(T entity) {
        return this.save(entity);
    }





}
