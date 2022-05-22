package com.demo.weatherMap.service;

import com.demo.weatherMap.dto.WeatherResponse;
import com.demo.weatherMap.exception.WeatherServiceException;
import com.demo.weatherMap.repository.database.Location;
import com.demo.weatherMap.repository.database.OpenWeatherDatabaseRepository;
import com.demo.weatherMap.repository.database.Weather;
import com.demo.weatherMap.repository.openweathermap.WeatherMapRepository;
import com.demo.weatherMap.repository.openweathermap.dto.WeatherDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

import static com.demo.weatherMap.utils.Constants.PARAM_SEPARATOR;

@Service
public class WeatherMapService {

    @Autowired
    WeatherMapRepository weatherMapRepository;

    @Autowired
    OpenWeatherDatabaseRepository openWeatherMapRepository;

    /**
     * processes business logic for weather app,
     * query database and then repository if no records found in database
     * @param city to get weather for
     * @param countryCode to get weather for
     * @return WeatherResponse with short description of current weather
     */
    public WeatherResponse getWeatherData(String city, String countryCode) {
        //query data from database
        Optional<Weather> weather = openWeatherMapRepository.findById(Location.builder()
                        .city(city)
                        .country(countryCode).build());
        if (weather.isPresent()) {
            return WeatherResponse.builder().description(weather.get().getDescription())
                    .build();
        } else {
            //call openWeatherMap.org downstream repository
            WeatherDetail weatherDetail =
                    Arrays.stream(weatherMapRepository.callOpenWeatherMap(city+PARAM_SEPARATOR+countryCode)
                            .getWeather()).findFirst()
                    .orElseThrow(() -> new WeatherServiceException("Error while getting weather details"));

            //save the data into database
            openWeatherMapRepository.save(Weather.builder()
                    .city(city)
                    .country(countryCode)
                    .description(weatherDetail.getDescription())
                    .id(weatherDetail.getId()).build());

            return WeatherResponse.builder().description(weatherDetail.getDescription())
                    .build();
        }
    }
}
