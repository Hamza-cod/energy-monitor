CREATE TABLE IF NOT EXISTS users (
                       id uuid PRIMARY KEY NOT NULL ,
                       name VARCHAR(255),
                       surname VARCHAR(255),
                       email VARCHAR(255) UNIQUE ,
                       address VARCHAR(255),
                       alerting BOOLEAN NOT NULL DEFAULT FALSE,
                       energy_alerting_threshold DOUBLE PRECISION NOT NULL DEFAULT 0
);