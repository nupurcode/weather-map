package com.demo.weatherMap.repository.openweathermap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDetail {

    Integer id;
    String main;
    String description;
    String icon;

}
