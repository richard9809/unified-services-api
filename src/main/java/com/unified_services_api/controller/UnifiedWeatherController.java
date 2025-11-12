package com.unified_services_api.controller;

import com.unified_services_api.domain.dto.UnifiedWeatherDTO;
import com.unified_services_api.service.UnifiedWeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UnifiedWeatherController {
    private final UnifiedWeatherService unifiedWeatherService;

    @GetMapping("/unified-weather")
    public Mono<ResponseEntity<UnifiedWeatherDTO>> getWeather(
            @RequestParam String city,
            @RequestParam(defaultValue = "metric") String units) {
        return unifiedWeatherService.getUnified(city, units)
                .map(ResponseEntity::ok)
                .onErrorResume(ex -> Mono.just(ResponseEntity.badRequest().build()));
    }
}
