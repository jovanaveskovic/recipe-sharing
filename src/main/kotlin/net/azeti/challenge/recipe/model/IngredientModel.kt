package net.azeti.challenge.recipe.model

import io.swagger.v3.oas.annotations.media.Schema
import net.azeti.challenge.recipe.data.Unit

@Schema(description = "Recipe ingredient model")
data class IngredientModel(
    @field:Schema(
        description = "Ingredient value",
        example = "5",
        type = "String"
    )
    val value: String,
    @field:Schema(
        description = "Ingredient unit",
        example = "pc",
        type = "String",
        allowableValues = ["g", "kg", "ml", "l", "pc", "tsp", "tbsp", "pinch"]
    )
    val unit: Unit,
    @field:Schema(
        description = "Ingredient type",
        example = "egg",
        type = "String"
    )
    val type: String
)