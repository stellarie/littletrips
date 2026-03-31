package com.littletrips.tripsim.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TripHistory(
    @JsonProperty("Trips")
    List<Trip> trips,
    @JsonProperty("Total")
    int total,
    @JsonProperty("Page")
    int page,
    @JsonProperty("PageSize")
    int pageSize,
    @JsonProperty("MonthSpend")
    double monthSpend,
    @JsonProperty("LifetimeSpend")
    double lifetimeSpend,
    @JsonProperty("MonthTripCount")
    int monthTripCount
) {}
