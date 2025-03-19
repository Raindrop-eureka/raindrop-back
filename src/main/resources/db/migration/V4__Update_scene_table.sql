ALTER TABLE scene
    RENAME COLUMN is_visible TO is_message_visible;

ALTER TABLE scene
    ADD COLUMN is_scene_visible BOOLEAN NOT NULL DEFAULT FALSE;