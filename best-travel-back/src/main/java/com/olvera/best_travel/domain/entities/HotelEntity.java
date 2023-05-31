package com.olvera.best_travel.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity(name = "hotel")
public class HotelEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long hotelId;

    @Column(length = 50)
    private String name;
    @Column(length = 50)
    private String address;

    private Integer rating;

    private BigDecimal price;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true,
            mappedBy = "hotel"
    )
    private Set<ReservationEntity> reservations;
}
