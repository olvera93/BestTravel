package com.olvera.best_travel.infraestructure.abstract_service;

import com.olvera.best_travel.api.model.request.ReservationRequest;
import com.olvera.best_travel.api.model.responses.ReservationResponse;

import java.math.BigDecimal;
import java.util.UUID;

public interface IReservationService extends CrudService<ReservationRequest, ReservationResponse, UUID>{

    BigDecimal findPrice(Long hotelId);

}
