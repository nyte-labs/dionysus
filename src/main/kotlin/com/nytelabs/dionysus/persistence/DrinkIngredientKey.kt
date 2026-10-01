package com.nytelabs.dionysus.persistence

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import org.hibernate.proxy.HibernateProxy
import java.io.Serializable
import java.util.Objects

@Embeddable
class DrinkIngredientKey(
    @Column("drink_id") var drinkId: Int? = null,

    @Column("ingredient_id") var ingredientId: Int? = null,
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass = if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass = this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as DrinkIngredientKey

        return drinkId == other.drinkId && ingredientId == other.ingredientId
    }

    override fun hashCode(): Int = Objects.hash(drinkId, ingredientId)
}
