package com.olvera.best_travel.infraestructure.service;

import com.olvera.best_travel.api.model.request.ReservationRequest;
import com.olvera.best_travel.api.model.responses.HotelResponse;
import com.olvera.best_travel.api.model.responses.ReservationResponse;
import com.olvera.best_travel.domain.entities.ReservationEntity;
import com.olvera.best_travel.domain.entities.repositories.CustomerRepository;
import com.olvera.best_travel.domain.entities.repositories.HotelRepository;
import com.olvera.best_travel.domain.entities.repositories.ReservationRepository;
import com.olvera.best_travel.infraestructure.abstract_service.IReservationService;
import com.olvera.best_travel.infraestructure.helpers.BlackListHelper;
import com.olvera.best_travel.infraestructure.helpers.CustomerHelper;
import com.olvera.best_travel.util.exceptions.IdNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class ReservationService implements IReservationService {

    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;

    @Override
    public ReservationResponse create(ReservationRequest request) {

        blackListHelper.isInBlackListCustomer(request.getClientId());

        var hotel = this.hotelRepository.findById(request.getHotelId()).orElseThrow(() -> new IdNotFoundException("hotel"));
        var customer = this.customerRepository.findById(request.getClientId()).orElseThrow(() -> new IdNotFoundException("customer"));

        var reservationToPersist = ReservationEntity.builder()
                .reservationId(UUID.randomUUID())
                .hotel(hotel)
                .customer(customer)
                .totalDays(request.getTotalDays())
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(request.getTotalDays()))
                .price(hotel.getPrice().add(hotel.getPrice().multiply(changes_price_percentage)))
                .build();

        var reservationPersisted = reservationRepository.save(reservationToPersist);
        this.customerHelper.increase(customer.getDni(), ReservationService.class);
        return this.entityToResponse(reservationPersisted);
    }

    @Override
    public ReservationResponse read(UUID id) {
        var reservationFromDb = this.reservationRepository.findById(id).orElseThrow(() -> new IdNotFoundException("reservation"));
        return this.entityToResponse(reservationFromDb);
    }

    @Override
    public ReservationResponse update(ReservationRequest request, UUID id) {
        var hotel = this.hotelRepository.findById(request.getHotelId()).orElseThrow(() -> new IdNotFoundException("hotel"));

        var reservationToUpdate = this.reservationRepository.findById(id).orElseThrow(() -> new IdNotFoundException("reservation"));
        reservationToUpdate.setHotel(hotel);
        reservationToUpdate.setDateTimeReservation(LocalDateTime.now());
        reservationToUpdate.setDateStart(LocalDate.now());
        reservationToUpdate.setDateEnd(LocalDate.now().plusDays(request.getTotalDays()));
        reservationToUpdate.setPrice(hotel.getPrice().add(hotel.getPrice().multiply(changes_price_percentage)));
        reservationToUpdate.setTotalDays(request.getTotalDays());

        var reservationUpdated = this.reservationRepository.save(reservationToUpdate);

        log.info("Ticket updated with id {}", reservationUpdated.getReservationId());

        return this.entityToResponse(reservationUpdated);
    }

    @Override
    public void delete(UUID id) {
        var reservationFromDb = this.reservationRepository.findById(id).orElseThrow(() -> new IdNotFoundException("reservation"));
        this.reservationRepository.delete(reservationFromDb);

    }

    private ReservationResponse entityToResponse(ReservationEntity entity) {
        var response = new ReservationResponse();
        BeanUtils.copyProperties(entity, response);
        var hotelResponse = new HotelResponse();

        BeanUtils.copyProperties(entity.getHotel(), hotelResponse);
        response.setHotel(hotelResponse);

        return response;

    }

    @Override
    public BigDecimal findPrice(Long hotelId) {
        var hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new IdNotFoundException("hotel"));
        return hotel.getPrice().add(hotel.getPrice().multiply(changes_price_percentage));
    }

    public static final BigDecimal changes_price_percentage = BigDecimal.valueOf(0.20);


}
