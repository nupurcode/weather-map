package com.demo.weatherMap.repository.openweathermap.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class OpenWeatherMapResponse {

    WeatherDetail[] weather;
}
