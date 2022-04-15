package com.nolanofra.service.model

import com.nolanofra.service.model.PokemonEndpointResponse.{ FlavorText, Habitat, Language, Pokemon }
import org.scalatest.funsuite.AnyFunSuite

class PokemonTest extends AnyFunSuite {
  test("Get any of the English Pokemon description") {

    val englishDescriptions = List(Some("some flavor text"), Some("one more flavor text"))

    val pokemon = Pokemon(
      name = "pokemonName",
      habitat = Habitat("habitatName"),
      isLegendary = true,
      flavorTextEntries = List(
        FlavorText("some flavor text", Language("en")),
        FlavorText("some japanese flavor text", Language("ja")),
        FlavorText("some italian flavor text", Language("it")),
        FlavorText("one more flavor text", Language("en"))
      )
    )

    val actual = pokemon.descriptionFor("en")

    assert(englishDescriptions.contains(actual))
  }

  test("No English Pokemon description available") {

    val pokemon = Pokemon(
      name = "pokemonName",
      habitat = Habitat("habitatName"),
      isLegendary = true,
      flavorTextEntries = List(
        FlavorText("some japanese flavor text", Language("ja")),
        FlavorText("some italian flavor text", Language("it"))
      )
    )

    val actual = pokemon.descriptionFor("en")

    assert(actual.isEmpty)
  }
}
