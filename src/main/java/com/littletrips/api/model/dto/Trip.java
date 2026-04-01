package com.littletrips.api.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.littletrips.api.enums.Status;

import java.time.LocalDateTime;

public record Trip(
        @JsonProperty("Started")
        LocalDateTime startTime,
        @JsonProperty("Finished")
        LocalDateTime endTime,
        @JsonProperty("DurationSecs")
        Long durationSecs,
        @JsonProperty("FromStopId")
        String fromStopId,
        @JsonProperty("ToStopId")
        String toStopId,
        @JsonProperty("ChargeAmount")
        double chargeAmount,
        @JsonProperty("CompanyId")
        String companyId,
        @JsonProperty("BusID")
        String busId,
        @JsonProperty("PAN")
        String pan,
        @JsonProperty("Status")
        Status status
) {}