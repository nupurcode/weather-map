package com.demo.weatherMap.blackbox;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static com.demo.weatherMap.utils.Constants.X_API_KEY;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
public class WeatherMapControllerTest {

    @LocalServerPort
    Integer port;

    public static WireMockServer wireMockServer;
    public String mockAPiKey = "WM-api-key";

    String basePath = "http://localhost:";
    String endpoint = "/weather-app/weather/{city}/{countryCode}";

    @BeforeAll
    static void setUp() {
        wireMockServer = new WireMockServer(wireMockConfig()
                .port(8089));
        wireMockServer.start();
    }

    @AfterAll
    static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void callWeatherApi_withValidData_returnsSuccess() {
        RestAssured
                .given()
                .contentType("application/json")
                .header(X_API_KEY, mockAPiKey)
                .when()
                .get(basePath+port+endpoint,"Delhi","IN")
                .then()
                .statusCode(200)
                .assertThat()
                .body("description", org.hamcrest.core.Is.is("dust"));

        //verify one request sent with correct city name to downstream endpoint
        wireMockServer.verify(1, getRequestedFor(urlEqualTo("/data/2.5/weather?q=Delhi,IN&appId=9c783130312b92a5d55f1786514b6fad")));
    }

    @Test
    public void callWeatherApi_withoutValidHeader_ReturnUnauthorized() {
        RestAssured
                .given()
                .contentType("application/json")
                .when()
                .get(basePath+port+endpoint,"Delhi","IN")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .assertThat()
                .body("message", org.hamcrest.core.StringContains.containsString("x-api-key"));
    }

    @ParameterizedTest
    @CsvSource({
            "Delhi,I, WM-111",
            "Delhi,innnn, WM-111",
            "Delhi,&*^%----, WM-111",
            "Delhi,null, WM-111",
            "Delhi,12988745358751402, WM-111",
            "$&%*&^((&*&_*)(_()_),IN, WM-222",
            "486768,innnn,IN, WM-222",
            "London&*^%----,IN, WM-222",
            "D,IN, WM-222",
            "----,IN, WM-222"
    })
    public void callWeatherApi_invalidCountryPathParam_ReturnBadRequest(String invalidCountryCodeParam, String invalidCityCodeParam, String apiKey) {
        RestAssured
                .given()
                .contentType("application/json")
                .header(X_API_KEY, apiKey)
                .when()
                .get(basePath+port+endpoint,invalidCityCodeParam,invalidCountryCodeParam)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .assertThat()
                .body("message", org.hamcrest.core.StringContains
                        .containsString("parameter should be of minimum size 2"));
    }

    @Test
    public void callWeatherApi_errorResponseFromDownstreamAPICall_returnsError() {
        RestAssured
                .given()
                .contentType("application/json")
                .header(X_API_KEY, "WM-333")
                .when()
                .get(basePath+port+endpoint,"Zootopia","UK")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .assertThat()
                .body("message", org.hamcrest.core.StringContains
                        .containsString("city not found"));

        //verify one request sent with unavailable city name to downstream endpoint
        wireMockServer.verify(1, getRequestedFor(urlEqualTo("/data/2.5/weather?q=Zootopia,UK&appId=9c783130312b92a5d55f1786514b6fad")));

    }

}
