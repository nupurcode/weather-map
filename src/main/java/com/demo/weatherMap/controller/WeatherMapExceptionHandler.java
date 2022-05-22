package com.demo.weatherMap.controller;

import com.demo.weatherMap.dto.ErrorResponse;
import com.demo.weatherMap.exception.WeatherDownstreamException;
import com.demo.weatherMap.exception.WeatherServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class WeatherMapExceptionHandler {

    @ExceptionHandler({WeatherServiceException.class})
    public ResponseEntity<ErrorResponse> handleWeatherServiceException(WeatherServiceException weatherServiceException) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(weatherServiceException.getMessage())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({MissingRequestHeaderException.class})
    public ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException missingRequestHeaderException) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(missingRequestHeaderException.getMessage())
                .build(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException constraintViolationException) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(constraintViolationException.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({WeatherDownstreamException.class})
    public ResponseEntity<ErrorResponse> handleWeatherDownstreamException(WeatherDownstreamException weatherDownstreamException) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(weatherDownstreamException.getClientError().getMessage())
                .error(HttpStatus.resolve(weatherDownstreamException.getClientError().getCod()).getReasonPhrase())
                .build(),
                HttpStatus.resolve(weatherDownstreamException.getClientError().getCod()));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleGenericException(Exception exception) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(exception.getMessage())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
