package com.unified_services_api.service;

import com.unified_services_api.domain.dto.WeatherResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WeatherClient {
    private final WebClient openMeteoClient;

    public Mono<WeatherResult> current(double lat, double lon) {
        return openMeteoClient.get()
                .uri(uri -> uri.path("/v1/forecast")
                        .queryParam("latitude", lat)
                        .queryParam("longitude", lon)
                        .queryParam("current", "temperature_2m,relative_humidity_2m,wind_speed_10m")
                        .build())
                .retrieve()
                .bodyToMono(WeatherResult.class); // first result or empty
    }
}
