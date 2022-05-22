package com.demo.weatherMap.repository.openweathermap;

import com.demo.weatherMap.exception.WeatherDownstreamException;
import com.demo.weatherMap.repository.openweathermap.dto.ClientError;
import com.demo.weatherMap.repository.openweathermap.dto.OpenWeatherMapResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class WeatherMapRepository {

    @Value("${services.openweathermap.endpoint}")
    String openWeatherMapEndpoint;

    @Value("${services.openweathermap.path}")
    String openWeatherMapPath;

    @Value("${services.openweathermap.appid}")
    String openWeatherMapAppId;

    private final String locationParameter = "q";
    private final String appIdParameter = "appId";

    /**
     * sends rest call to openWeatherMap.org
     * @param location composite of city and country code for weather query
     * @return selective response parameters from downstream
     */
    public OpenWeatherMapResponse callOpenWeatherMap(String location) {
            return WebClient.create(openWeatherMapEndpoint)
                    .get()
                    .uri(uriBuilder -> uriBuilder.path(openWeatherMapPath)
                            .queryParam(locationParameter, location)
                            .queryParam(appIdParameter, openWeatherMapAppId)
                            .build())
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> response.bodyToMono(ClientError.class)
                            .flatMap(clientError -> Mono.error(new WeatherDownstreamException(clientError))))
                    .bodyToMono(OpenWeatherMapResponse.class)
                    .block();
    }
}
