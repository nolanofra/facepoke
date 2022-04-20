package com.nolanofra.service

import com.nolanofra.api.PokeApi
import com.nolanofra.api.model.PokemonEndpointResponse.EnglishLanguage
import com.nolanofra.service.model.FacePoke

class PokemonService private (
  private val pokeAPi: PokeApi
) {

  def pokemonInformation(pokemonName: String) =
    for {
      pokemon <- pokeAPi.getPokemonSpecies(pokemonName)
      description = pokemon.descriptionFor(EnglishLanguage.lang)
    } yield FacePoke(
      pokemon.name,
      description,
      pokemon.habitat.name,
      pokemon.isLegendary
    )
}

object PokemonService {

  def apply(pokemonApi: PokeApi) =
    new PokemonService(pokemonApi)
}
