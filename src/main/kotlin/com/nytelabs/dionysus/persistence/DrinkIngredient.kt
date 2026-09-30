package com.nytelabs.dionysus.persistence

import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import java.math.BigDecimal

@Entity
class DrinkIngredient(
    @EmbeddedId
    val id: DrinkIngredientKey = DrinkIngredientKey(),

    @ManyToOne
    @MapsId("drinkId")
    @JoinColumn("drink_id")
    val drink: Drink,

    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn("ingredient_id")
    val ingredient: Ingredient,

    @Column("amount_ml", scale = 1, precision = 4)
    val amountMl: BigDecimal,
)