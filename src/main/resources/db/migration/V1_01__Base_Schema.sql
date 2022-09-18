CREATE EXTENSION pg_crypto;

CREATE TABLE positions(
    id UUID NOT NULL,
    NAME VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE employees(
    id UUID NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    date_of_birth DATE,
    position_id UUID NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (position_id) REFERENCES positions(id)
);