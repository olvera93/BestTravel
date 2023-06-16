package com.olvera.best_travel.api.model.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReservationRequest implements Serializable {

    @Size(min = 18, max = 20, message = "The size have to a length between 10 and 20 characters")
    @NotBlank(message = "Id client is mandatory")
    private String clientId;

    @Positive
    @NotNull(message = "Id hotel is mandatory")
    private Long hotelId;

    @Min(value = 1, message = "Min one day to make a reservation")
    @Max(value = 30, message = "Max to make a reservation")
    @NotNull(message = "Total days is mandatory")
    private Integer totalDays;

    @Email(message = "Invalid email")
    private String email;
}
