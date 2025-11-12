package com.unified_services_api.domain.dto;

public record WeatherResult(
        WeatherCurrent current
) {
    public static record WeatherCurrent(
            Double temperature_2m,
            Double relative_humidity_2m,
            Double wind_speed_10m,
            String time
    ) {}
}
