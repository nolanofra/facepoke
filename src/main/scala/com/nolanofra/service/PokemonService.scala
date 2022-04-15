package com.nolanofra.service

import com.nolanofra.api.PokeApi
import com.nolanofra.domain.model.FacePoke
import com.nolanofra.service.model.PokemonEndpointResponse.Pokemon
import decoder.PokemonJsonDecoder._

class PokemonService private (
  private val pokeAPi: PokeApi
) {

  def pokemonInformation(pokemonName: String) =
    for {
      pokemon <- pokeAPi.getPokemonSpecies[Pokemon](pokemonName)
      description = pokemon.descriptionFor("en")
    } yield FacePoke(
      pokemon.name,
      description.getOrElse("Description not found"),
      pokemon.habitat.name,
      pokemon.isLegendary
    )
}

object PokemonService {

  def apply(pokemonApi: PokeApi) =
    new PokemonService(pokemonApi)
}
