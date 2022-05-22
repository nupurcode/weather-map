package com.demo.weatherMap.exception;


public class WeatherServiceException extends RuntimeException{

    String message;

    public WeatherServiceException(String errorMessage) {
        super(errorMessage);
        this.message = errorMessage;
    }
}
