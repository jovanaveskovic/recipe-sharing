package net.azeti.challenge.recipe.service

import net.azeti.challenge.recipe.configuration.toUser
import net.azeti.challenge.recipe.data.Ingredient
import net.azeti.challenge.recipe.data.Recipe
import net.azeti.challenge.recipe.data.repository.IngredientRepository
import net.azeti.challenge.recipe.data.repository.RecipeRepository
import net.azeti.challenge.recipe.exception.ApiException
import net.azeti.challenge.recipe.model.IngredientModel
import net.azeti.challenge.recipe.model.RecipeRequest
import net.azeti.challenge.recipe.model.RecipeResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class RecipeService(
    @Value("\${recipe.recommendation.t1}")
    val temperature1: String,
    @Value("\${recipe.recommendation.t2}")
    val temperature2: String,
    private val recipeRepository: RecipeRepository,
    private val ingredientRepository: IngredientRepository,
    private val recommendationService: RecommendationService
) {

    fun createRecipe(authentication: Authentication, recipeRequest: RecipeRequest): RecipeResponse {
        val recipe = convertRequestToRecipe(authentication, recipeRequest);
        recipeRepository.save(recipe)
        return convertRecipeToResponse(recipe, convertIngredientsToModel(recipeRequest.ingredients, recipe))
    }

    fun updateRecipe(authentication: Authentication, recipeId: Long, recipeRequest: RecipeRequest): RecipeResponse {
        val recipe =
            recipeRepository.findRecipeById(recipeId) ?: throw ApiException(HttpStatus.NOT_FOUND, "Recipe not found")
        checkIfUserCanModifyRecipe(authentication, recipe)

        recipe.title = recipeRequest.title
        recipe.description = recipeRequest.description
        recipe.instructions = recipeRequest.instructions
        recipe.servings = recipeRequest.servings
        recipeRepository.save(recipe)
        return convertRecipeToResponse(recipe, updateIngredients(recipe, recipeRequest))
    }

    fun deleteRecipe(authentication: Authentication, recipeId: Long) {
        val recipe =
            recipeRepository.findRecipeById(recipeId) ?: throw ApiException(HttpStatus.NOT_FOUND, "Recipe not found")
        checkIfUserCanModifyRecipe(authentication, recipe)

        val ingredients = ingredientRepository.findAllByRecipe(recipe)
        ingredients.forEach { ingredient -> ingredientRepository.delete(ingredient) }
        recipeRepository.delete(recipe)
    }

    fun getRecipe(recipeId: Long): RecipeResponse {
        val recipe =
            recipeRepository.findRecipeById(recipeId) ?: throw ApiException(HttpStatus.NOT_FOUND, "Recipe not found")

        return convertRecipeToResponse(recipe, convertIngredientsToModelList(recipe))
    }

    fun getRecipesByUsername(username: String): List<RecipeResponse> =
        convertRecipesBySearchCriteria(recipeRepository.findRecipesByUsernameContainingIgnoreCase(username))

    fun getRecipesByTitle(title: String): List<RecipeResponse> =
        convertRecipesBySearchCriteria(recipeRepository.findRecipesByTitleContainingIgnoreCase(title))

    fun getRecommendedRecipe(): RecipeResponse {
        val temperature = recommendationService.getCurrentTemperature().toDouble()
        var recipes = recipeRepository.findAll()

        if (temperature > temperature1.toDouble()) {
            recipes = getRecommendationsForHotDays(recipes)
        } else if (temperature < temperature2.toDouble()) {
            recipes = getRecommendationsForColdDays(recipes)
        }

        val recommendedRecipe = recipes.randomOrNull() ?: throw ApiException(HttpStatus.NOT_FOUND, "Recipe not found")
        return convertRecipeToResponse(recommendedRecipe, convertIngredientsToModelList(recommendedRecipe))
    }

    private fun convertRequestToRecipe(authentication: Authentication, recipeRequest: RecipeRequest): Recipe =
        Recipe(
            username = authentication.toUser().username,
            title = recipeRequest.title,
            instructions = recipeRequest.instructions,
            description = recipeRequest.description,
            servings = recipeRequest.servings
        )

    private fun convertRecipeToResponse(recipe: Recipe, ingredients: List<IngredientModel>): RecipeResponse =
        RecipeResponse(
            id = recipe.id,
            title = recipe.title,
            username = recipe.username,
            description = recipe.description,
            ingredients = ingredients,
            instructions = recipe.instructions,
            servings = recipe.servings
        )

    private fun updateIngredients(recipe: Recipe, recipeRequest: RecipeRequest): List<IngredientModel> {
        val ingredients = ingredientRepository.findAllByRecipe(recipe)
        ingredients.forEach { ingredient -> ingredientRepository.delete(ingredient) }
        return convertIngredientsToModel(recipeRequest.ingredients, recipe)
    }

    private fun convertIngredientsToModel(ingredients: List<IngredientModel>, recipe: Recipe): List<IngredientModel> {
        val createdIngredients = mutableListOf<IngredientModel>()
        ingredients.forEach { ingredient ->
            val savedIngredient = Ingredient(
                type = ingredient.type,
                unit = ingredient.unit,
                value = ingredient.value,
                recipe = recipe
            )
            ingredientRepository.save(savedIngredient)
            createdIngredients.add(convertIngredientToModel(savedIngredient))
        }
        return createdIngredients
    }

    private fun convertIngredientsToModelList(recipe: Recipe): List<IngredientModel> {
        val parsedIngredients = mutableListOf<IngredientModel>()
        val ingredients = ingredientRepository.findAllByRecipe(recipe)
        ingredients.forEach { ingredient ->
            parsedIngredients.add(convertIngredientToModel(ingredient))
        }
        return parsedIngredients
    }

    private fun convertIngredientToModel(ingredient: Ingredient): IngredientModel =
        IngredientModel(
            type = ingredient.type,
            unit = ingredient.unit,
            value = ingredient.value
        )

    private fun convertRecipesBySearchCriteria(recipes: List<Recipe>): List<RecipeResponse> {
        val parsedRecipes = mutableListOf<RecipeResponse>()
        recipes.forEach { recipe ->
            parsedRecipes.add(convertRecipeToResponse(recipe, convertIngredientsToModelList(recipe)))
        }
        return parsedRecipes
    }

    private fun checkIfUserCanModifyRecipe(authentication: Authentication, recipe: Recipe) {
        if (authentication.toUser().username != recipe.username)
            throw ApiException(HttpStatus.FORBIDDEN, "You don't have rights to modify this recipe")
    }

    private fun getRecommendationsForHotDays(recipes: List<Recipe>): List<Recipe> =
        recipes.filterNot {
            it.instructions.contains("bake", ignoreCase = true) || it.instructions.contains("oven", ignoreCase = true)
        }

    private fun getRecommendationsForColdDays(recipes: List<Recipe>): List<Recipe> =
        recipes.filterNot {
            val ingredients = ingredientRepository.findAllByRecipe(it)
            ingredients.any {
                it.type.contains(
                    "frozen",
                    ignoreCase = true
                )
            }
        }
}