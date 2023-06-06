package com.olvera.best_travel.api.controllers;

import com.olvera.best_travel.api.model.request.TicketRequest;
import com.olvera.best_travel.api.model.responses.TicketResponse;
import com.olvera.best_travel.infraestructure.abstract_service.ITicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "ticket")
@AllArgsConstructor
public class TicketController {

    private final ITicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponse> post(@RequestBody TicketRequest request) {
        return ResponseEntity.ok(ticketService.create(request));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable UUID id ) {
        return ResponseEntity.ok(this.ticketService.read(id));
    }

}
