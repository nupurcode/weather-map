package com.demo.weatherMap.controller;

import com.demo.weatherMap.dto.WeatherResponse;
import com.demo.weatherMap.service.WeatherMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@RestController
public class WeatherMapController {

    @Autowired
    WeatherMapService weatherMapService;

    @GetMapping(value = "/weather/{city}/{countryCode}")
    public ResponseEntity<WeatherResponse> getWeatherResponse(@PathVariable("city") @NotBlank @Size(max = 20) String city,
                                                     @PathVariable("countryCode") @NotBlank @Size(max = 2) String countryCode,
                                                     @RequestHeader("x-api-key") String apiKey) {
        return new ResponseEntity<>(weatherMapService.getWeatherData(city, countryCode),
                HttpStatus.OK);
    }

}
