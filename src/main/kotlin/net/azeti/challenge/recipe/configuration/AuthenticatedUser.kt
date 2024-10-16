package net.azeti.challenge.recipe.configuration

import net.azeti.challenge.recipe.data.User
import org.springframework.security.core.Authentication

/**
 * Shorthand for controllers accessing the authenticated user.
 */
fun Authentication.toUser(): User {
    return principal as User
}