package com.olvera.best_travel.domain.entities.repositories;

import com.olvera.best_travel.domain.entities.FlyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlyRepository extends JpaRepository<FlyEntity, Long> {
}
