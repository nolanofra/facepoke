package com.nolanofra.service

import cats.effect.IO
import com.nolanofra.api.TranslationType.{ Shakespeare, TranslationType, Yoda }
import com.nolanofra.api.error.Errors.Errors
import com.nolanofra.api.model.FunTranslationsResponse.{ CaveHabitat, LegendaryPokemon }
import com.nolanofra.api.model.PokemonEndpointResponse.Pokemon
import com.nolanofra.api.{ FunTranslationsApi, PokeApi }
import com.nolanofra.service.model.FacePoke

class PokemonTranslationService private (private val pokeApi: PokeApi, translationsApi: FunTranslationsApi) {

  def pokemonInformationTranslated(pokemonName: String) =
    for {
      pokemon <- pokeApi.getPokemonSpecies(pokemonName)
      description = pokemon.descriptionFor("en")
      maybeTranslation <- description.fold(IO(description))(desc =>
        translationsApi
          .translate(desc, calculateTranslationLanguage(pokemon))
          .flatMap { case t =>
            IO.apply(Some(t.contents.translated))
          }
          .handleErrorWith { case _: Errors =>
            IO.apply(Some(desc))
          }
      )
    } yield FacePoke(pokemon.name, maybeTranslation, pokemon.habitat.name, pokemon.isLegendary)

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
