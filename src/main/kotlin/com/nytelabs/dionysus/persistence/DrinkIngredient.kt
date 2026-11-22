package com.nytelabs.dionysus.persistence

import jakarta.persistence.*

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

    @Column("amount_ml")
    val amountMl: Float,
)