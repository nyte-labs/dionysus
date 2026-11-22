CREATE TABLE ingredient
(
    id                  INT GENERATED ALWAYS AS IDENTITY,
    name                VARCHAR(255)  NOT NULL,
    abv                 REAL          NOT NULL,
    calories_per_ml     REAL          NOT NULL,
    CONSTRAINT pk_ingredient PRIMARY KEY (id),
    CONSTRAINT uq_ingredient_name UNIQUE (name)
);
