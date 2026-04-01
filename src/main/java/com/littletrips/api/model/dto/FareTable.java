package com.littletrips.api.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record FareTable(
    @JsonProperty("StopID")
    String stopId,
    @JsonProperty("Destinations")
    List<Destination> destinations
){}
