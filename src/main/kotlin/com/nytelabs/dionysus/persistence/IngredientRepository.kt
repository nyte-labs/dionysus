package com.nytelabs.dionysus.persistence

import org.springframework.data.repository.Repository

interface IngredientRepository : Repository<Ingredient, Int> {
    fun findByName(name: String): Ingredient?

    fun save(ingredient: Ingredient): Ingredient
}