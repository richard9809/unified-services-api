package com.unified_services_api.service;


import com.unified_services_api.domain.dto.GeoResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GeoClient {
    private final WebClient nominatimClient;

    public Mono<GeoResult> geocode(String city) {
        return nominatimClient.get()
                .uri(uri -> uri.path("/search")
                        .queryParam("q", city)
                        .queryParam("format", "json")
                        .queryParam("limit", 1).build())
                .retrieve()
                .bodyToFlux(GeoResult.class)
                .next(); // first result or empty
    }
}
