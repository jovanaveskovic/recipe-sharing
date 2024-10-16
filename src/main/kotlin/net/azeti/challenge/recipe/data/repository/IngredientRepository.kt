package net.azeti.challenge.recipe.data.repository

import net.azeti.challenge.recipe.data.Ingredient
import net.azeti.challenge.recipe.data.Recipe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IngredientRepository : JpaRepository<Ingredient, Long> {
    fun findAllByRecipe(recipe: Recipe): List<Ingredient>
}