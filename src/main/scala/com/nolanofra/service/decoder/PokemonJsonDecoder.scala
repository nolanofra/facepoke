package com.nolanofra.service.decoder

import com.nolanofra.service.model.PokemonEndpointResponse.{ FlavorText, Habitat, Language, Pokemon }
import io.circe.Decoder

object PokemonJsonDecoder {

  implicit val languageDecoder = Decoder.forProduct1("name")(Language)
  implicit val flavorTextDecoder = Decoder.forProduct2("flavor_text", "language")(FlavorText)
  implicit val habitatDecoder = Decoder.forProduct1("name")(Habitat)

  implicit val pokemonDetailDecoder =
    Decoder.forProduct4("name", "habitat", "is_legendary", "flavor_text_entries")(Pokemon)
}
