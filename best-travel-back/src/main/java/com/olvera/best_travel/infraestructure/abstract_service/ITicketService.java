package com.olvera.best_travel.infraestructure.abstract_service;

import com.olvera.best_travel.api.model.request.TicketRequest;
import com.olvera.best_travel.api.model.responses.TicketResponse;

import java.math.BigDecimal;
import java.util.UUID;

public interface ITicketService extends CrudService<TicketRequest, TicketResponse, UUID>{

    BigDecimal findPrice(Long flyId);

}
