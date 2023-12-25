-- liquibase formatted sql

-- changeset x3imal:26
ALTER TABLE image
    DROP COLUMN image,
    ADD COLUMN image_path VARCHAR;
