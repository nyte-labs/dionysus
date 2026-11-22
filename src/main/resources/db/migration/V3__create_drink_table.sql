CREATE TABLE drink
(
    id          INT GENERATED ALWAYS AS IDENTITY,
    garnish     VARCHAR(255) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    preparation TEXT         NOT NULL,
    abv         REAL         NOT NULL,
    calories    REAL         NOT NULL,
    CONSTRAINT pk_drink PRIMARY KEY (id),
    CONSTRAINT uq_drink_name UNIQUE (name)
);
