package com.nolanofra.service

import cats.effect.IO
import com.nolanofra.api.PokeApi
import com.nolanofra.api.model.PokemonEndpointResponse.{ FlavorText, Habitat, Language, Pokemon }
import com.nolanofra.domain.model.FacePoke
import munit.CatsEffectSuite

class PokemonServiceTest extends CatsEffectSuite {

  test("Retrieve Pokemon basic information") {

    val pokemonName = "pokemonName"
    val pokemonHabitat = "pokemonHabitat"
    val pokemonDescription = "pokemonDescription"
    val isLegendary = false

    val expectedPokemon =
      FacePoke(
        pokemonName,
        pokemonDescription,
        pokemonHabitat,
        isLegendary
      )

    val pokeApiStub = new PokeApi {
      override def getPokemonSpecies(name: String): IO[Pokemon] = IO(
        Pokemon(pokemonName, Habitat(pokemonHabitat), false, List(FlavorText(pokemonDescription, Language("en"))))
      )
    }

    for {
      actual <- PokemonService(pokeApiStub)
        .pokemonInformation(pokemonName)

    } yield assertEquals(actual, expectedPokemon)
  }

  test("Retrieve pokemon information raises an error") {
    val pokemonName = "pokemonName"

    val pokeApiStub = new PokeApi {
      override def getPokemonSpecies(name: String): IO[Pokemon] = IO.raiseError(throw new Throwable("error"))
    }

    intercept[Throwable] {
      PokemonService(pokeApiStub).pokemonInformation(pokemonName)
    }
  }
}
