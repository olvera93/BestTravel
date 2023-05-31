package com.olvera.best_travel.domain.entities;

import com.olvera.best_travel.util.AeroLine;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity(name = "fly")
public class FlyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long flyId;

    private Double originLat;

    private Double originLng;

    private Double destinyLat;

    private Double destinyLng;

    @Column(length = 20)
    private String originName;

    @Column(length = 20)
    private String destinyName;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private AeroLine aeroLine;

}
