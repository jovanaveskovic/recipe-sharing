package net.azeti.challenge.recipe.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Registration response model")
class RegistrationResponse(
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
    val username: String
)
