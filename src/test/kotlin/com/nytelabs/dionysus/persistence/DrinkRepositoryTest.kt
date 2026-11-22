package com.nytelabs.dionysus.persistence

import com.nytelabs.dionysus.TestcontainersConfiguration
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import org.springframework.context.annotation.Import

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
            Ingredient(name = "ingredient1", abv = 40f, caloriesPerMl = 2.63f)
        )
        val drink = Drink(
            garnish = "garnish1",
            name = "drink1",
            preparation = "preparation1",
            abv = 33f,
            calories = 134f,
        )
        drink.ingredients += DrinkIngredient(
            drink = drink,
            ingredient = ingredient,
            amountMl = 60f,
        )
        repository.save(drink)

        entityManager.flush()
        entityManager.clear()

        val found = repository.findByName("drink1")

        assertNotNull(found)
        assertEquals("drink1", found!!.name)
        assertEquals(1, found.ingredients.size)
        assertEquals("ingredient1", found.ingredients.first().ingredient.name)
        assertEquals(60f, found.ingredients.first().amountMl)
    }

    @Test
    fun `find by name - missing`() {
        assertNull(repository.findByName("drink2"))
    }
}