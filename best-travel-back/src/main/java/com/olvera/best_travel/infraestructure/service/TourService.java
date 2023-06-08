package com.olvera.best_travel.infraestructure.service;

import com.olvera.best_travel.api.model.request.TourRequest;
import com.olvera.best_travel.api.model.responses.TourResponse;
import com.olvera.best_travel.domain.entities.*;
import com.olvera.best_travel.domain.entities.repositories.*;
import com.olvera.best_travel.infraestructure.abstract_service.ITourService;
import com.olvera.best_travel.infraestructure.helpers.TourHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class TourService implements ITourService {

    private final TourRepository tourRepository;
    private final HotelRepository hotelRepository;
    private final FlyRepository flyRepository;
    private final CustomerRepository customerRepository;
    private final TourHelper tourHelper;

    @Override
    public TourResponse create(TourRequest request) {
        var customer = customerRepository.findById(request.getCustomerId()).orElseThrow();

        var flights = new HashSet<FlyEntity>();
        request.getFlights().forEach(fly -> {
            flights.add(this.flyRepository.findById(fly.getFlyId()).orElseThrow());

        });

        var hotels = new HashMap<HotelEntity, Integer>();
        request.getHotels().forEach(hotel -> {
            hotels.put(this.hotelRepository.findById(hotel.getHotelId()).orElseThrow(), hotel.getTotalDays());
        });

        var tourToSave = TourEntity.builder()
                .tickets(this.tourHelper.createTicket(flights, customer))
                .reservations(this.tourHelper.createReservation(hotels, customer))
                .customer(customer)
                .build();

        var tourSaved = this.tourRepository.save(tourToSave);


        return TourResponse.builder()
                .reservationIds(tourSaved.getReservations().stream().map(ReservationEntity::getReservationId).collect(Collectors.toSet()))
                .ticketIds(tourSaved.getTickets().stream().map(TicketEntity::getTicketId).collect(Collectors.toSet()))
                .tourId(tourSaved.getTourId())
                .build();
    }

    @Override
    public TourResponse read(Long id) {
        var tourFromDb = this.tourRepository.findById(id).orElseThrow();
        return TourResponse.builder()
                .reservationIds(tourFromDb.getReservations().stream().map(ReservationEntity::getReservationId).collect(Collectors.toSet()))
                .ticketIds(tourFromDb.getTickets().stream().map(TicketEntity::getTicketId).collect(Collectors.toSet()))
                .tourId(tourFromDb.getTourId())
                .build();
    }

    @Override
    public void delete(Long id) {
        var tourToDb = this.tourRepository.findById(id)
                .orElseThrow();
        this.tourRepository.delete(tourToDb);
    }

    @Override
    public void removeTicket(UUID ticketId, Long tourId) {

    }

    @Override
    public UUID addTicket(Long flyId, Long tourId) {
        return null;
    }

    @Override
    public void removeReservation(UUID reservationId, Long tourId) {

    }

    @Override
    public UUID addReservation(Long reservationId, Long tourId) {
        return null;
    }

}
