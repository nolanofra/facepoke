package com.nolanofra.service

import cats.effect.IO
import com.nolanofra.api.Translations.{ Shakespeare, Translations, Yoda }
import com.nolanofra.api.error.Errors.Errors
import com.nolanofra.api.{ FunTranslationsApi, PokeApi }
import com.nolanofra.domain.model.FacePoke
import com.nolanofra.service.decoder.PokemonJsonDecoder._
import com.nolanofra.service.decoder.TranslationsDecoder._
import com.nolanofra.service.model.PokemonEndpointResponse.Pokemon
import com.nolanofra.service.model.FunTranslationsResponse.{ CaveHabitat, LegendaryPokemon, Translation }

class PokemonTranslationService private (private val pokeApi: PokeApi, translationsApi: FunTranslationsApi) {

  def pokemonInformationTranslated(pokemonName: String) =
    for {
      pokemon <- pokeApi.getPokemonSpecies[Pokemon](pokemonName)
      description = pokemon.descriptionFor("en").getOrElse("Description not found")
      text <- translationsApi
        .translate[Translation](description, calculateTranslationLanguage(pokemon))
        .flatMap { case t: Translation =>
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

  private def calculateTranslationLanguage(pokemon: Pokemon): Translations =
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
