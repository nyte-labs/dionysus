package com.nytelabs.dionysus.persistence

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.Repository

interface IngredientRepository : Repository<Ingredient, Int> {
    fun findAll(pageable: Pageable): Page<Ingredient>

    fun findByName(name: String): Ingredient?

    fun save(ingredient: Ingredient): Ingredient
}