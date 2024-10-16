package net.azeti.challenge.recipe.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Login response model")
data class LoginResponse(
    @field:Schema(
        description = "Access token",
        example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiZXhwIjoxNzMxNzAwMzI0LCJpYXQiOjE3MjkxMDgzMjQsInVzZXJJZCI6MX0.YzvZikR0kc3DYdYLal55qQvPAfKHWWgMOpjNRaTUxlc",
        type = "String"
    )
    val accessToken: String
)
