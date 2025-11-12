package com.unified_services_api.domain.dto;

import java.time.Instant;

public record UnifiedWeatherDTO(
        String city,
        double latitude,
        double longitude,
        String units,            // "metric" | "imperial"
        double temperature,      // in chosen units
        double humidityPct,
        double windSpeed,        // in chosen units
        String weatherSource,
        Instant asOf
) {}
