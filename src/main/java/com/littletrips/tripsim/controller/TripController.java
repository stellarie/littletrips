package com.littletrips.tripsim.controller;

import com.littletrips.tripsim.model.dto.Trip;
import com.littletrips.tripsim.model.dto.TripHistory;
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
    public TripHistory getTripHistoryByPAN(@PathVariable String pan, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        return tripService.getTripHistoryByPAN(pan, page, size);
    }
}
