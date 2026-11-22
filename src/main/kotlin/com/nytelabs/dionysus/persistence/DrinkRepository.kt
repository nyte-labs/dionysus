package com.nytelabs.dionysus.persistence

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.Repository

interface DrinkRepository : Repository<Drink, Int> {
    fun findAll(pageable: Pageable): Page<Drink>

    fun findByName(name: String): Drink?

    fun save(drink: Drink): Drink
}