package net.azeti.challenge.recipe.service

import net.azeti.challenge.recipe.configuration.toUser
import net.azeti.challenge.recipe.data.Ingredient
import net.azeti.challenge.recipe.data.Recipe
import net.azeti.challenge.recipe.data.User
import net.azeti.challenge.recipe.data.Unit
import net.azeti.challenge.recipe.data.repository.IngredientRepository
import net.azeti.challenge.recipe.data.repository.RecipeRepository
import net.azeti.challenge.recipe.exception.ApiException
import net.azeti.challenge.recipe.model.IngredientModel
import net.azeti.challenge.recipe.model.RecipeRequest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RecipeServiceTest {

    private lateinit var recipeService: RecipeService

    @Mock
    private lateinit var recipeRepository: RecipeRepository

    @Mock
    private lateinit var ingredientRepository: IngredientRepository

    @Mock
    private lateinit var recommendationService: RecommendationService

    private val authentication: Authentication = Mockito.mock(Authentication::class.java)

    @BeforeAll
    fun beforeAll() {
        MockitoAnnotations.openMocks(this)

        recipeService =
            RecipeService("28", "2", recipeRepository, ingredientRepository, recommendationService)

        val user = User(
            email = "test@gmail.com",
            username = "test",
            password = "test123"
        )

        val recipe = Recipe(
            id = 1,
            title = "Cheesecake",
            description = "Desert",
            username = "test",
            instructions = "Beat the cream cheese, add the egg...",
            servings = "12"
        )

        val recipe2 = Recipe(
            id = 2,
            title = "Chocolate muffins",
            description = "Desert",
            username = "test",
            instructions = "Turn the oven on 200C to bake the muffins...",
            servings = "12"
        )

        val ingredient2 = Ingredient(
            value = "100",
            unit = Unit.g,
            type = "chocolate",
            recipe = recipe2
        )
        val ingredients = mutableListOf(
            Ingredient(
                value = "100",
                unit = Unit.g,
                type = "frozen berries",
                recipe = recipe
            ),
            Ingredient(
                value = "100",
                unit = Unit.g,
                type = "cream cheese",
                recipe = recipe
            )
        )

        val securityContext: SecurityContext = Mockito.mock(SecurityContext::class.java)
        Mockito.`when`(securityContext.authentication).thenReturn(authentication)
        SecurityContextHolder.setContext(securityContext)
        Mockito.`when`(authentication.principal).thenReturn(user)
        Mockito.`when`(authentication.toUser()).thenReturn(user)
        Mockito.`when`(recipeRepository.save(recipe)).thenReturn(recipe)
        Mockito.`when`(recipeRepository.save(recipe2)).thenReturn(recipe2)
        Mockito.`when`(recipeRepository.findRecipeById(1)).thenReturn(recipe)
        Mockito.`when`(recipeRepository.findRecipeById(2)).thenReturn(recipe2)
        Mockito.`when`(recipeRepository.findAll()).thenReturn(
            mutableListOf(recipe, recipe2)
        )
        Mockito.`when`(recipeRepository.findRecipesByUsernameContainingIgnoreCase("te")).thenReturn(
            mutableListOf(recipe, recipe2)
        )
        Mockito.`when`(recipeRepository.findRecipesByTitleContainingIgnoreCase("cake")).thenReturn(
            mutableListOf(recipe)
        )
        Mockito.`when`(ingredientRepository.findAllByRecipe(recipe)).thenReturn(ingredients)
        Mockito.`when`(ingredientRepository.findAllByRecipe(recipe2)).thenReturn(mutableListOf(ingredient2))
        Mockito.`when`(recommendationService.getCurrentTemperature()).thenReturn("30").thenReturn("1")
    }

    @Test
    fun testCreateRecipe() {
        val recipeResponse = recipeService.createRecipe(
            authentication, RecipeRequest(
                title = "Cheesecake",
                description = "Desert",
                ingredients = mutableListOf(
                    IngredientModel(
                        value = "5",
                        unit = Unit.pc,
                        type = "egg"
                    ),
                    IngredientModel(
                        value = "500",
                        unit = Unit.g,
                        type = "mascrapone"
                    ),
                    IngredientModel(
                        value = "100",
                        unit = Unit.g,
                        type = "frozen berries"
                    )
                ),
                instructions = "Brick cream cheese: Four 8-ounce bricks of full-fat cream cheese are the base of this cheesecake. That’s 2 pounds. Make sure you’re buying the bricks of cream cheese and not cream cheese spread. There are no diets allowed in cheesecake, so don’t pick up the reduced-fat variety!Sugar: 1 cup. Not that much considering how many mouths you can feed with this dessert. Over-sweetened cheesecake is hardly cheesecake anymore. Using only 1 cup of sugar gives this cheesecake the opportunity to balance tangy and sweet, just as classic cheesecake should taste.Sour cream: 1 cup. I recently tested a cheesecake recipe with 1 cup of heavy cream instead, but ended up sticking with my original (which can be found here with blueberry swirl cheesecake!). I was curious about the heavy cream addition and figured it would yield a softer cheesecake bite. The cheesecake was soft, but lacked the stability and richness I wanted. It was almost too creamy. Sour cream is most definitely the right choice.A little flavor: 1 teaspoon of pure vanilla extract and 2 of lemon juice. The lemon juice brightens up the cheesecake’s overall flavor and vanilla is always a good idea.Eggs: 3 eggs are the final ingredient. You’ll beat the eggs in last, one at a time, until they are *just* incorporated. Do not overmix the batter once the eggs are added. This will whip air into the cheesecake batter, resulting in cheesecake cracking and deflating.And as always, make sure all of the cheesecake batter ingredients are at room temperature so the batter remains smooth, even, and combines quickly. Beating cold ingredients together will result in a chunky over-beaten cheesecake batter, hardly the way we want to start!",
                servings = "12"
            )
        )
        assertNotNull(recipeResponse)
        assertEquals("Cheesecake", recipeResponse.title)
    }

    @Test
    fun testUpdateRecipe() {
        val recipeResponse = recipeService.updateRecipe(
            authentication,
            1,
            RecipeRequest(
                title = "Cheesecake",
                description = "new description",
                ingredients = mutableListOf(
                    IngredientModel(
                        value = "5",
                        unit = Unit.pc,
                        type = "egg"
                    ),
                    IngredientModel(
                        value = "500",
                        unit = Unit.g,
                        type = "mascrapone"
                    ),
                    IngredientModel(
                        value = "100",
                        unit = Unit.g,
                        type = "frozen berries"
                    )
                ),
                instructions = "Brick cream cheese: Four 8-ounce bricks of full-fat cream cheese are the base of this cheesecake. That’s 2 pounds. Make sure you’re buying the bricks of cream cheese and not cream cheese spread. There are no diets allowed in cheesecake, so don’t pick up the reduced-fat variety!Sugar: 1 cup. Not that much considering how many mouths you can feed with this dessert. Over-sweetened cheesecake is hardly cheesecake anymore. Using only 1 cup of sugar gives this cheesecake the opportunity to balance tangy and sweet, just as classic cheesecake should taste.Sour cream: 1 cup. I recently tested a cheesecake recipe with 1 cup of heavy cream instead, but ended up sticking with my original (which can be found here with blueberry swirl cheesecake!). I was curious about the heavy cream addition and figured it would yield a softer cheesecake bite. The cheesecake was soft, but lacked the stability and richness I wanted. It was almost too creamy. Sour cream is most definitely the right choice.A little flavor: 1 teaspoon of pure vanilla extract and 2 of lemon juice. The lemon juice brightens up the cheesecake’s overall flavor and vanilla is always a good idea.Eggs: 3 eggs are the final ingredient. You’ll beat the eggs in last, one at a time, until they are *just* incorporated. Do not overmix the batter once the eggs are added. This will whip air into the cheesecake batter, resulting in cheesecake cracking and deflating.And as always, make sure all of the cheesecake batter ingredients are at room temperature so the batter remains smooth, even, and combines quickly. Beating cold ingredients together will result in a chunky over-beaten cheesecake batter, hardly the way we want to start!",
                servings = "12"
            )
        )
        assertNotNull(recipeResponse)
        assertEquals("new description", recipeResponse.description)
    }

    @Test
    fun testDeleteRecipe() {
        recipeService.deleteRecipe(authentication, 2)
        assertTrue {
            recipeRepository.findRecipeById(2) == null
        }
    }

    @Test
    fun testGetRecipe() {
        val recipe = recipeService.getRecipe(1)
        assertNotNull(recipe)
        assertEquals("Cheesecake", recipe.title)
        assertEquals(2, recipe.ingredients.size)
    }

    @Test
    fun testGetRecipeWithWrongId() {
        val exception = assertThrows<ApiException> {
            recipeService.getRecipe(4)
        }
        assertEquals("Recipe not found", exception.message)
    }

    @Test
    fun testGetRecipesByUsername() {
        val recipes = recipeService.getRecipesByUsername("te")
        assertTrue { recipes.isNotEmpty() }
        assertEquals(2, recipes.size)
    }

    @Test
    fun testGetRecipesByTitle() {
        val recipes = recipeService.getRecipesByTitle("cake")
        assertTrue { recipes.isNotEmpty() }
        assertEquals(1, recipes.size)
    }

    @Test
    fun testGetRecommendedRecipeForHotDays() {
        val recipe = recipeService.getRecommendedRecipe()
        assertNotNull(recipe)
        assertEquals(1, recipe.id)
    }

    @Test
    fun testGetRecommendedRecipeForColdDays() {
        val recipe = recipeService.getRecommendedRecipe()
        assertNotNull(recipe)
        assertEquals(2, recipe.id)
    }


}