package com.nolanofra.service

import cats.effect.IO
import com.nolanofra.api.TranslationType.TranslationType
import com.nolanofra.api.model.FunTranslationsResponse
import com.nolanofra.api.model.FunTranslationsResponse.{ Contents, Translation }
import com.nolanofra.api.{ FunTranslationsApi, PokeApi }
import com.nolanofra.api.model.PokemonEndpointResponse.{ FlavorText, Habitat, Language, Pokemon }
import com.nolanofra.domain.model.FacePoke
import munit.CatsEffectSuite

class PokemonTranslationServiceTest extends CatsEffectSuite {

  test("Translate pokemon description") {
    val pokemonName = "pokemonName"
    val pokemonHabitat = "pokemonHabitat"
    val pokemonDescription = "pokemonDescription"
    val isLegendary = false

    val expectedTranslation = "translation"

    val expectedPokemon =
      FacePoke(
        pokemonName,
        expectedTranslation,
        pokemonHabitat,
        isLegendary
      )

    val pokeApiStub = new PokeApi {
      override def getPokemonSpecies(name: String): IO[Pokemon] = IO(
        Pokemon(pokemonName, Habitat(pokemonHabitat), false, List(FlavorText(pokemonDescription, Language("en"))))
      )
    }

    val translationApiStub = new FunTranslationsApi {
      override def translate(text: String, translations: TranslationType): IO[FunTranslationsResponse.Translation] = IO(
        Translation(Contents(expectedTranslation))
      )
    }

    for {
      actual <- PokemonTranslationService(pokeApiStub, translationApiStub).pokemonInformationTranslated(pokemonName)

    } yield assertEquals(actual, expectedPokemon)
  }

  test("Retrieve pokemon information raises an error") {
    val pokemonName = "pokemonName"
    val expectedTranslation = "translation"

    val pokeApiStub = new PokeApi {
      override def getPokemonSpecies(name: String): IO[Pokemon] = IO.raiseError(throw new Throwable("error"))
    }

    val translationApiStub = new FunTranslationsApi {
      override def translate(text: String, translations: TranslationType): IO[FunTranslationsResponse.Translation] = IO(
        Translation(Contents(expectedTranslation))
      )
    }

    intercept[Throwable] {
      PokemonTranslationService(pokeApiStub, translationApiStub).pokemonInformationTranslated(pokemonName)
    }
  }
}
