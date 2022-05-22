package com.demo.weatherMap.repository.openweathermap.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherDetail {

    Integer id;
    String main;
    String description;
    String icon;

}
