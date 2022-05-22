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
    public static HttpHeaders httpHeaders;
    public String mockAPiKey = "mock-api-key";

    String basePath = "http://localhost:";
    String endpoint = "/weather-app/weather/{city}/{countryCode}";

    @BeforeAll
    static void setUp() {
        httpHeaders = new HttpHeaders();
        httpHeaders.add("x-api-key", "dummy-api-key");
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

        //verify NO request sent with unavailable city name to downstream endpoint
        wireMockServer.verify(0, getRequestedFor(urlEqualTo("/data/2.5/weather?q=Delhi,IN&appId=9c783130312b92a5d55f1786514b6fad")));
    }

    @ParameterizedTest
    @CsvSource({
            "Delhi,I",
            "Delhi,innnn",
            "Delhi,&*^%----",
            "Delhi,null",
            "Delhi,12988745358751402",
            "$&%*&^((&*&_*)(_()_),IN",
            "486768,innnn,IN",
            "London&*^%----,IN",
            "D,IN",
            "----,IN"
    })
    public void callWeatherApi_invalidCountryPathParam_ReturnBadRequest(String invalidCountryCodeParam, String invalidCityCodeParam) {
        RestAssured
                .given()
                .contentType("application/json")
                .header(X_API_KEY, mockAPiKey)
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
                .header(X_API_KEY, mockAPiKey)
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
