package com.olvera.best_travel.infraestructure.abstract_service;

public interface SimpleCrudService <RQ, RS, ID>{

    RS create(RQ request);

    RS read(ID id);

    void delete(ID id);

}
