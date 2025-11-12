package com.unified_services_api.service;

import com.unified_services_api.domain.LocationCache;
import com.unified_services_api.domain.WeatherCache;
import com.unified_services_api.domain.dto.UnifiedWeatherDTO;
import com.unified_services_api.domain.dto.WeatherResult;
import com.unified_services_api.repo.LocationCacheRepository;
import com.unified_services_api.repo.WeatherCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class UnifiedWeatherService {
    private final GeoClient geoClient;
    private final WeatherClient weatherClient;
    private final LocationCacheRepository locationRepo;
    private final WeatherCacheRepository weatherRepo;
    @Value("${app.cache.minutes:15}") private int cacheMinutes;

    public Mono<UnifiedWeatherDTO> getUnified(String city, String units) {
        final String normalized = city.trim().toLowerCase();
        final Instant windowStart = Instant.now()
                .minus(Duration.ofMinutes(Instant.now().atZone(ZoneOffset.UTC).getMinute() % cacheMinutes))
                .truncatedTo(ChronoUnit.MINUTES);

        return ensureCoords(normalized, city)
                .flatMap(coords -> fetchWeather(normalized, windowStart, coords))
                .map(raw -> toDto(city, units, raw));
    }

    private Mono<double[]> ensureCoords(String normalized, String rawCity) {
        return Mono.fromCallable(() -> locationRepo.findLocationCacheByNormalizedCity(normalized))
                .flatMap(opt -> opt.map(loc -> Mono.just(new double[]{loc.getLatitude(), loc.getLongitude()}))
                        .orElseGet(() -> geoClient.geocode(rawCity)
                                .flatMap(geo -> {
                                    if (geo == null) return Mono.error(new RuntimeException("City not found"));
                                    double lat = Double.parseDouble(geo.lat());
                                    double lon = Double.parseDouble(geo.lon());
                                    LocationCache entry = new LocationCache();
                                    entry.setNormalizedCity(normalized);
                                    entry.setLatitude(lat);
                                    entry.setLongitude(lon);
                                    locationRepo.save(entry);
                                    return Mono.just(new double[]{lat, lon});
                                })));
    }

    private Mono<WeatherCache> fetchWeather(String normalized, Instant windowStart, double[] coords) {
        return Mono.fromCallable(() -> weatherRepo.findWeatherCacheByNormalizedCityAndWindowStart(normalized, windowStart))
                .flatMap(opt -> opt.map(Mono::just).orElseGet(() ->
                        weatherClient.current(coords[0], coords[1]).flatMap(w -> {
                            WeatherResult.WeatherCurrent c = w.current();
                            WeatherCache cache = new WeatherCache();
                            cache.setNormalizedCity(normalized);
                            cache.setWindowStart(windowStart);
                            cache.setTemperatureC(c.temperature_2m());
                            cache.setHumidityPct(c.relative_humidity_2m());
                            // open-Meteo wind_speed_10m is m/s by default if using 'current' group; adjust if API changes
                            cache.setWindSpeedMs(c.wind_speed_10m());
                            cache.setSource("open-meteo");
                            weatherRepo.save(cache);
                            return Mono.just(cache);
                        })
                ));
    }

    private UnifiedWeatherDTO toDto(String city, String units, WeatherCache wc) {
        double temp = wc.getTemperatureC();
        double wind = wc.getWindSpeedMs();
        if ("imperial".equalsIgnoreCase(units)) {
            temp = temp * 9/5 + 32.0;       // C -> F
            wind = wind * 2.23694;          // m/s -> mph
        }
        // Fetch lat/lon for output
        var loc = locationRepo.findLocationCacheByNormalizedCity(city.toLowerCase()).orElseThrow();
        return new UnifiedWeatherDTO(
                city,
                loc.getLatitude(),
                loc.getLongitude(),
                (units == null || units.isBlank()) ? "metric" : units.toLowerCase(),
                temp,
                wc.getHumidityPct(),
                wind,
                wc.getSource(),
                wc.getFetchedAt()
        );
    }
}
