package com.unified_services_api.repo;

import com.unified_services_api.domain.WeatherCache;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface WeatherCacheRepository extends JpaRepository<WeatherCache, Long> {
    Optional<WeatherCache> findWeatherCacheByNormalizedCityAndWindowStart(String city, Instant windowStart);
}
