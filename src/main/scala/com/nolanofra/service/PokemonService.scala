package com.nolanofra.service

import com.nolanofra.api.PokeApi
import com.nolanofra.domain.model.FacePoke

class PokemonService private (
  private val pokeAPi: PokeApi
) {

  def pokemonInformation(pokemonName: String) =
    for {
      pokemon <- pokeAPi.getPokemonSpecies(pokemonName)
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
