CREATE TABLE IF NOT EXISTS location_cache (
    id BIGSERIAL PRIMARY KEY,
    normalized_city TEXT UNIQUE NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS weather_cache (
    id BIGSERIAL PRIMARY KEY,
    normalized_city TEXT NOT NULL,
    window_start TIMESTAMP NOT NULL,
    temperature_c DOUBLE PRECISION,
    humidity_pct DOUBLE PRECISION,
    wind_speed_ms DOUBLE PRECISION,
    source TEXT NOT NULL,
    fetched_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (normalized_city, window_start)
);