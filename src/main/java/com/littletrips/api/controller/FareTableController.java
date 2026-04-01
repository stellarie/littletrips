package com.littletrips.api.controller;

import com.littletrips.api.controller.exception.ExceptionMessage;
import com.littletrips.api.model.dto.FareTable;
import com.littletrips.api.service.FareService;
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
