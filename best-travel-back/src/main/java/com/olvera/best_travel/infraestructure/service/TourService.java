package com.olvera.best_travel.infraestructure.service;

import com.olvera.best_travel.api.model.request.TourRequest;
import com.olvera.best_travel.api.model.responses.TourResponse;
import com.olvera.best_travel.domain.entities.*;
import com.olvera.best_travel.domain.entities.repositories.*;
import com.olvera.best_travel.infraestructure.abstract_service.ITourService;
import com.olvera.best_travel.infraestructure.helpers.BlackListHelper;
import com.olvera.best_travel.infraestructure.helpers.CustomerHelper;
import com.olvera.best_travel.infraestructure.helpers.TourHelper;
import com.olvera.best_travel.util.enums.Tables;
import com.olvera.best_travel.util.exceptions.IdNotFoundException;
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
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;
    @Override
    public TourResponse create(TourRequest request) {
        blackListHelper.isInBlackListCustomer(request.getCustomerId());
        var customer = customerRepository.findById(request.getCustomerId()).orElseThrow(() -> new IdNotFoundException(Tables.customer.name()));

        var flights = new HashSet<FlyEntity>();
        request.getFlights().forEach(fly -> {
            flights.add(this.flyRepository.findById(fly.getTourId()).orElseThrow(() -> new IdNotFoundException(Tables.fly.name())));

        });

        var hotels = new HashMap<HotelEntity, Integer>();
        request.getHotels().forEach(hotel -> {
            hotels.put(this.hotelRepository.findById(hotel.getHotelId()).orElseThrow(() -> new IdNotFoundException(Tables.hotel.name())), hotel.getTotalDays());
        });

        var tourToSave = TourEntity.builder()
                .tickets(this.tourHelper.createTicket(flights, customer))
                .reservations(this.tourHelper.createReservation(hotels, customer))
                .customer(customer)
                .build();

        var tourSaved = this.tourRepository.save(tourToSave);

        this.customerHelper.increase(customer.getDni(), TourService.class);

        return TourResponse.builder()
                .reservationIds(tourSaved.getReservations().stream().map(ReservationEntity::getReservationId).collect(Collectors.toSet()))
                .ticketIds(tourSaved.getTickets().stream().map(TicketEntity::getTicketId).collect(Collectors.toSet()))
                .tourId(tourSaved.getTourId())
                .build();
    }

    @Override
    public TourResponse read(Long id) {
        var tourFromDb = this.tourRepository.findById(id).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        return TourResponse.builder()
                .reservationIds(tourFromDb.getReservations().stream().map(ReservationEntity::getReservationId).collect(Collectors.toSet()))
                .ticketIds(tourFromDb.getTickets().stream().map(TicketEntity::getTicketId).collect(Collectors.toSet()))
                .tourId(tourFromDb.getTourId())
                .build();
    }

    @Override
    public void delete(Long id) {
        var tourToDb = this.tourRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        this.tourRepository.delete(tourToDb);
    }

    @Override
    public void removeTicket(Long tourId, UUID ticketId) {
        var tourUpdate = this.tourRepository.findById(tourId).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        tourUpdate.removeTicket(ticketId);
        this.tourRepository.save(tourUpdate);
    }

    @Override
    public UUID addTicket(Long tourId, Long flyId) {
        var tourUpdate = this.tourRepository.findById(tourId).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        var fly = this.flyRepository.findById(flyId).orElseThrow(() -> new IdNotFoundException(Tables.fly.name()));
        var ticket = this.tourHelper.createTicket(fly, tourUpdate.getCustomer());

        tourUpdate.addTicket(ticket);
        this.tourRepository.save(tourUpdate);

        return ticket.getTicketId();
    }

    @Override
    public void removeReservation( Long tourId, UUID reservationId) {
        var tourUpdate = this.tourRepository.findById(tourId).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        tourUpdate.removeReservation(reservationId);
        this.tourRepository.save(tourUpdate);
    }

    @Override
    public UUID addReservation(Long tourId, Long hotelId, Integer totalDays) {
        var tourUpdate = this.tourRepository.findById(tourId).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        var hotel = this.hotelRepository.findById(hotelId).orElseThrow(() -> new IdNotFoundException(Tables.hotel.name()));
        var reservation = this.tourHelper.createReservation(hotel, tourUpdate.getCustomer(), totalDays);

        tourUpdate.addReservation(reservation);
        this.tourRepository.save(tourUpdate);
        return reservation.getReservationId();
    }

}
