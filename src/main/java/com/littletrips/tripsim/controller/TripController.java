package com.littletrips.tripsim.controller;

import com.littletrips.tripsim.model.dto.Trip;
import com.littletrips.tripsim.service.TripService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {
    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping("/{pan}/history")
    public List<Trip> getTripHistoryByPAN(@PathVariable String pan) {
        return tripService.getTripHistoryByPAN(pan);
    }
}
