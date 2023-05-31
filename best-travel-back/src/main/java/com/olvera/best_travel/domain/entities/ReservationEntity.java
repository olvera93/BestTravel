package com.olvera.best_travel.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity(name = "reservation")
public class ReservationEntity implements Serializable {

    @Id
    @Column(name = "id")
    private UUID reservationId;

    @Column(name = "date_reservation")
    private LocalDateTime dateTimeReservation;

    private LocalDate dateStart;

    private LocalDate dateEnd;

    private Integer totalDays;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotel;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = true)
    private TourEntity tour;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;
}
