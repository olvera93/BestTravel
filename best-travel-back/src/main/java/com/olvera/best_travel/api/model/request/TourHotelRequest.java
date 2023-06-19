package com.olvera.best_travel.api.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TourHotelRequest implements Serializable {

    @Positive
    @NotNull(message = "Id hotel is mandatory")
    public Long hotelId;

    @Positive
    @NotNull(message = "Total days is mandatory")
    public Integer totalDays;
}
