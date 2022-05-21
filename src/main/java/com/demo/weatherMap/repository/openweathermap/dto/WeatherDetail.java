package com.demo.weatherMap.repository.openweathermap.dto;

import lombok.Getter;

@Getter
public class WeatherDetail {

    Integer id;
    String main;
    String description;
    String icon;

}
