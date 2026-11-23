package com.nytelabs.dionysus.service

import com.nytelabs.dionysus.model.DrinkRequest
import com.nytelabs.dionysus.persistence.Drink
import com.nytelabs.dionysus.persistence.DrinkIngredient
import com.nytelabs.dionysus.persistence.DrinkRepository
import com.nytelabs.dionysus.persistence.IngredientRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BartenderService(val drinkRepository: DrinkRepository, val ingredientRepository: IngredientRepository) {
    fun getDrinks(pageable: Pageable): Page<Drink> {
        return drinkRepository.findAll(pageable)
    }

    fun createDrink(drinkRequest: DrinkRequest): Drink {
        val ingredientAmounts = drinkRequest.ingredients.map {
            val ingredient = ingredientRepository.findByName(it.uid) ?: throw IngredientNotFoundException()

            ingredient to it.amountMl
        }

        val totalMl = ingredientAmounts.map { it.second }.sum()
        if (totalMl <= 0) {
            throw IllegalArgumentException("Drink must contain at least one ingredient")
        }

        val drink = Drink(
            garnish = drinkRequest.garnish,
            name = drinkRequest.name,
            preparation = drinkRequest.preparation,
            abv = ingredientAmounts.map { (i, ml) -> i.abv * ml }.sum() / totalMl,
            calories = ingredientAmounts.map { (i, ml) -> i.caloriesPerMl * ml }.sum(),
        )

        ingredientAmounts.forEach { (i, ml) ->
            drink.ingredients += DrinkIngredient(drink = drink, ingredient = i, amountMl = ml)
        }

        return drinkRepository.save(drink)
    }
}
