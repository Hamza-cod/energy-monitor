CREATE TABLE IF NOT EXISTS alerts (
        id uuid PRIMARY KEY NOT NULL ,
        sent boolean default false,
        createdAt TIMESTAMP WITH TIME ZONE,
        userId VARCHAR(255)
);