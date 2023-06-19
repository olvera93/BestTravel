package com.olvera.best_travel.api.controllers;

import com.olvera.best_travel.api.model.request.TourRequest;
import com.olvera.best_travel.api.model.responses.TourResponse;
import com.olvera.best_travel.infraestructure.abstract_service.ITourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "tour")
@AllArgsConstructor
@Tag(name = "Tour")
public class TourController {

    private final ITourService tourService;

    @Operation(summary = "Save in system a tour based in list of hotels and flights")
    @PostMapping
    public ResponseEntity<TourResponse> postTour(
            @RequestBody TourRequest request
    ) {
        return ResponseEntity.ok(this.tourService.create(request));
    }

    @Operation(summary = "Return a Tour with id passed")
    @GetMapping(path = "{tourId}")
    public ResponseEntity<TourResponse> getTour(
            @PathVariable Long tourId
    ) {
        return ResponseEntity.ok(this.tourService.read(tourId));
    }

    @Operation(summary = "Delete a Tour with id passed")
    @DeleteMapping(path = "{tourId}")
    public ResponseEntity<Void> deleteTour(
            @PathVariable Long tourId
    ) {
        this.tourService.delete(tourId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove a ticket from tour")
    @PatchMapping(path = "{tourId}/remove_ticket/{ticketId}")
    public ResponseEntity<Void> deleteTicket(
            @PathVariable Long tourId,
            @PathVariable UUID ticketId
    ) {
        this.tourService.removeTicket(tourId, ticketId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add a ticket from tour")
    @PatchMapping(path = "{tourId}/add_ticket/{flyId}")
    public ResponseEntity<Map<String, UUID>> postTicket(
            @PathVariable Long tourId,
            @PathVariable Long flyId
    ) {
        var response = Collections.singletonMap("ticketId", this.tourService.addTicket(tourId, flyId));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Remove a reservation from tour")
    @PatchMapping(path = "{tourId}/remove_reservation/{reservationId}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable Long tourId,
            @PathVariable UUID reservationId
    ) {
        this.tourService.removeReservation(tourId, reservationId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add a reservation from tour")
    @PatchMapping(path = "{tourId}/add_reservation/{hotelId}")
    public ResponseEntity<Map<String, UUID>> postTicket(
            @PathVariable Long tourId,
            @PathVariable Long hotelId,
            @RequestParam Integer totalDays
    ) {
        var response = Collections.singletonMap("ticketId", this.tourService.addReservation(tourId, hotelId, totalDays));
        return ResponseEntity.ok(response);
    }


}
