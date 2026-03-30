package com.littletrips.tripsim.controller;

import com.littletrips.tripsim.model.dto.FareTable;
import com.littletrips.tripsim.service.FareService;
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

    @GetMapping("/")
    public List<FareTable> getFareTable() {
        return fareService.getFareTable();
    }
}
