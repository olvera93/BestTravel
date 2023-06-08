package com.olvera.best_travel.api.controllers;

import com.olvera.best_travel.api.model.request.TourRequest;
import com.olvera.best_travel.api.model.responses.TourResponse;
import com.olvera.best_travel.infraestructure.abstract_service.ITourService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<TourResponse> deleteTour(
            @PathVariable Long tourId
    ) {
        this.tourService.delete(tourId);
        return ResponseEntity.noContent().build();
    }


}
