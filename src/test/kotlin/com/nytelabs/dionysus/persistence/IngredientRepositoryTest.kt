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

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration::class)
class IngredientRepositoryTest @Autowired constructor(
    private val repository: IngredientRepository,
    private val entityManager: TestEntityManager,
) {
    @Test
    fun `find by name - found`() {
        val saved = repository.save(
            Ingredient(name = "test1", abv = 40f, caloriesPerMl = 0.94f)
        )

        entityManager.flush()
        entityManager.clear()

        val found = repository.findByName(saved.name)

        assertNotNull(found)
        assertEquals("test1", found!!.name)
        assertEquals(40f, found.abv)
        assertEquals(0.94f, found.caloriesPerMl)
    }

    @Test
    fun `find by name - missing`() {
        assertNull(repository.findByName("missing1"))
    }
}