package com.olvera.best_travel.infraestructure.service;

import com.olvera.best_travel.api.model.request.TicketRequest;
import com.olvera.best_travel.api.model.responses.FlyResponse;
import com.olvera.best_travel.api.model.responses.TicketResponse;
import com.olvera.best_travel.domain.entities.TicketEntity;
import com.olvera.best_travel.domain.entities.repositories.CustomerRepository;
import com.olvera.best_travel.domain.entities.repositories.FlyRepository;
import com.olvera.best_travel.domain.entities.repositories.TicketRepository;
import com.olvera.best_travel.infraestructure.abstract_service.ITicketService;
import com.olvera.best_travel.util.BestTravelUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class TicketService implements ITicketService {

    private final FlyRepository flyRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;

    @Override
    public TicketResponse create(TicketRequest request) {
        var fly = flyRepository.findById(request.getIdFly()).orElseThrow();
        var customer = customerRepository.findById(request.getIdClient()).orElseThrow();
        var ticketToPersist = TicketEntity.builder()
                .ticketId(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().add(fly.getPrice().multiply(changes_price_percentage)))
                .purchaseDate(LocalDate.now())
                .arrivalDate(BestTravelUtil.getRandomLater())
                .departureDate(BestTravelUtil.getRandomSoon())
                .build();

        var ticketPersisted = this.ticketRepository.save(ticketToPersist);
        log.info("Ticket saved with id: {}", ticketPersisted.getTicketId());
        return this.entityToResponse(ticketToPersist);
    }

    @Override
    public TicketResponse read(UUID id) {
        var ticketFromDb = this.ticketRepository.findById(id).orElseThrow();
        return this.entityToResponse(ticketFromDb);
    }

    @Override
    public TicketResponse update(TicketRequest request, UUID id) {
        var ticketToUpdate = ticketRepository.findById(id).orElseThrow();
        var fly = flyRepository.findById(request.getIdFly()).orElseThrow();

        ticketToUpdate.setFly(fly);
        ticketToUpdate.setPrice(fly.getPrice().add(fly.getPrice().multiply(changes_price_percentage)));
        ticketToUpdate.setArrivalDate(BestTravelUtil.getRandomLater());
        ticketToUpdate.setDepartureDate(BestTravelUtil.getRandomSoon());

        var ticket = this.ticketRepository.save(ticketToUpdate);
        log.info("Ticket updated with id {}", ticketToUpdate.getTicketId());

        return this.entityToResponse(ticket);
    }

    @Override
    public void delete(UUID id) {
        var ticketToDelete = ticketRepository.findById(id).orElseThrow();
        this.ticketRepository.delete(ticketToDelete);

    }

    @Override
    public BigDecimal findPrice(Long flyId) {
        var fly = this.flyRepository.findById(flyId).orElseThrow();
        return fly.getPrice().add(fly.getPrice().multiply(changes_price_percentage));
    }

    private TicketResponse entityToResponse(TicketEntity entity) {
        var response = new TicketResponse();
        BeanUtils.copyProperties(entity, response);
        var flyResponse = new FlyResponse();

        BeanUtils.copyProperties(entity.getFly(), flyResponse);
        response.setFly(flyResponse);

        return response;

    }

    public static final BigDecimal changes_price_percentage = BigDecimal.valueOf(0.25);
}
