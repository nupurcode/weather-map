package com.demo.weatherMap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class ErrorResponse {
    @JsonProperty
    String error;
    @JsonProperty
    String message;
    @JsonProperty
    String detail;
}
