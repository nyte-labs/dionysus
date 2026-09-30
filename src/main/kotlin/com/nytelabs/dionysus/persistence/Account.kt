package com.nytelabs.dionysus.persistence

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    val username: String,
    val password: String,
    val enabled: Boolean = true,
) {
    override fun toString(): String {
        return "Account(id=$id, username='$username', enabled=$enabled)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Account) return false
        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}