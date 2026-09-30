package com.nytelabs.dionysus.persistence

import org.springframework.data.repository.Repository

interface DrinkRepository : Repository<Drink, Int> {
    fun findByName(name: String): Drink?

    fun save(drink: Drink): Drink
}