package com.littletrips.tripsim.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Destination(
    @JsonProperty("StopID")
    String stopId,
    @JsonProperty("Cost")
    double cost
) {}
