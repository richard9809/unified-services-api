package com.unified_services_api.repo;

import com.unified_services_api.domain.LocationCache;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationCacheRepository extends JpaRepository<LocationCache, Long> {
    Optional<LocationCache> findLocationCacheByNormalizedCity(String normalizedCity);
}
