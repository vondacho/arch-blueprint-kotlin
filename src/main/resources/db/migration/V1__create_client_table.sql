CREATE TABLE client
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    age         SMALLINT,
    created_at  TIMESTAMP   NOT NULL,
    modified_at TIMESTAMP   NOT NULL,
    activated   BOOLEAN DEFAULT TRUE
);

CREATE
UNIQUE INDEX uk_client_name ON client (name);
