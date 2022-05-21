package com.demo.weatherMap.blackbox;

import com.demo.weatherMap.config.OpenWeatherWiremockStub;
import com.demo.weatherMap.controller.WeatherMapController;
import com.demo.weatherMap.repository.database.OpenWeatherDatabaseRepository;
import com.demo.weatherMap.repository.openweathermap.WeatherMapRepository;
import com.demo.weatherMap.service.WeatherMapService;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureWireMock
@AutoConfigureRestDocs
public class WeatherMapControllerTest {

    @Autowired
    MockMvc mvc;

    public static WireMockServer wireMockServer;
    public static HttpHeaders httpHeaders;

    @BeforeAll
    static void setUp() {
        //mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        httpHeaders = new HttpHeaders();
        httpHeaders.add("x-api-key", "dummy-api-key");
        wireMockServer = new WireMockServer(wireMockConfig()
                .port(8089)
                .usingFilesUnderDirectory("stubs"));
        wireMockServer.start();
    }

    @AfterAll
    static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void callWeatherApiWithValidDataReturnsSuccess() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/weather-app/weather/Delhi/IN")
                        .header("x-api-key", "mock-api-key")
                        .contentType(MediaType.APPLICATION_JSON));

        verify(getRequestedFor(urlEqualTo("http://localhost:8080/weather-app/weather/Delhi/IN")));
    }


}
