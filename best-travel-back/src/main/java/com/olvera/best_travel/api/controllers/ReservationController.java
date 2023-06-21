package com.olvera.best_travel.api.controllers;

import com.olvera.best_travel.api.model.request.ReservationRequest;
import com.olvera.best_travel.api.model.responses.ErrorResponse;
import com.olvera.best_travel.api.model.responses.ReservationResponse;
import com.olvera.best_travel.infraestructure.abstract_service.IReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping(path = "reservation")
@AllArgsConstructor
@Tag(name = "Reservation")
public class ReservationController {

    private final IReservationService reservationService;

    @ApiResponse(
            responseCode = "400",
            description = "When the request have a field invalid we response this",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }
    )
    @Operation(summary = "Save in system a reservation with the fly passed in parameter")
    @PostMapping
    public ResponseEntity<ReservationResponse> postReservation(@Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.ok(reservationService.create(request));
    }

    @Operation(summary = "Return a reservation with of passed")
    @GetMapping(path = "{id}")
    public ResponseEntity<ReservationResponse> getTicket(@PathVariable UUID id) {
        return ResponseEntity.ok(this.reservationService.read(id));
    }

    @Operation(summary = "Update reservation")
    @PutMapping(path = "{id}")
    public ResponseEntity<ReservationResponse> putTicket(@Valid @PathVariable UUID id, @RequestBody ReservationRequest request) {
        return ResponseEntity.ok(this.reservationService.update(request, id));
    }

    @Operation(summary = "Delete a reservation with of passed")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable UUID id) {
        this.reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "return a reservation price given a hotel id")
    @GetMapping()
    public ResponseEntity<Map<String, BigDecimal>> getHotelPrice(
            @RequestParam Long hotelId,
            @RequestHeader(required = false) Currency currency) {
        if (Objects.isNull(currency)) currency = Currency.getInstance("USD");
        return ResponseEntity.ok(Collections.singletonMap("ticketPrice", this.reservationService.findPrice(hotelId, currency )));
    }
}
