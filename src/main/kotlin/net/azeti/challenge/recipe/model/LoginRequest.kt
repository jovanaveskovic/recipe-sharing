package net.azeti.challenge.recipe.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Login request model")
data class LoginRequest(
    @field:Schema(
        description = "Username",
        example = "test",
        type = "String"
    )
    val username: String,
    @field:Schema(
        description = "User's password",
        example = "test123",
        type = "String"
    )
    val password: String
)
