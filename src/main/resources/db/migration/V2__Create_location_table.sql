DROP TABLE IF EXISTS location CASCADE;

CREATE TABLE location (
                          location_id BIGINT AUTO_INCREMENT NOT NULL,
                          latitude DOUBLE NOT NULL,
                          longitude DOUBLE NOT NULL,
                          PRIMARY KEY (location_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;