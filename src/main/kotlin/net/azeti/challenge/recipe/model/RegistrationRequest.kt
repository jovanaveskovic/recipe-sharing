package net.azeti.challenge.recipe.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Registration request model")
data class RegistrationRequest(
    @field:Schema(
        description = "User's email",
        example = "test@gmail.com",
        type = "String"
    )
    val email: String,
    @field:Schema(
        description = "Username",
        example = "test",
        type = "String"
    )
    val username: String,
    @field:Schema(
        description = "Password",
        example = "test123",
        type = "String"
    )
    val password: String
)
