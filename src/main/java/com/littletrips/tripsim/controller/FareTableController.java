package com.littletrips.tripsim.controller;

import com.littletrips.tripsim.controller.exception.ExceptionMessage;
import com.littletrips.tripsim.model.dto.FareTable;
import com.littletrips.tripsim.service.FareService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fare")
public class FareTableController {
    private final FareService fareService;

    public FareTableController(FareService fareService) {
        this.fareService = fareService;
    }

    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FareTable> getFareTable() {
        return fareService.getFareTable();
    }

    @ExceptionHandler(exception = Exception.class, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleException() {
        return ResponseEntity
                .internalServerError()
                .body(ExceptionMessage.getGenericExceptionMessage());
    }
}
