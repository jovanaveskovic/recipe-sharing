package net.azeti.challenge.recipe.data

import jakarta.persistence.*

@Entity
@Table(name = "ingredient")
data class Ingredient(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var value: String,
    @Enumerated(EnumType.STRING)
    var unit: Unit,
    var type: String,
    @ManyToOne
    @JoinColumn(name = "recipe_id", foreignKey = ForeignKey(name = "fk_recipe_ingredient"))
    val recipe: Recipe
)
