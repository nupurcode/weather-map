package com.demo.weatherMap.controller;

import com.demo.weatherMap.dto.WeatherResponse;
import com.demo.weatherMap.service.WeatherMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.demo.weatherMap.utils.Constants.*;

@RestController
@Validated
public class WeatherMapController {

    @Autowired
    WeatherMapService weatherMapService;

    /**
     * serves weather queries based on city and country
     * @param city to get the weather
     * @param countryCode to get the weather
     * @param apiKey to authenticate and rate limit
     * @return json response with short description of current weather
     */
    @GetMapping(value = "/weather/{city}/{countryCode}")
    public ResponseEntity<WeatherResponse> getWeatherResponse(@PathVariable("city") @NotBlank @Size(min = 2, message = INVALID_CITY_PARAM_MESSAGE) String city,
                                                     @PathVariable("countryCode") @NotBlank @Size(min = 2, max = 2, message = INVALID_COUNTRY_PARAM_MESSAGE) String countryCode,
                                                     @RequestHeader(X_API_KEY) String apiKey) {

        return new ResponseEntity<>(weatherMapService.getWeatherData(city, countryCode),
                HttpStatus.OK);
    }

}
