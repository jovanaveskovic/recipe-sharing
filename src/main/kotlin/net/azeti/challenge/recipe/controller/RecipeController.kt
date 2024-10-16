package net.azeti.challenge.recipe.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import net.azeti.challenge.recipe.model.RecipeRequest
import net.azeti.challenge.recipe.model.RecipeResponse
import net.azeti.challenge.recipe.service.RecipeService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(RecipeController.BASE_URL)
@Validated
class RecipeController(
    val recipeService: RecipeService
) {

    companion object {
        const val BASE_URL = "recipe"
        const val RECIPE_URL = "/{Recipe-Id}"
        const val GET_BY_USERNAME_URL = "/username/{Username}"
        const val GET_BY_TITLE_URL = "/title/{Title}"
        const val GET_RECOMMENDED_RECIPE = "/recommendation"
    }

    @Operation(summary = "Creates a new recipe")
    @PostMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun createRecipe(authentication: Authentication, @RequestBody recipeRequest: RecipeRequest): RecipeResponse =
        recipeService.createRecipe(authentication, recipeRequest)

    @Operation(summary = "Reads recipe by id")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Successfully retrieved recipe"),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    @GetMapping(
        RECIPE_URL,
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun getRecipe(@PathVariable("Recipe-Id") recipeId: Long): RecipeResponse =
        recipeService.getRecipe(recipeId)

    @Operation(summary = "Updates recipe")
    @PutMapping(
        RECIPE_URL,
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun updateRecipe(
        authentication: Authentication,
        @PathVariable("Recipe-Id") recipeId: Long,
        @RequestBody recipeRequest: RecipeRequest
    ): RecipeResponse =
        recipeService.updateRecipe(authentication, recipeId, recipeRequest)

    @Operation(summary = "Deletes recipe")
    @DeleteMapping(
        RECIPE_URL,
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun deleteRecipe(
        authentication: Authentication,
        @PathVariable("Recipe-Id") recipeId: Long
    ) =
        recipeService.deleteRecipe(authentication, recipeId)

    @Operation(
        summary = "Gets recipes by username",
        description = "This operation allows user to search recipes based on username (exact match or partial match)"
    )
    @GetMapping(
        GET_BY_USERNAME_URL,
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun getRecipesByUsername(@PathVariable("Username") username: String): List<RecipeResponse> =
        recipeService.getRecipesByUsername(username)

    @Operation(
        summary = "Gets recipes by title",
        description = "This operation allows user to search recipes based on title (exact match or partial match)"
    )
    @GetMapping(
        GET_BY_TITLE_URL,
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun getRecipesByTitle(@PathVariable("Title") title: String): List<RecipeResponse> =
        recipeService.getRecipesByTitle(title)

    @Operation(
        summary = "Gets recipe recommendation",
        description = "This operation operation will give user a recipe recommendation based on the current weather situation in Berlin. If the temperature is above 28°C, it will avoid the recipes that require baking. If the temperature is below 2°C, it will avoid the recipes using frozen ingredients."
    )
    @GetMapping(
        GET_RECOMMENDED_RECIPE,
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun getRecommendedRecipe(): RecipeResponse =
        recipeService.getRecommendedRecipe()
}