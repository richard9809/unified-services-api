package com.unified_services_api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "weather_cache")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WeatherCache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "normalized_city", nullable = false)
    private String normalizedCity;

    @Column(name = "window_start", nullable = false)
    private Instant windowStart;

    @Column(name = "temperature_c")
    private Double temperatureC;

    @Column(name = "humidity_pct")
    private Double humidityPct;

    @Column(name = "wind_speed_ms")
    private Double windSpeedMs;

    @Column(name = "source", nullable = false)
    private String source;

    @Column(name = "fetched_at", nullable = false)
    private Instant fetchedAt = Instant.now();

    @PrePersist
    void onCreate() {
        if (fetchedAt == null) fetchedAt = Instant.now();
    }
}
