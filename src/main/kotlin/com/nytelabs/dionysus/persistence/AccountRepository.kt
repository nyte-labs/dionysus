package com.nytelabs.dionysus.persistence

import org.springframework.data.repository.Repository

interface AccountRepository : Repository<Account, Int> {
    fun findByUsername(username: String): Account?

    fun save(account: Account): Account
}