package net.azeti.challenge.recipe.data.repository

import net.azeti.challenge.recipe.data.Recipe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RecipeRepository : JpaRepository<Recipe, Long> {
    fun findRecipeById(recipeId: Long): Recipe?
    fun findRecipesByUsernameContainingIgnoreCase(username: String): List<Recipe>
    fun findRecipesByTitleContainingIgnoreCase(title: String): List<Recipe>
}