CREATE TABLE IF NOT EXISTS devices (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255),
                       location VARCHAR(255),
                       user_id BIGINT
);