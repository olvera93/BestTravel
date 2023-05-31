package com.olvera.best_travel.domain.entities.repositories;

import com.olvera.best_travel.domain.entities.CustomerEntity;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<CustomerEntity, String> {
}
