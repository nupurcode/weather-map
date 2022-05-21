package com.demo.weatherMap.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.stereotype.Component;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@TestComponent
public class OpenWeatherWiremockStub {

    public WireMockServer createWiremockStub() {
        WireMockServer wireMock = new WireMockServer(wireMockConfig()
                .port(8089)
                .usingFilesUnderDirectory("stubs"));
        return wireMock;
    }
}
