package com.nytelabs.dionysus.persistence

import jakarta.persistence.*

@Entity
class Drink(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    val garnish: String,
    val name: String,
    val preparation: String,
    val abv: Float,
    val calories: Float,

    @OneToMany(mappedBy = "drink", cascade = [CascadeType.ALL], orphanRemoval = true)
    val ingredients: MutableSet<DrinkIngredient> = mutableSetOf()
) {
    override fun toString(): String {
        return "Drink(id=$id, garnish='$garnish', name='$name', preparation='$preparation', abv='$abv', calories='$calories')"
    }
}
