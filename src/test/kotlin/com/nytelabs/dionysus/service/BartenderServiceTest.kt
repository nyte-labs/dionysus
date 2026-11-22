package com.nytelabs.dionysus.service

import com.nytelabs.dionysus.model.DrinkRequest
import com.nytelabs.dionysus.model.IngredientAmountRequest
import com.nytelabs.dionysus.persistence.Drink
import com.nytelabs.dionysus.persistence.DrinkRepository
import com.nytelabs.dionysus.persistence.Ingredient
import com.nytelabs.dionysus.persistence.IngredientRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*

@ExtendWith(MockitoExtension::class)
class BartenderServiceTest {
    private val gin = Ingredient(id = 1, name = "gin", abv = 40f, caloriesPerMl = 2.3f)
    private val vermouth = Ingredient(id = 2, name = "vermouth", abv = 16f, caloriesPerMl = 1.4f)
    private val water = Ingredient(id = 3, name = "water", abv = 0f, caloriesPerMl = 0f)

    @Mock
    private lateinit var drinkRepository: DrinkRepository

    @Mock
    private lateinit var ingredientRepository: IngredientRepository

    @InjectMocks
    private lateinit var service: BartenderService

    @Test
    fun `create drink - copies descriptive fields from the request`() {
        val ingredients = arrayOf(gin to 60f)

        whenever(ingredientRepository.findByName(gin.name)).thenReturn(gin)
        whenever(drinkRepository.save(any())).thenAnswer { it.arguments[0] }

        val drink = service.createDrink(createRequest(ingredients))
        assertEquals("martini", drink.name)
        assertEquals("olive", drink.garnish)
        assertEquals("stir with ice", drink.preparation)
    }
    
    @Test
    fun `create drink - abv is weighted by volume`() {
        val ingredients = arrayOf(gin to 60f, vermouth to 30f)

        whenever(ingredientRepository.findByName(gin.name)).thenReturn(gin)
        whenever(ingredientRepository.findByName(vermouth.name)).thenReturn(vermouth)
        whenever(drinkRepository.save(any())).thenAnswer { it.arguments[0] }

        val drink = service.createDrink(createRequest(ingredients))

        // (40 * 60 + 16 * 30) / 90
        assertEquals(32f, drink.abv, 0.0001f)
    }

    @Test
    fun `create drink - calories are summed across ingredients`() {
        val ingredients = arrayOf(gin to 60f, vermouth to 30f)

        whenever(ingredientRepository.findByName(gin.name)).thenReturn(gin)
        whenever(ingredientRepository.findByName(vermouth.name)).thenReturn(vermouth)
        whenever(drinkRepository.save(any())).thenAnswer { it.arguments[0] }

        val drink = service.createDrink(createRequest(ingredients))

        assertEquals(180f, drink.calories, 0.0001f)
    }

    @Test
    fun `create drink - non-alcoholic ingredients dilute abv`() {
        val ingredients = arrayOf(gin to 50f, water to 50f)

        whenever(ingredientRepository.findByName(gin.name)).thenReturn(gin)
        whenever(ingredientRepository.findByName(water.name)).thenReturn(water)
        whenever(drinkRepository.save(any())).thenAnswer { it.arguments[0] }

        val drink = service.createDrink(createRequest(ingredients))

        assertEquals(20f, drink.abv, 0.0001f)
    }

    @Test
    fun `create drink - saves the built drink and returns the saved result`() {
        val ingredients = arrayOf(gin to 50f)
        whenever(ingredientRepository.findByName(gin.name)).thenReturn(gin)

        val persisted = Drink(id = 42, garnish = "olive", name = "martini", preparation = "stir with ice", abv = 40f, calories = 138f)
        whenever(drinkRepository.save(any())).thenReturn(persisted)

        val result = service.createDrink(createRequest(ingredients))

        argumentCaptor<Drink>().apply {
            verify(drinkRepository).save(capture())
            assertEquals("martini", firstValue.name)
        }
        assertSame(persisted, result)
    }

    @Test
    fun `create drink - unknown ingredient throws and nothing is saved`() {
        val ingredients = arrayOf(gin to 50f, Ingredient(name = "unknown", abv = 0f, caloriesPerMl = 0f) to 10f)

        whenever(ingredientRepository.findByName(gin.name)).thenReturn(gin)

        assertThrows<IngredientNotFoundException> {
            service.createDrink(createRequest(ingredients))
        }

        verifyNoInteractions(drinkRepository)
    }

    @Test
    fun `create drink - no ingredients throws and nothing is saved`() {
        assertThrows<IllegalArgumentException> { service.createDrink(createRequest(arrayOf())) }

        verifyNoInteractions(drinkRepository)
    }

    @Test
    fun `create drink - zero total volume throws and nothing is saved`() {
        val ingredients = arrayOf(gin to 0f)

        whenever(ingredientRepository.findByName(gin.name)).thenReturn(gin)

        assertThrows<IllegalArgumentException> { service.createDrink(createRequest(ingredients)) }

        verifyNoInteractions(drinkRepository)
    }

    private fun createRequest(ingredients: Array<Pair<Ingredient, Float>>) = DrinkRequest(
        name = "martini",
        garnish = "olive",
        preparation = "stir with ice",
        ingredients = ingredients.map { (ingredient, ml) -> IngredientAmountRequest(ingredient.name, ml) },
    )
}
