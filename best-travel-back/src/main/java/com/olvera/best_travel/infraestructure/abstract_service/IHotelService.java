package com.olvera.best_travel.infraestructure.abstract_service;

import com.olvera.best_travel.api.model.responses.HotelResponse;

import java.util.Set;

public interface IHotelService extends CatalogService<HotelResponse> {

    Set<HotelResponse> readByRating(Integer rating);

}
