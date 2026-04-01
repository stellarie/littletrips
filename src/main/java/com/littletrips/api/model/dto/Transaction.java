package com.littletrips.api.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.littletrips.api.enums.TapType;

import java.time.LocalDateTime;

public record Transaction(
        @JsonProperty("ID")
        Integer id,
        @JsonProperty("DateTimeUTC")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime dateTimeUTC,
        @JsonProperty("TapType")
        TapType tapType,
        @JsonProperty("StopId")
        String stopId,
        @JsonProperty("CompanyId")
        String companyId,
        @JsonProperty("BusID")
        String busId,
        @JsonProperty("PAN")
        String pan
) {}