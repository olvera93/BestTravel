package com.olvera.best_travel.api.controllers;

import com.olvera.best_travel.api.model.request.TourRequest;
import com.olvera.best_travel.api.model.responses.TourResponse;
import com.olvera.best_travel.infraestructure.abstract_service.ITourService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "tour")
@AllArgsConstructor
public class TourController {

    private final ITourService tourService;

    @PostMapping
    public ResponseEntity<TourResponse> postTour(
            @RequestBody TourRequest request
    ) {
        return ResponseEntity.ok(this.tourService.create(request));
    }

    @GetMapping(path = "{tourId}")
    public ResponseEntity<TourResponse> getTour(
            @PathVariable Long tourId
    ) {
        return ResponseEntity.ok(this.tourService.read(tourId));
    }

    @DeleteMapping(path = "{tourId}")
    public ResponseEntity<Void> deleteTour(
            @PathVariable Long tourId
    ) {
        this.tourService.delete(tourId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "{tourId}/remove_ticket/{ticketId}")
    public ResponseEntity<Void> deleteTicket(
            @PathVariable Long tourId,
            @PathVariable UUID ticketId
    ) {
        this.tourService.removeTicket(tourId, ticketId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "{tourId}/add_ticket/{flyId}")
    public ResponseEntity<Map<String, UUID>> postTicket(
            @PathVariable Long tourId,
            @PathVariable Long flyId
    ) {
        var response = Collections.singletonMap("ticketId", this.tourService.addTicket(tourId, flyId));
        return ResponseEntity.ok(response);
    }

    @PatchMapping(path = "{tourId}/remove_reservation/{reservationId}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable Long tourId,
            @PathVariable UUID reservationId
    ) {
        this.tourService.removeReservation(tourId, reservationId);
        return ResponseEntity.noContent().build();
    }

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
