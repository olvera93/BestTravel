package com.olvera.best_travel.domain.entities.repositories;

import com.olvera.best_travel.domain.entities.ReservationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ReservationRepository extends CrudRepository<ReservationEntity, UUID> {
}
