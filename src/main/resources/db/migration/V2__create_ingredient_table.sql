CREATE TABLE ingredient
(
    id                  INT GENERATED ALWAYS AS IDENTITY,
    name                VARCHAR(255) NOT NULL,
    abv                 INTEGER      NOT NULL,
    calories_per_100_ml INTEGER      NOT NULL,
    CONSTRAINT pk_ingredient PRIMARY KEY (id),
    CONSTRAINT uq_ingredient_name UNIQUE (name)
);
