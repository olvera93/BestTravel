package com.olvera.best_travel.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity(name = "tour")
public class TourEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long tourId;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true,
            mappedBy = "tour"
    )
    private Set<ReservationEntity> reservations;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true,
            mappedBy = "tour"
    )
    private Set<TicketEntity> tickets;

    @ManyToOne
    @JoinColumn(name = "id_customer")
    private CustomerEntity customer;

    public void addTicket(TicketEntity ticket) {
        if ((Objects.isNull(this.tickets))) this.tickets = new HashSet<>();
        this.tickets.add(ticket);
    }

    public void removeTicket(UUID id) {
        if ((Objects.isNull(this.tickets))) this.tickets = new HashSet<>();
        this.tickets.removeIf(ticket -> ticket.getTicketId().equals(id));
    }

    public void updateTicket() {
        this.tickets.forEach(ticket -> ticket.setTour(this));
    }

    public void addReservation(ReservationEntity reservation) {
        if (Objects.isNull(this.reservations)) this.reservations = new HashSet<>();
        this.reservations.add(reservation);
    }

    public void removeReservation(UUID reservationId) {
        if (Objects.isNull(this.reservations)) this.reservations = new HashSet<>();
        this.reservations.removeIf(reservation -> reservation.getReservationId().equals(reservationId));
    }

}
