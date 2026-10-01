package com.nytelabs.dionysus.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Ingredient(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    val name: String,
    val abv: Int,
    @Column("calories_per_100_ml")
    val caloriesPer100Ml: Int
) {
    override fun toString(): String {
        return "Ingredient(id=$id, name='$name', abv=$abv, caloriesPer100Ml=$caloriesPer100Ml)"
    }
}
