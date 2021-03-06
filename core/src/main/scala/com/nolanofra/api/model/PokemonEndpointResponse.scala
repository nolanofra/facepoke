package com.nolanofra.api.model

object PokemonEndpointResponse {

  case object EnglishLanguage {
    val lang = "en"
  }

  case class Pokemon(name: String, habitat: Habitat, isLegendary: Boolean, flavorTextEntries: List[FlavorText]) {

    def descriptionFor(language: String) =
      flavorTextEntries.find(e => e.language.lang.equals(language)).map(_.text).map(_.trim)
  }

  case class Habitat(name: String)

  case class FlavorText(text: String, language: Language)

  case class Language(lang: String)
}
