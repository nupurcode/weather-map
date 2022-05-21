package com.demo.weatherMap.exception;


import com.demo.weatherMap.repository.openweathermap.dto.ClientError;
import lombok.Getter;

@Getter
public class WeatherDownstreamException extends RuntimeException{

    ClientError clientError;

    public WeatherDownstreamException(ClientError clientError) {
        super(clientError.getMessage());
        this.clientError = clientError;
    }
}
