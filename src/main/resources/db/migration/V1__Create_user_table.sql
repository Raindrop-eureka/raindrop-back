CREATE TABLE `user` (
                        social_id VARCHAR(255) NOT NULL,
                        name VARCHAR(255) NOT NULL,
                        PRIMARY KEY (social_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;