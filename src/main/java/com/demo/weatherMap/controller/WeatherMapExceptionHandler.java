package com.demo.weatherMap.controller;

import com.demo.weatherMap.dto.ErrorResponse;
import com.demo.weatherMap.exception.WeatherDownstreamException;
import com.demo.weatherMap.exception.WeatherServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WeatherMapExceptionHandler {

    @ExceptionHandler({WeatherServiceException.class})
    public ResponseEntity<Object> handleWeatherServiceException(WeatherServiceException weatherServiceException) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(weatherServiceException.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({WeatherDownstreamException.class})
    public ResponseEntity<Object> handleWeatherDownstreamException(WeatherDownstreamException weatherDownstreamException) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(weatherDownstreamException.getClientError().getMessage())
                .error(HttpStatus.resolve(weatherDownstreamException.getClientError().getCod()).getReasonPhrase())
                .build(),
                HttpStatus.resolve(weatherDownstreamException.getClientError().getCod()));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleGenericException(Exception exception) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(exception.getMessage())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
