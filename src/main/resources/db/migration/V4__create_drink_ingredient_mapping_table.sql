CREATE TABLE drink_ingredient
(
    amount_ml     NUMERIC(4, 1)    NOT NULL,
    drink_id      INTEGER          NOT NULL,
    ingredient_id INTEGER          NOT NULL,
    CONSTRAINT pk_drinkingredient PRIMARY KEY (drink_id, ingredient_id)
);

ALTER TABLE drink_ingredient
    ADD CONSTRAINT FK_DRINKINGREDIENT_ON_DRINK FOREIGN KEY (drink_id) REFERENCES drink (id);

ALTER TABLE drink_ingredient
    ADD CONSTRAINT FK_DRINKINGREDIENT_ON_INGREDIENT FOREIGN KEY (ingredient_id) REFERENCES ingredient (id);