package com.nytelabs.dionysus.persistence

import jakarta.persistence.*

@Entity
class Ingredient(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    val name: String,
    val abv: Float,

    @Column("calories_per_ml")
    val caloriesPerMl: Float
) {
    override fun toString(): String {
        return "Ingredient(id=$id, name='$name', abv=$abv, caloriesPerMl=$caloriesPerMl)"
    }
}
