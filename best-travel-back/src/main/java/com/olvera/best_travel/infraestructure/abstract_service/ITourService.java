package com.olvera.best_travel.infraestructure.abstract_service;

import com.olvera.best_travel.api.model.request.TourRequest;
import com.olvera.best_travel.api.model.responses.TourResponse;

import java.util.UUID;

public interface ITourService extends SimpleCrudService<TourRequest, TourResponse, Long> {

    void removeTicket(Long tourId, UUID ticketId);

    UUID addTicket(Long tourId, Long flyId);

    void removeReservation(Long tourId, UUID reservationId);

    UUID addReservation(Long tourId, Long hotelId, Integer totalDay);


}
