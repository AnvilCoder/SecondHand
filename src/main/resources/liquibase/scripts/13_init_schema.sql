-- liquibase formatted sql

-- changeset Ldv236:1
CREATE TABLE image (
    id INT GENERATED ALWAYS AS IDENTITY,
    image BYTEA,

    CONSTRAINT image_pk PRIMARY KEY (id)
);

CREATE TABLE users (
    id INT GENERATED ALWAYS AS IDENTITY,
    image_id INT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    phone TEXT NOT NULL,
    role TEXT NOT NULL,

    CONSTRAINT users_pk PRIMARY KEY (id),
    CONSTRAINT fk_image_id FOREIGN KEY (image_id) REFERENCES image(id)
);

CREATE TABLE ads (
    id INT GENERATED ALWAYS AS IDENTITY,
    title TEXT,
    description TEXT,
    price INT,
    image_id INT,
    user_id INT,

    CONSTRAINT ads_pk PRIMARY KEY (id),
    CONSTRAINT fk_image_id FOREIGN KEY (image_id) REFERENCES image(id),
    CONSTRAINT fk_users_id FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE comments (
    id INT GENERATED ALWAYS AS IDENTITY,
    text TEXT,
    created_at TIMESTAMP,
    author_id INT,
    ad_id INT,

    CONSTRAINT comments_pk PRIMARY KEY (id),
    CONSTRAINT fk_users_id FOREIGN KEY (author_id) REFERENCES users(id),
    CONSTRAINT fk_ads_id FOREIGN KEY (ad_id) REFERENCES ads(id)
);

