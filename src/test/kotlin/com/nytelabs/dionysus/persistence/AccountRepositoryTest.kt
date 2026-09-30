package com.nytelabs.dionysus.persistence

import com.nytelabs.dionysus.TestcontainersConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import org.springframework.context.annotation.Import

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration::class)
class AccountRepositoryTest @Autowired constructor(
    private val repository: AccountRepository,
    private val entityManager: TestEntityManager,
) {
    @Test
    fun `find by username - found`() {
        val saved = repository.save(
            Account(username = "username", password = "test")
        )

        entityManager.flush()
        entityManager.clear()

        val found = repository.findByUsername(saved.username)

        assertNotNull(found)
        assertEquals("username", found!!.username)
        assertEquals("test", found.password)
        assertTrue(found.enabled)
    }

    @Test
    fun `find by username - missing`() {
        assertNull(repository.findByUsername("some username"))
    }
}