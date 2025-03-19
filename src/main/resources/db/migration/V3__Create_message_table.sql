DROP TABLE IF EXISTS message CASCADE;

CREATE TABLE message (
                         message_id BIGINT AUTO_INCREMENT NOT NULL,
                         scene_id BIGINT NOT NULL,
                         nickname VARCHAR(255) NOT NULL,
                         content TEXT NOT NULL,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (message_id),
                         FOREIGN KEY (scene_id) REFERENCES scene(scene_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;