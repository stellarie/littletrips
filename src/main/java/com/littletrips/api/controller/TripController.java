package com.littletrips.api.controller;

import com.littletrips.api.controller.exception.ExceptionMessage;
import com.littletrips.api.model.dto.TripHistory;
import com.littletrips.api.service.TripService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trips")
public class TripController {
    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping(path = "/{pan}/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public TripHistory getTripHistoryByPAN(@PathVariable String pan, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        return tripService.getTripHistoryByPAN(pan, page, size);
    }

    @ExceptionHandler(value = Exception.class, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleException() {
        return ResponseEntity
                .internalServerError()
                .body(ExceptionMessage.getGenericExceptionMessage());
    }
}
