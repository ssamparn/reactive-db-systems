CREATE TABLE IF NOT EXISTS person(
    id           BIGSERIAL,
    first_name   VARCHAR(255) NOT NULL UNIQUE,
    last_name    VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);
