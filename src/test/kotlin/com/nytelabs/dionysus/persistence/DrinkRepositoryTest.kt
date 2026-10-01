package com.nytelabs.dionysus.persistence

import com.nytelabs.dionysus.TestcontainersConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import org.springframework.context.annotation.Import
import java.math.BigDecimal

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration::class)
class DrinkRepositoryTest @Autowired constructor(
    private val repository: DrinkRepository,
    private val ingredientRepository: IngredientRepository,
    private val entityManager: TestEntityManager,
) {
    @Test
    fun `find by name - found`() {
        val ingredient = ingredientRepository.save(
            Ingredient(name = "ingredient1", abv = 40, caloriesPer100Ml = 263)
        )
        val drink = Drink(
            garnish = "garnish1",
            name = "drink1",
            preparation = "preparation1",
            abv = 33,
            calories = 134,
        )
        drink.ingredients += DrinkIngredient(
            drink = drink,
            ingredient = ingredient,
            amountMl = BigDecimal("60.0"),
        )
        repository.save(drink)

        entityManager.flush()
        entityManager.clear()

        val found = repository.findByName("drink1")

        assertNotNull(found)
        assertEquals("drink1", found!!.name)
        assertEquals(1, found.ingredients.size)
        assertEquals("ingredient1", found.ingredients.first().ingredient.name)
        assertEquals(BigDecimal("60.0"), found.ingredients.first().amountMl)
    }

    @Test
    fun `find by name - missing`() {
        assertNull(repository.findByName("drink2"))
    }
}