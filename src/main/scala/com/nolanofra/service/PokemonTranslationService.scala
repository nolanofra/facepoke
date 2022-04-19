package com.nolanofra.service

import cats.effect.IO
import com.nolanofra.api.TranslationType.{ Shakespeare, TranslationType, Yoda }
import com.nolanofra.api.error.Errors.Errors
import com.nolanofra.api.model.FunTranslationsResponse.{ CaveHabitat, LegendaryPokemon }
import com.nolanofra.api.model.PokemonEndpointResponse.Pokemon
import com.nolanofra.api.{ FunTranslationsApi, PokeApi }
import com.nolanofra.domain.model.FacePoke

class PokemonTranslationService private (private val pokeApi: PokeApi, translationsApi: FunTranslationsApi) {

  def pokemonInformationTranslated(pokemonName: String) =
    for {
      pokemon <- pokeApi.getPokemonSpecies(pokemonName)
      description = pokemon.descriptionFor("en").getOrElse("Description not found")
      text <- translationsApi
        .translate(description, calculateTranslationLanguage(pokemon))
        .flatMap { case t =>
          IO.apply(t.contents.translated)
        }
        .handleErrorWith { case _: Errors =>
          IO.apply(description)
        }
    } yield FacePoke(
      pokemon.name,
      text,
      pokemon.habitat.name,
      pokemon.isLegendary
    )

  private def calculateTranslationLanguage(pokemon: Pokemon): TranslationType =
    pokemon match {
      case LegendaryPokemon(_) => Yoda
      case CaveHabitat(_) => Yoda
      case _ => Shakespeare
    }

}

object PokemonTranslationService {

  def apply(pokemonApi: PokeApi, translationsApi: FunTranslationsApi): PokemonTranslationService =
    new PokemonTranslationService(pokemonApi, translationsApi)
}
