package com.nolanofra.service.model

import com.nolanofra.service.model.PokemonEndpointResponse.Pokemon

object FunTranslationsResponse {
  case class Contents(translated: String)
  case class Translation(contents: Contents)

  object LegendaryPokemon {

    def unapply(pokemon: Pokemon): Option[Boolean] =
      if (pokemon.isLegendary)
        Some(true)
      else None
  }

  object CaveHabitat {

    def unapply(pokemon: Pokemon): Option[String] =
      if (pokemon.habitat.name.equals("cave"))
        Some(pokemon.habitat.name)
      else None
  }
}
