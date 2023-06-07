package com.olvera.best_travel.infraestructure.abstract_service;

import com.olvera.best_travel.api.model.responses.FlyResponse;

import java.util.Set;

public interface IFlyService extends CatalogService<FlyResponse> {

    Set<FlyResponse> readByOriginDestiny(String origin, String destiny);
}
