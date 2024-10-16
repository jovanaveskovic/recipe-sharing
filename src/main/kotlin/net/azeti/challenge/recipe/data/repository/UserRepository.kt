package net.azeti.challenge.recipe.data.repository

import net.azeti.challenge.recipe.data.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findUserByEmail(email: String): User?
    fun findUserByUsername(username: String): User?
}