package com.demo.weatherMap.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static com.demo.weatherMap.utils.Constants.X_API_KEY;


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
        )
public class WeatherMapIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Test
    void callWeatherApiWithValidDataReturnsSuccess() {
        RestAssured
                .given()
                .contentType("application/json")
                .header(X_API_KEY, "mock-api-key")
                .when()
                .get("http://localhost:"+port+"/weather-app/weather/Delhi/IN")
                .then()
                .statusCode(200);
    }
}
