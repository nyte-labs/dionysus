package com.nytelabs.dionysus.model

data class DrinkRequest(val name: String, val garnish: String, val preparation: String, val ingredients: List<IngredientAmountRequest>)