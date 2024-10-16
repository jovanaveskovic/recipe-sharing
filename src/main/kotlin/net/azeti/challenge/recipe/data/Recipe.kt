package net.azeti.challenge.recipe.data

import jakarta.persistence.*

@Entity
@Table(name = "recipe")
data class Recipe(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var title: String,
    val username: String,
    var description: String,
    @Lob
    var instructions: String,
    var servings: String
)