package net.azeti.challenge.recipe.exception

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Error response")
class ApiError(
    @field:Schema(
        description = "Bad request",
        example = "***",
        type = "Int"
    )
    val status: Int?,
    @field:Schema(
        description = "Error message",
        example = "Error message",
        type = "String"
    )
    val message: String?
)