package com.demo.weatherMap.unit;

import com.demo.weatherMap.dto.WeatherResponse;
import com.demo.weatherMap.exception.WeatherDownstreamException;
import com.demo.weatherMap.repository.database.OpenWeatherDatabaseRepository;
import com.demo.weatherMap.repository.database.Weather;
import com.demo.weatherMap.repository.openweathermap.WeatherMapRepository;
import com.demo.weatherMap.repository.openweathermap.dto.ClientError;
import com.demo.weatherMap.repository.openweathermap.dto.OpenWeatherMapResponse;
import com.demo.weatherMap.repository.openweathermap.dto.WeatherDetail;
import com.demo.weatherMap.service.WeatherMapService;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WeatherMapServiceTest {

    @InjectMocks
    WeatherMapService weatherMapService;

    @Mock
    WeatherMapRepository weatherMapRepository;

    @Mock
    OpenWeatherDatabaseRepository openWeatherDatabaseRepository;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Test
    public void testGetWeatherData_noRecordsFromDatabase() {
        when(weatherMapRepository.callOpenWeatherMap("Delhi,IN"))
                .thenReturn(createOpenWeatherMapResponse("dust"));
        when(openWeatherDatabaseRepository.findById(any()))
                .thenReturn(Optional.empty());

        //call service method
        WeatherResponse weatherResponse = weatherMapService.getWeatherData("Delhi","IN");

        //Then Asset
        assertNotNull(weatherResponse);
    }

    @Test
    public void testGetWeatherData_recordFoundFromDatabase() {
        //given we will find the weather data into Database
        when(openWeatherDatabaseRepository.findById(any()))
                .thenReturn(Optional.of(Weather.builder().description("Dust").build()));

        //call service method
        WeatherResponse weatherResponse = weatherMapService.getWeatherData("Delhi","IN");

        //Then Asset
        assertNotNull(weatherResponse);
        assertEquals("Dust", weatherResponse.getDescription());
        verify(weatherMapRepository, never()).callOpenWeatherMap(any());
        Mockito.verify(openWeatherDatabaseRepository, Mockito.times(1)).findById(any());
    }

    @Test
    public void testGetWeatherData_exceptionThrownFromDownstream() {
        //given we will find the weather data into Database
        when(weatherMapRepository.callOpenWeatherMap("Delhi,IN"))
                .thenThrow(new WeatherDownstreamException(ClientError.builder().build()));
        when(openWeatherDatabaseRepository.findById(any()))
                .thenReturn(Optional.empty());

        //Then call service method and Assert Exception
        assertThrows(WeatherDownstreamException.class, () -> weatherMapService.getWeatherData("Delhi","IN"));
        verify(weatherMapRepository, Mockito.times(1)).callOpenWeatherMap(any());
        Mockito.verify(openWeatherDatabaseRepository, Mockito.times(1)).findById(any());
    }

    private OpenWeatherMapResponse createOpenWeatherMapResponse(String description) {
        return OpenWeatherMapResponse.builder()
                .weather(new WeatherDetail[]{
                        WeatherDetail.builder()
                                .description(description)
                                .id(111)
                                .icon("50d")
                                .main("Haze").build()}).build();
    }

}
