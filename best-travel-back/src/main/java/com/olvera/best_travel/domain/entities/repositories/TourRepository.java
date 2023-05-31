package com.olvera.best_travel.domain.entities.repositories;

import com.olvera.best_travel.domain.entities.TourEntity;
import org.springframework.data.repository.CrudRepository;

public interface TourRepository extends CrudRepository<TourEntity, Long> {
}
