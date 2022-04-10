package com.nolanofra.model

import org.http4s.Uri

object PokemonEndpointResponse {

  case class Pokemon(species: Species)

  case class Species(name: String, url: Uri)

  case class FlavorTextEntries(flavorTextEntries: List[FlavorText])

  case class FlavorText(text: String, language: Language)

  case class Language(lang: String)
}
