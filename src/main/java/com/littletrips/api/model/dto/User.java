package com.littletrips.api.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record User(
    @JsonProperty("PAN")
    String pan,
    @JsonProperty("Name")
    String name
) {}
