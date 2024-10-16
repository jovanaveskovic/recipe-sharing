package net.azeti.challenge.recipe.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid

@Schema(description = "Recipe response model")
data class RecipeResponse(
    @field:Schema(
        description = "Recipe id",
        example = "1",
        type = "Long"
    )
    val id: Long,
    @field:Schema(
        description = "Recipe title",
        example = "Cheesecake",
        type = "String"
    )
    val title: String,
    @field:Schema(
        description = "Username of the user who created recipe",
        example = "test",
        type = "String"
    )
    val username: String,
    @field:Schema(
        description = "Recipe description",
        example = "Desert",
        type = "String"
    )
    val description: String,
    @field:Schema(
        description = "Recipe ingredients"
    )
    val ingredients: List<IngredientModel>,
    @field:Schema(
        description = "Recipe instructions",
        example = "Brick cream cheese: Four 8-ounce bricks of full-fat cream cheese are the base of this cheesecake. That’s 2 pounds. Make sure you’re buying the bricks of cream cheese and not cream cheese spread. There are no diets allowed in cheesecake, so don’t pick up the reduced-fat variety!Sugar: 1 cup. Not that much considering how many mouths you can feed with this dessert. Over-sweetened cheesecake is hardly cheesecake anymore. Using only 1 cup of sugar gives this cheesecake the opportunity to balance tangy and sweet, just as classic cheesecake should taste.Sour cream: 1 cup. I recently tested a cheesecake recipe with 1 cup of heavy cream instead, but ended up sticking with my original (which can be found here with blueberry swirl cheesecake!). I was curious about the heavy cream addition and figured it would yield a softer cheesecake bite. The cheesecake was soft, but lacked the stability and richness I wanted. It was almost too creamy. Sour cream is most definitely the right choice.A little flavor: 1 teaspoon of pure vanilla extract and 2 of lemon juice. The lemon juice brightens up the cheesecake’s overall flavor and vanilla is always a good idea.Eggs: 3 eggs are the final ingredient. You’ll beat the eggs in last, one at a time, until they are *just* incorporated. Do not overmix the batter once the eggs are added. This will whip air into the cheesecake batter, resulting in cheesecake cracking and deflating.And as always, make sure all of the cheesecake batter ingredients are at room temperature so the batter remains smooth, even, and combines quickly. Beating cold ingredients together will result in a chunky over-beaten cheesecake batter, hardly the way we want to start!",
        type = "String"
    )
    val instructions: String,
    @field:Schema(
        description = "Recipe servings",
        example = "12",
        type = "String"
    )
    val servings: String
)